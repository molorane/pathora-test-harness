package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.Map;

/**
 * Operator: {@code STRING_LENGTH_BETWEEN}
 *
 * <p>Validates that the character length of the extracted string lies inclusively between {@code min} and {@code max}.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.password",
 *   "Operator": "STRING_LENGTH_BETWEEN",
 *   "Value": {
 *     "min": 8,
 *     "max": 64
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#STRING_LENGTH_BETWEEN
 */
public class StringLengthBetweenEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.STRING_LENGTH_BETWEEN;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);
        int length = actualStr == null ? 0 : actualStr.length();

        Map<String, Object> range = AssertionUtils.toMap(expected);
        Object minObj = range.get("min");
        Object maxObj = range.get("max");

        if (minObj == null || maxObj == null) {
            throw new IllegalArgumentException(
                    "STRING_LENGTH_BETWEEN operator requires 'min' and 'max' in Value at " + path);
        }

        int min = toInt(minObj, path + " (min)");
        int max = toInt(maxObj, path + " (max)");

        if (length < min || length > max) {
            throw new HarnessAssertionException(
                    AssertionOperator.STRING_LENGTH_BETWEEN,
                    path,
                    "length between " + min + " and " + max,
                    length,
                    "STRING_LENGTH_BETWEEN failed at " + path +
                            ". Expected length between " + min + " and " + max +
                            ", but found length " + length + " for: " + actualStr);
        }
    }

    private int toInt(Object value, String path) {
        if (value instanceof Number num) {
            return num.intValue();
        }
        if (value instanceof String str) {
            try {
                return Integer.parseInt(str.trim());
            } catch (NumberFormatException ignored) {
            }
        }
        throw new IllegalArgumentException("Expected integer length at " + path + " but got: " + value);
    }
}

