package io.github.molorane.pathora.testharness.engine.operator.scalar;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

/**
 * Operator: {@code IS_INTEGER}
 *
 * <p>Validates that the extracted value represents a whole number/integer with no fractional part.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.termMonths",
 *   "Operator": "IS_INTEGER"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_INTEGER
 */
public class IsIntegerEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_INTEGER;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalized = AssertionUtils.normalizeResult(actual, path);

        boolean isInt = false;
        if (normalized instanceof Integer || normalized instanceof Long
            || normalized instanceof Short || normalized instanceof Byte) {
            isInt = true;
        } else if (normalized instanceof Number num) {
            double d = num.doubleValue();
            isInt = Math.floor(d) == d && !Double.isInfinite(d) && !Double.isNaN(d);
        } else if (normalized instanceof String str) {
            isInt = str.trim().matches("^[+-]?\\d+$");
        }

        if (!isInt) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_INTEGER,
                path,
                "an integer",
                actual,
                "IS_INTEGER failed at " + path +
                    ". Expected integer value but was: " + normalized);
        }
    }
}

