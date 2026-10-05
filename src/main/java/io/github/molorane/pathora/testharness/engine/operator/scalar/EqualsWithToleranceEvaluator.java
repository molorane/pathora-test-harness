package io.github.molorane.pathora.testharness.engine.operator.scalar;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.Map;

/**
 * Operator: {@code EQUALS_WITH_TOLERANCE}
 *
 * <p>Validates numeric equality between actual and expected values within an allowed absolute tolerance delta.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.interestRate",
 *   "Operator": "EQUALS_WITH_TOLERANCE",
 *   "Value": {
 *     "expected": 6.5,
 *     "tolerance": 0.05
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#EQUALS_WITH_TOLERANCE
 */
public class EqualsWithToleranceEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.EQUALS_WITH_TOLERANCE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);

        double actualVal = toDouble(normalizedActual, path);
        double expectedVal;
        double tolerance;

        if (expected instanceof Map<?, ?> map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> conf = (Map<String, Object>) map;
            expectedVal = toDouble(conf.get("value"), path + " (expected value)");
            tolerance = toDouble(conf.getOrDefault("tolerance", 0.0), path + " (tolerance)");
        } else {
            throw new IllegalArgumentException(
                    "EQUALS_WITH_TOLERANCE requires Value object with 'value' and 'tolerance' at " + path);
        }

        if (Math.abs(actualVal - expectedVal) > tolerance) {
            throw new HarnessAssertionException(
                    AssertionOperator.EQUALS_WITH_TOLERANCE,
                    path,
                    expected,
                    actual,
                    "EQUALS_WITH_TOLERANCE failed at " + path +
                            ". Expected: " + expectedVal + " (+/- " + tolerance + "), Actual: " + actualVal);
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

