package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;

/**
 * Operator: {@code LIST_CONTAINS_ONLY_VALUES}
 *
 * <p>Validates that the list contains exclusively the specified values (same size and containing all expected elements).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.allowedMethods",
 *   "Operator": "LIST_CONTAINS_ONLY_VALUES",
 *   "Value": ["GET", "POST"]
 * }
 * }</pre>
 *
 * @see AssertionOperator#LIST_CONTAINS_ONLY_VALUES
 */
public class ListContainsOnlyValuesEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.LIST_CONTAINS_ONLY_VALUES;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> list = AssertionUtils.requireList(actual, path);
        List<?> expectedList = AssertionUtils.requireList(expected, path);

        if (list.size() != expectedList.size() ||
                !list.containsAll(expectedList)) {

            throw new HarnessAssertionException(
                    AssertionOperator.LIST_CONTAINS_ONLY_VALUES,
                    path,
                    expectedList,
                    list,
                    "LIST_CONTAINS_ONLY_VALUES failed at " + path +
                            ". Expected: " + expectedList +
                            ", Actual: " + list);
        }
    }
}

