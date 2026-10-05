package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;

/**
 * Operator: {@code IS_NOT_EMPTY_LIST}
 *
 * <p>Validates that the extracted JSON array is not empty (contains at least one element).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.accounts",
 *   "Operator": "IS_NOT_EMPTY_LIST"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_NOT_EMPTY_LIST
 */
public class IsNotEmptyListEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_NOT_EMPTY_LIST;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        List<?> list = AssertionUtils.requireList(actual, path);
        if (list.isEmpty()) {
            throw new HarnessAssertionException(
                    AssertionOperator.IS_NOT_EMPTY_LIST,
                    path,
                    "non-empty list",
                    actual,
                    "IS_NOT_EMPTY_LIST failed at " + path +
                            ". Expected non-empty list but found 0 elements.");
        }
    }
}

