package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;

/**
 * Operator: {@code LIST_IS_SORTED_DESC}
 *
 * <p>Validates that the elements of the list are ordered in descending order.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.priorities",
 *   "Operator": "LIST_IS_SORTED_DESC"
 * }
 * }</pre>
 *
 * @see AssertionOperator#LIST_IS_SORTED_DESC
 */
public class ListIsSortedDescEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.LIST_IS_SORTED_DESC;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        List<?> list = AssertionUtils.requireList(actual, path);

        if (list.size() <= 1) {
            return;
        }

        for (int i = 0; i < list.size() - 1; i++) {
            Object current = list.get(i);
            Object next = list.get(i + 1);

            if (ListIsSortedAscEvaluator.compare(current, next) < 0) {
                throw new HarnessAssertionException(
                        AssertionOperator.LIST_IS_SORTED_DESC,
                        path,
                        "sorted in descending order",
                        list,
                        "LIST_IS_SORTED_DESC failed at " + path +
                                ". Element at index " + i + " (" + current +
                                ") is less than element at index " + (i + 1) + " (" + next + ")");
            }
        }
    }
}

