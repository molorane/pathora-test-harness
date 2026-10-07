package io.github.molorane.pathora.testharness.engine.operator.scalar;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

/**
 * Operator: {@code GREATER_THAN_OR_EQUALS}
 *
 * <p>Validates that the actual numeric value is greater than or equal to the expected threshold ({@code actual >= expected}).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.score",
 *   "Operator": "GREATER_THAN_OR_EQUALS",
 *   "Value": 750
 * }
 * }</pre>
 *
 * @see AssertionOperator#GREATER_THAN_OR_EQUALS
 */
public class GreaterThanOrEqualsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.GREATER_THAN_OR_EQUALS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        Object[] normalized = AssertionUtils.normalizeTypes(normalizedActual, expected);

        double actualValue = ((Number) normalized[0]).doubleValue();
        double expectedValue = ((Number) normalized[1]).doubleValue();

        if (actualValue < expectedValue) {
            throw new HarnessAssertionException(
                AssertionOperator.GREATER_THAN_OR_EQUALS,
                path,
                expected,
                actual,
                "GREATER_THAN_OR_EQUALS failed at " + path +
                    ". Expected >= " + expectedValue +
                    ", Actual: " + actualValue);
        }
    }
}
