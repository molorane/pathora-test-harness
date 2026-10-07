package io.github.molorane.pathora.testharness.engine.operator.scalar;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

/**
 * Operator: {@code IS_ZERO}
 *
 * <p>Validates that the actual numeric value equals zero ({@code actual == 0}).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.pendingCount",
 *   "Operator": "IS_ZERO"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_ZERO
 */
public class IsZeroEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_ZERO;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalized = AssertionUtils.normalizeResult(actual, path);
        double val = toDouble(normalized, path);

        if (Double.compare(val, 0.0) != 0) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_ZERO,
                path,
                0,
                actual,
                "IS_ZERO failed at " + path +
                    ". Expected zero but was: " + val);
        }
    }

    private double toDouble(Object value, String path) {
        if (value instanceof Number num) {
            return num.doubleValue();
        }
        if (value instanceof String str) {
            try {
                return Double.parseDouble(str.trim());
            } catch (NumberFormatException ignored) {
            }
        }
        throw new IllegalArgumentException("Expected numeric value at " + path + " but was: " + value);
    }
}

