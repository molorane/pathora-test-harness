package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;

/**
 * Operator: {@code IS_EMPTY_LIST}
 *
 * <p>Validates that the extracted JSON array contains 0 elements ({@code []}).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.errors",
 *   "Operator": "IS_EMPTY_LIST"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_EMPTY_LIST
 */
public class IsEmptyListEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_EMPTY_LIST;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> list = AssertionUtils.requireList(actual, path);

        if (!list.isEmpty()) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_EMPTY_LIST,
                path,
                "empty list",
                actual,
                "IS_EMPTY_LIST failed at " + path +
                    ". Expected empty list but found " + list.size() + " elements: " + list);
        }
    }
}

