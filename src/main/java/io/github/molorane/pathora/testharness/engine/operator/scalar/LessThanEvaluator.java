package io.github.molorane.pathora.testharness.engine.operator.scalar;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

/**
 * Operator: {@code LESS_THAN}
 *
 * <p>Validates that the actual numeric value is strictly less than the expected threshold ({@code actual < expected}).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.riskScore",
 *   "Operator": "LESS_THAN",
 *   "Value": 600
 * }
 * }</pre>
 *
 * @see AssertionOperator#LESS_THAN
 */
public class LessThanEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.LESS_THAN;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        Object[] normalized = AssertionUtils.normalizeTypes(normalizedActual, expected);

        double a = ((Number) normalized[0]).doubleValue();
        double e = ((Number) normalized[1]).doubleValue();

        if (!(a < e)) {
            throw new HarnessAssertionException(
                AssertionOperator.LESS_THAN,
                path,
                e,
                a,
                "LESS_THAN failed at " + path +
                    ". Expected < " + e +
                    ", Actual: " + a);
        }
    }
}
