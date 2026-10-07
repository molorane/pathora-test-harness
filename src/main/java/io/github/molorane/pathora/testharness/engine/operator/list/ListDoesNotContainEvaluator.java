package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Operator: {@code LIST_DOES_NOT_CONTAIN}
 *
 * <p>Validates that the extracted list does not contain the specified element.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.blacklistedUserIds",
 *   "Operator": "LIST_DOES_NOT_CONTAIN",
 *   "Value": "USER-999"
 * }
 * }</pre>
 *
 * @see AssertionOperator#LIST_DOES_NOT_CONTAIN
 */
public class ListDoesNotContainEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.LIST_DOES_NOT_CONTAIN;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        List<?> list = AssertionUtils.requireList(actual, path);

        for (Object item : list) {
            Object[] normalized = AssertionUtils.normalizeTypes(item, expected);
            if (Objects.equals(normalized[0], normalized[1])) {
                throw new HarnessAssertionException(
                    AssertionOperator.LIST_DOES_NOT_CONTAIN,
                    path,
                    expected,
                    actual,
                    "LIST_DOES_NOT_CONTAIN failed at " + path +
                        ". Expected list NOT to contain: " + expected +
                        ", but it was found in: " + list);
            }
        }
    }
}

