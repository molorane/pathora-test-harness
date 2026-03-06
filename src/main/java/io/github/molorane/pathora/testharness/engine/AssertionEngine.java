package io.github.molorane.pathora.testharness.engine;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.github.molorane.pathora.testharness.engine.operator.*;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;

import java.util.EnumMap;
import java.util.Map;

public class AssertionEngine {

    private final Map<AssertionOperator, OperatorAssertion> operators;

    public AssertionEngine() {
        operators = new EnumMap<>(AssertionOperator.class);

        operators.put(AssertionOperator.EQUALS, new EqualsOperator());
        operators.put(AssertionOperator.NOT_EQUALS, new NotEqualsOperator());
        operators.put(AssertionOperator.GREATER_THAN, new GreaterThanOperator());
        operators.put(AssertionOperator.LESS_THAN, new LessThanOperator());
        operators.put(AssertionOperator.BETWEEN, new BetweenOperator());
        operators.put(AssertionOperator.GREATER_THAN_OR_EQUALS, new GreaterThanOrEqualsOperator());
        operators.put(AssertionOperator.LESS_THAN_OR_EQUALS, new LessThanOrEqualsOperator());
        operators.put(AssertionOperator.REGEX_MATCH, new RegexMatchOperator());
        operators.put(AssertionOperator.STARTS_WITH, new StartsWithOperator());
        operators.put(AssertionOperator.ENDS_WITH, new EndsWithOperator());
        operators.put(AssertionOperator.DATE_BEFORE, new DateBeforeOperator());
        operators.put(AssertionOperator.DATE_AFTER, new DateAfterOperator());
        operators.put(AssertionOperator.DATETIME_BEFORE, new DateTimeBeforeOperator());
        operators.put(AssertionOperator.DATETIME_AFTER, new DateTimeAfterOperator());
        operators.put(AssertionOperator.DATE_BEFORE_NOW, new DateBeforeNowOperator());
        operators.put(AssertionOperator.DATE_AFTER_NOW, new DateAfterNowOperator());
        operators.put(AssertionOperator.DATE_WITHIN_LAST, new DateWithinLastOperator());
        operators.put(AssertionOperator.DATE_WITHIN_NEXT, new DateWithinNextOperator());
        operators.put(AssertionOperator.DURATION_BETWEEN, new DurationBetweenDatesOperator());
        operators.put(AssertionOperator.DURATION_EQUALS, new DurationEqualsOperator());
        operators.put(AssertionOperator.DURATION_GREATER_THAN, new DurationGreaterThanOperator());
        operators.put(AssertionOperator.DURATION_LESS_THAN, new DurationLessThanOperator());
        operators.put(AssertionOperator.DATE_AFTER_DURATION, new DateAfterDurationOperator());
        operators.put(AssertionOperator.DATE_BEFORE_DURATION, new DateBeforeDurationOperator());
        operators.put(AssertionOperator.EXISTS, new ExistsOperator());
        operators.put(AssertionOperator.ARRAY_SIZE_EQUALS, new ArraySizeEqualsOperator());
        operators.put(AssertionOperator.ARRAY_CONTAINS, new ArrayContainsOperator());
        operators.put(AssertionOperator.ARRAY_CONTAINS_ONLY_VALUES, new ArrayContainsOnlyValuesOperator());
        operators.put(AssertionOperator.ARRAY_CONTAINS_ONLY_ONE_VALUE, new ArrayContainsOnlyOneValueOperator());
        operators.put(AssertionOperator.ARRAY_CONTAINS_OBJECT_WITH_FIELDS, new ArrayContainsObjectWithFieldsOperator());
        operators.put(AssertionOperator.ALL_MATCH, new AllMatchOperator());
        operators.put(AssertionOperator.CONTAINS_ANY, new ContainsAnyOperator());
        operators.put(AssertionOperator.CONTAINS_ALL, new ContainsAllOperator());
        operators.put(AssertionOperator.ARRAY_IS_EMPTY, new ArrayIsEmptyOperator());
        operators.put(AssertionOperator.UNIQUE_ELEMENTS, new UniqueElementsOperator());
        operators.put(AssertionOperator.OBJECT_CONTAINS_FIELDS, new ObjectContainsFieldsOperator());
        operators.put(AssertionOperator.OBJECT_CONTAINS_FIELDS_IGNORE_NULLS,
                new ObjectContainsFieldsIgnoreNullsOperator());
        operators.put(AssertionOperator.HAS_KEYS, new HasKeysOperator());
        operators.put(AssertionOperator.FIELD_EQUALS_OTHER_FIELD, new FieldEqualsOtherFieldOperator());
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
        if (assertion.operator() == AssertionOperator.AND) {
            if (assertion.assertions() == null || assertion.assertions().isEmpty()) {
                throw new IllegalArgumentException("AND operator requires 'Assertions' list");
            }
            for (JsonAssertion nested : assertion.assertions()) {
                evaluateAssertion(nested, context, testCase, response);
            }
            return;
        }

        if (assertion.operator() == AssertionOperator.OR) {
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
                            "Last error was: " + lastError.getMessage(),
                    lastError);
        }

        if (assertion.operator() == AssertionOperator.NOT) {
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

        // Context-aware operators resolve their own paths
        OperatorAssertion handler = operators.get(assertion.operator());
        if (handler instanceof DocumentContextAwareOperator contextAware) {
            contextAware.apply(context, assertion.value());
            return;
        }

        Object actual = null;
        boolean pathExists = true;

        try {
            actual = context.read(assertion.jsonPath());

        } catch (com.jayway.jsonpath.PathNotFoundException e) {

            pathExists = false;

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

        } catch (Exception e) {

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

        applyAssertion(assertion, actual, pathExists);
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