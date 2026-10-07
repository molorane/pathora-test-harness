package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;

/**
 * Operator: {@code LIST_SIZE_LESS_THAN}
 *
 * <p>Validates that the number of elements in the extracted list is strictly less than expected threshold.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.warnings",
 *   "Operator": "LIST_SIZE_LESS_THAN",
 *   "Value": 5
 * }
 * }</pre>
 *
 * @see AssertionOperator#LIST_SIZE_LESS_THAN
 */
public class ListSizeLessThanEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.LIST_SIZE_LESS_THAN;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        List<?> list = AssertionUtils.requireList(actual, path);
        int threshold = toInt(expected, path);

        if (list.size() >= threshold) {
            throw new HarnessAssertionException(
                AssertionOperator.LIST_SIZE_LESS_THAN,
                path,
                "< " + threshold,
                list.size(),
                "LIST_SIZE_LESS_THAN failed at " + path +
                    ". Expected size < " + threshold +
                    ", but found size " + list.size());
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

