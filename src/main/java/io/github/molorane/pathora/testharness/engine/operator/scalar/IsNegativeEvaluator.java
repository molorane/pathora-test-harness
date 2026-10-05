package io.github.molorane.pathora.testharness.engine.operator.scalar;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

/**
 * Operator: {@code IS_NEGATIVE}
 *
 * <p>Validates that the actual numeric value is strictly negative ({@code actual < 0}).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.lossAmount",
 *   "Operator": "IS_NEGATIVE"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_NEGATIVE
 */
public class IsNegativeEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_NEGATIVE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalized = AssertionUtils.normalizeResult(actual, path);
        double val = toDouble(normalized, path);

        if (val >= 0.0) {
            throw new HarnessAssertionException(
                    AssertionOperator.IS_NEGATIVE,
                    path,
                    "negative number (< 0)",
                    actual,
                    "IS_NEGATIVE failed at " + path +
                            ". Expected negative (< 0) but was: " + val);
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

