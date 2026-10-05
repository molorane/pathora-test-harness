package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Operator: {@code LIST_SIZE_EQUALS}
 *
 * <p>Validates that the number of elements in the extracted list equals expected count.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.items",
 *   "Operator": "LIST_SIZE_EQUALS",
 *   "Value": 3
 * }
 * }</pre>
 *
 * @see AssertionOperator#LIST_SIZE_EQUALS
 */
public class ListSizeEqualsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.LIST_SIZE_EQUALS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> list;

        if (actual == null) {
            list = Collections.emptyList();
        } else if (actual instanceof List<?>) {
            list = (List<?>) actual;
        } else {
            throw new HarnessAssertionException(
                    AssertionOperator.LIST_SIZE_EQUALS,
                    path,
                    expected,
                    actual,
                    "Expected array at path " + path +
                            " but got: " + actual);
        }

        int expectedSize = ((Number) AssertionUtils.normalizeExpected(expected)).intValue();

        if (list.size() != expectedSize) {
            throw new HarnessAssertionException(
                    AssertionOperator.LIST_SIZE_EQUALS,
                    path,
                    expectedSize,
                    list.size(),
                    "LIST_SIZE_EQUALS failed at " + path +
                            ". Expected size: " + expectedSize +
                            ", Actual size: " + list.size()
            );
        }
    }
}

