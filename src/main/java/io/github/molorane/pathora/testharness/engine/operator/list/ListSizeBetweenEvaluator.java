package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;
import java.util.Map;

/**
 * Operator: {@code LIST_SIZE_BETWEEN}
 *
 * <p>Validates that the number of elements in the extracted list is inclusively between {@code min} and {@code max}.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.results",
 *   "Operator": "LIST_SIZE_BETWEEN",
 *   "Value": {
 *     "min": 1,
 *     "max": 10
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#LIST_SIZE_BETWEEN
 */
public class ListSizeBetweenEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.LIST_SIZE_BETWEEN;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        List<?> list = AssertionUtils.requireList(actual, path);

        Map<String, Object> range = AssertionUtils.toMap(expected);
        Object minObj = range.get("min");
        Object maxObj = range.get("max");

        if (minObj == null || maxObj == null) {
            throw new IllegalArgumentException(
                    "LIST_SIZE_BETWEEN operator requires 'min' and 'max' in Value at " + path);
        }

        int min = toInt(minObj, path + " (min)");
        int max = toInt(maxObj, path + " (max)");
        int size = list.size();

        if (size < min || size > max) {
            throw new HarnessAssertionException(
                    AssertionOperator.LIST_SIZE_BETWEEN,
                    path,
                    "size between " + min + " and " + max,
                    size,
                    "LIST_SIZE_BETWEEN failed at " + path +
                            ". Expected size between " + min + " and " + max +
                            ", but found size " + size);
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
        throw new IllegalArgumentException("Expected integer size at " + path + " but got: " + value);
    }
}

