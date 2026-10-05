package io.github.molorane.pathora.testharness.engine.operator.scalar;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

/**
 * Operator: {@code GREATER_THAN}
 *
 * <p>Validates that the actual numeric value is strictly greater than the expected threshold ({@code actual > expected}).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.score",
 *   "Operator": "GREATER_THAN",
 *   "Value": 50
 * }
 * }</pre>
 *
 * @see AssertionOperator#GREATER_THAN
 */
public class GreaterThanEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.GREATER_THAN;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        Object[] normalized = AssertionUtils.normalizeTypes(normalizedActual, expected);

        double a = ((Number) normalized[0]).doubleValue();
        double e = ((Number) normalized[1]).doubleValue();

        if (!(a > e)) {
            throw new HarnessAssertionException(
                    AssertionOperator.GREATER_THAN,
                    path,
                    e,
                    a,
                    "GREATER_THAN failed at " + path +
                            ". Expected > " + e +
                            ", Actual: " + a);
        }
    }
}
