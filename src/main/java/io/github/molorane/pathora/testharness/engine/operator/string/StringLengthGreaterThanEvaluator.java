package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code STRING_LENGTH_GREATER_THAN}
 *
 * <p>Validates that the character length of the extracted string is strictly greater than threshold.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.comment",
 *   "Operator": "STRING_LENGTH_GREATER_THAN",
 *   "Value": 10
 * }
 * }</pre>
 *
 * @see AssertionOperator#STRING_LENGTH_GREATER_THAN
 */
public class StringLengthGreaterThanEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.STRING_LENGTH_GREATER_THAN;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);
        int threshold = toInt(expected, path);
        int actualLength = actualStr == null ? 0 : actualStr.length();

        if (actualLength <= threshold) {
            throw new HarnessAssertionException(
                    AssertionOperator.STRING_LENGTH_GREATER_THAN,
                    path,
                    "> " + threshold,
                    actualLength,
                    "STRING_LENGTH_GREATER_THAN failed at " + path +
                            ". Expected length > " + threshold +
                            ", but found length " + actualLength + " for value: " + actualStr);
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

