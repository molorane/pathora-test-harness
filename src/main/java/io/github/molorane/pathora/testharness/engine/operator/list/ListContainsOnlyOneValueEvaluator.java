package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Operator: {@code LIST_CONTAINS_ONLY_ONE_VALUE}
 *
 * <p>Validates that the list contains exactly 1 element, and that this element equals expected value.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.primaryAccount",
 *   "Operator": "LIST_CONTAINS_ONLY_ONE_VALUE",
 *   "Value": "ACC-001"
 * }
 * }</pre>
 *
 * @see AssertionOperator#LIST_CONTAINS_ONLY_ONE_VALUE
 */
public class ListContainsOnlyOneValueEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.LIST_CONTAINS_ONLY_ONE_VALUE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> list = AssertionUtils.requireList(actual, path);

        if (list.size() != 1) {
            throw new HarnessAssertionException(
                AssertionOperator.LIST_CONTAINS_ONLY_ONE_VALUE,
                path,
                expected,
                list,
                "LIST_CONTAINS_ONLY_ONE_VALUE failed at " + path +
                    ". Expected exactly one element, Actual: " + list);
        }

        Object actualValue = AssertionUtils.normalizeResult(list.get(0), path);
        Object[] normalized = AssertionUtils.normalizeTypes(actualValue, expected);

        if (!Objects.equals(normalized[0], normalized[1])) {
            throw new HarnessAssertionException(
                AssertionOperator.LIST_CONTAINS_ONLY_ONE_VALUE,
                path,
                normalized[1],
                normalized[0],
                "LIST_CONTAINS_ONLY_ONE_VALUE failed at " + path +
                    ". Expected: " + normalized[1] +
                    ", Actual: " + normalized[0]);
        }
    }
}

