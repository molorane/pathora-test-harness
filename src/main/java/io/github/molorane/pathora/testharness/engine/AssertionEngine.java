package io.github.molorane.pathora.testharness.engine;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.engine.operator.DocumentContextAwareEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import io.github.molorane.pathora.testharness.util.DateExpressionResolver;
import io.github.molorane.pathora.testharness.util.PathoraClock;

import java.time.Clock;
import java.time.ZoneId;

/**
 * Core engine responsible for evaluating assertion rules against response payloads.
 *
 * <p>Supports standard JSONPath leaf assertions, document-context-aware cross-field assertions,
 * and composable logical operators ({@code AND}, {@code OR}, {@code NOT}).</p>
 */
public class AssertionEngine {

    private final OperatorRegistry operatorRegistry;

    /**
     * Constructs a new {@code AssertionEngine} with a default {@link OperatorRegistry}.
     */
    public AssertionEngine() {
        this.operatorRegistry = new OperatorRegistry();
    }

    /**
     * Asserts that a response string satisfies all assertions defined in the test case.
     *
     * @param response the response payload string
     * @param testCase the rule test case specifying assertions
     */
    public void assertResponse(
        String response,
        RuleTestCase testCase) {

        assertResponse(response, testCase, null);
    }

    /**
     * Asserts that a response string satisfies all assertions defined in the test case, recording the mutated request on failure.
     *
     * @param response       the response payload string
     * @param testCase       the rule test case specifying assertions
     * @param mutatedRequest the mutated request string sent to produce the response
     */
    public void assertResponse(
        String response,
        RuleTestCase testCase,
        String mutatedRequest
    ) {

        var assertions = testCase.assertions();
        DocumentContext context = JsonPath.parse(response);

        boolean customTimezone = testCase.timezone() != null && !testCase.timezone().isBlank();
        if (customTimezone) {
            PathoraClock.setThreadClock(Clock.system(ZoneId.of(testCase.timezone().trim())));
        }

        try {
            for (JsonAssertion assertion : assertions) {
                try {
                    evaluateAssertion(assertion, context, testCase, response, mutatedRequest);
                } catch (HarnessAssertionException e) {
                    if (e.testName() == null && e.testDescription() == null) {
                        throw e.withTestDetails(testCase.name(), testCase.description());
                    }
                    throw e;
                }
            }
        } finally {
            if (customTimezone) {
                PathoraClock.clearThreadClock();
            }
        }
    }

    private void evaluateAssertion(
        JsonAssertion assertion,
        DocumentContext context,
        RuleTestCase testCase,
        String response,
        String mutatedRequest) {

        // Handle logical composition operators first
        if (isLogicalOperator(assertion.operator())) {
            evaluateLogicalAssertion(assertion, context, testCase, response, mutatedRequest);
            return;
        }

        // Context-aware operators resolve their own paths
        AssertionEvaluator handler = operatorRegistry.get(assertion.operator());
        if (handler instanceof DocumentContextAwareEvaluator contextAware) {
            Object resolvedValue = DateExpressionResolver.resolve(assertion.value());
            contextAware.apply(context, resolvedValue);
            return;
        }

        evaluatePathAssertion(assertion, context, testCase, response, mutatedRequest);
    }

    private boolean isLogicalOperator(AssertionOperator operator) {
        return operator == AssertionOperator.AND
            || operator == AssertionOperator.OR
            || operator == AssertionOperator.NOT;
    }

    private void evaluateLogicalAssertion(
        JsonAssertion assertion,
        DocumentContext context,
        RuleTestCase testCase,
        String response,
        String mutatedRequest) {
        if (assertion.operator() == AssertionOperator.AND) {
            evaluateAnd(assertion, context, testCase, response, mutatedRequest);
        } else if (assertion.operator() == AssertionOperator.OR) {
            evaluateOr(assertion, context, testCase, response, mutatedRequest);
        } else if (assertion.operator() == AssertionOperator.NOT) {
            evaluateNot(assertion, context, testCase, response, mutatedRequest);
        }
    }

    private void evaluateAnd(
        JsonAssertion assertion,
        DocumentContext context,
        RuleTestCase testCase,
        String response,
        String mutatedRequest) {
        if (assertion.assertions() == null || assertion.assertions().isEmpty()) {
            throw new IllegalArgumentException("AND operator requires 'Assertions' list");
        }
        for (JsonAssertion nested : assertion.assertions()) {
            evaluateAssertion(nested, context, testCase, response, mutatedRequest);
        }
    }

    private void evaluateOr(
        JsonAssertion assertion,
        DocumentContext context,
        RuleTestCase testCase,
        String response,
        String mutatedRequest) {
        if (assertion.assertions() == null || assertion.assertions().isEmpty()) {
            throw new IllegalArgumentException("OR operator requires 'Assertions' list");
        }
        AssertionError lastError = null;
        for (JsonAssertion nested : assertion.assertions()) {
            try {
                evaluateAssertion(nested, context, testCase, response, mutatedRequest);
                return; // At least one passed, so OR is satisfied
            } catch (AssertionError e) {
                lastError = e;
            }
        }
        throw new AssertionError(
            "LOGICAL_OR_FAILED\n" +
                "None of the nested assertions passed.\n" +
                "Last error was: " + (lastError != null ? lastError.getMessage() : "null"),
            lastError);
    }

    private void evaluateNot(
        JsonAssertion assertion,
        DocumentContext context,
        RuleTestCase testCase,
        String response,
        String mutatedRequest) {
        if (assertion.assertions() == null || assertion.assertions().size() != 1) {
            throw new IllegalArgumentException("NOT operator requires exactly one 'Assertions' configured");
        }
        JsonAssertion nested = assertion.assertions().get(0);
        try {
            evaluateAssertion(nested, context, testCase, response, mutatedRequest);
        } catch (AssertionError e) {
            return; // The nested assertion failed, so NOT passes
        }
        throw new AssertionError(
            "LOGICAL_NOT_FAILED\n" +
                "Nested assertion passed, but NOT expects it to fail.\n" +
                "Nested JsonPath: " + nested.path());
    }

    private void evaluatePathAssertion(
        JsonAssertion assertion,
        DocumentContext context,
        RuleTestCase testCase,
        String response,
        String mutatedRequest) {
        Object actual = null;
        boolean pathExists = true;

        try {
            actual = context.read(assertion.path());
        } catch (com.jayway.jsonpath.PathNotFoundException e) {
            pathExists = false;
            handlePathNotFound(assertion, testCase, response, mutatedRequest, e);
        } catch (Exception e) {
            handlePathException(assertion, testCase, response, mutatedRequest, e);
        }

        applyAssertion(assertion, actual, pathExists);
    }

    private void handlePathNotFound(
        JsonAssertion assertion,
        RuleTestCase testCase,
        String response,
        String mutatedRequest,
        com.jayway.jsonpath.PathNotFoundException e) {
        if (assertion.operator() != AssertionOperator.PATH_EXISTS
            && assertion.operator() != AssertionOperator.PATH_NOT_EXISTS) {
            throw new AssertionError(
                """
                    JSON_PATH_EVALUATION_FAILED
                    -----------------------------------------
                    JsonPath: %s
                    Expected Value: %s
                    Operator: %s
                    Operation: %s
                    
                    Path does not exist in response.
                    
                    Response:
                    %s
                    Mutated Request:
                    %s
                    """.formatted(
                    assertion.path(),
                    assertion.value(),
                    assertion.operator(),
                    testCase.operation(),
                    response,
                    mutatedRequest),
                e);
        }
    }

    private void handlePathException(
        JsonAssertion assertion,
        RuleTestCase testCase,
        String response,
        String mutatedRequest,
        Exception e) {
        throw new AssertionError(
            """
                JSON_PATH_RUNTIME_ERROR
                -----------------------------------------
                JsonPath: %s
                Expected Value: %s
                Operator: %s
                Operation: %s
                
                Error: %s
                
                Response:
                %s
                Mutated Request:
                %s
                """.formatted(
                assertion.path(),
                assertion.value(),
                assertion.operator(),
                testCase.operation(),
                e.getMessage(),
                response,
                mutatedRequest),
            e);
    }

    /**
     * Applies a single assertion against an extracted actual value and path existence indicator.
     *
     * @param assertion  the assertion definition
     * @param actual     the actual value read from JSONPath
     * @param pathExists whether the target JSONPath was found in the document
     */
    public void applyAssertion(JsonAssertion assertion, Object actual, boolean pathExists) {

        AssertionEvaluator handler = operatorRegistry.get(assertion.operator());

        if (handler == null) {
            throw new IllegalArgumentException(
                "No handler registered for operator: " + assertion.operator());
        }

        Object resolvedValue = DateExpressionResolver.resolve(assertion.value());
        handler.apply(
            assertion.path(),
            actual,
            resolvedValue,
            pathExists);
    }
}

