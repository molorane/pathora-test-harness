package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;

/**
 * Operator: {@code LIST_CONTAINS}
 *
 * <p>Validates that the extracted list contains the specified scalar or object value using deep equality.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.supportedCurrencies",
 *   "Operator": "LIST_CONTAINS",
 *   "Value": "ZAR"
 * }
 * }</pre>
 *
 * @see AssertionOperator#LIST_CONTAINS
 */
public class ListContainsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.LIST_CONTAINS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> list = AssertionUtils.requireList(actual, path);

        boolean found = list.stream().anyMatch(element -> AssertionUtils.deepEquals(element, expected));

        if (!found) {
            throw new HarnessAssertionException(
                    AssertionOperator.LIST_CONTAINS,
                    path,
                    expected,
                    list,
                    "LIST_CONTAINS failed at " + path +
                            ". Expected list to contain: " + expected +
                            ", Actual: " + list);
        }
    }
}

