package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;

/**
 * Operator: {@code LIST_IS_SORTED_ASC}
 *
 * <p>Validates that the elements of the list are ordered in ascending order.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.timestamps",
 *   "Operator": "LIST_IS_SORTED_ASC"
 * }
 * }</pre>
 *
 * @see AssertionOperator#LIST_IS_SORTED_ASC
 */
public class ListIsSortedAscEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.LIST_IS_SORTED_ASC;
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

            if (compare(current, next) > 0) {
                throw new HarnessAssertionException(
                        AssertionOperator.LIST_IS_SORTED_ASC,
                        path,
                        "sorted in ascending order",
                        list,
                        "LIST_IS_SORTED_ASC failed at " + path +
                                ". Element at index " + i + " (" + current +
                                ") is greater than element at index " + (i + 1) + " (" + next + ")");
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static int compare(Object a, Object b) {
        if (a == null && b == null) {
            return 0;
        }
        if (a == null) {
            return -1;
        }
        if (b == null) {
            return 1;
        }
        Object[] normalized = AssertionUtils.normalizeTypes(a, b);
        if (normalized[0] instanceof Comparable compA && normalized[1] instanceof Comparable compB) {
            if (compA.getClass().isAssignableFrom(compB.getClass())
                    || compB.getClass().isAssignableFrom(compA.getClass())) {
                return compA.compareTo(compB);
            }
        }
        return String.valueOf(a).compareTo(String.valueOf(b));
    }
}

