package io.github.molorane.pathora.testharness.engine;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.github.molorane.pathora.testharness.engine.operator.DocumentContextAwareOperator;
import io.github.molorane.pathora.testharness.engine.operator.OperatorAssertion;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;

import java.util.Map;

public class AssertionEngine {

    private final Map<AssertionOperator, OperatorAssertion> operators;

    public AssertionEngine() {
        operators = OperatorRegistry.loadOperators();
    }

    public void assertResponse(
            String response,
            RuleTestCase testCase) {

        var assertions = testCase.responseAssertions();
        DocumentContext context = JsonPath.parse(response);

        for (JsonAssertion assertion : assertions) {
            evaluateAssertion(assertion, context, testCase, response);
        }
    }

    private void evaluateAssertion(
            JsonAssertion assertion,
            DocumentContext context,
            RuleTestCase testCase,
            String response) {

        // Handle logical composition operators first
        if (isLogicalOperator(assertion.operator())) {
            evaluateLogicalAssertion(assertion, context, testCase, response);
            return;
        }

        // Context-aware operators resolve their own paths
        OperatorAssertion handler = operators.get(assertion.operator());
        if (handler instanceof DocumentContextAwareOperator contextAware) {
            contextAware.apply(context, assertion.value());
            return;
        }

        evaluatePathAssertion(assertion, context, testCase, response);
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
            String response) {
        if (assertion.operator() == AssertionOperator.AND) {
            evaluateAnd(assertion, context, testCase, response);
        } else if (assertion.operator() == AssertionOperator.OR) {
            evaluateOr(assertion, context, testCase, response);
        } else if (assertion.operator() == AssertionOperator.NOT) {
            evaluateNot(assertion, context, testCase, response);
        }
    }

    private void evaluateAnd(
            JsonAssertion assertion,
            DocumentContext context,
            RuleTestCase testCase,
            String response) {
        if (assertion.assertions() == null || assertion.assertions().isEmpty()) {
            throw new IllegalArgumentException("AND operator requires 'Assertions' list");
        }
        for (JsonAssertion nested : assertion.assertions()) {
            evaluateAssertion(nested, context, testCase, response);
        }
    }

    private void evaluateOr(
            JsonAssertion assertion,
            DocumentContext context,
            RuleTestCase testCase,
            String response) {
        if (assertion.assertions() == null || assertion.assertions().isEmpty()) {
            throw new IllegalArgumentException("OR operator requires 'Assertions' list");
        }
        AssertionError lastError = null;
        for (JsonAssertion nested : assertion.assertions()) {
            try {
                evaluateAssertion(nested, context, testCase, response);
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
            String response) {
        if (assertion.assertions() == null || assertion.assertions().size() != 1) {
            throw new IllegalArgumentException("NOT operator requires exactly one 'Assertions' configured");
        }
        JsonAssertion nested = assertion.assertions().get(0);
        try {
            evaluateAssertion(nested, context, testCase, response);
        } catch (AssertionError e) {
            return; // The nested assertion failed, so NOT passes
        }
        throw new AssertionError(
                "LOGICAL_NOT_FAILED\n" +
                        "Nested assertion passed, but NOT expects it to fail.\n" +
                        "Nested JsonPath: " + nested.jsonPath());
    }

    private void evaluatePathAssertion(
            JsonAssertion assertion,
            DocumentContext context,
            RuleTestCase testCase,
            String response) {
        Object actual = null;
        boolean pathExists = true;

        try {
            actual = context.read(assertion.jsonPath());
        } catch (com.jayway.jsonpath.PathNotFoundException e) {
            pathExists = false;
            handlePathNotFound(assertion, testCase, response, e);
        } catch (Exception e) {
            handlePathException(assertion, testCase, response, e);
        }

        applyAssertion(assertion, actual, pathExists);
    }

    private void handlePathNotFound(
            JsonAssertion assertion,
            RuleTestCase testCase,
            String response,
            com.jayway.jsonpath.PathNotFoundException e) {
        if (assertion.operator() != AssertionOperator.EXISTS) {
            throw new AssertionError(
                    """
                            JSON_PATH_EVALUATION_FAILED
                            -----------------------------------------
                            JsonPath: %s
                            Operator: %s
                            Entry Point: %s

                            Path does not exist in response.

                            Response:
                            %s
                            """.formatted(
                            assertion.jsonPath(),
                            assertion.operator(),
                            testCase.entryPointName(),
                            response),
                    e);
        }
    }

    private void handlePathException(
            JsonAssertion assertion,
            RuleTestCase testCase,
            String response,
            Exception e) {
        throw new AssertionError(
                """
                        JSON_PATH_RUNTIME_ERROR
                        -----------------------------------------
                        JsonPath: %s
                        Operator: %s
                        Entry Point: %s

                        Error: %s

                        Response:
                        %s
                        """.formatted(
                        assertion.jsonPath(),
                        assertion.operator(),
                        testCase.entryPointName(),
                        e.getMessage(),
                        response),
                e);
    }

    public void applyAssertion(JsonAssertion assertion, Object actual, boolean pathExists) {

        OperatorAssertion handler = operators.get(assertion.operator());

        if (handler == null) {
            throw new IllegalArgumentException(
                    "No handler registered for operator: " + assertion.operator());
        }

        handler.apply(
                assertion.jsonPath(),
                actual,
                assertion.value(),
                pathExists);
    }
}