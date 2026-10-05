package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;

/**
 * Operator: {@code CONTAINS_ANY}
 *
 * <p>Validates that the extracted list contains at least one item from the expected list.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.tags",
 *   "Operator": "CONTAINS_ANY",
 *   "Value": ["VIP", "PREMIUM"]
 * }
 * }</pre>
 *
 * @see AssertionOperator#CONTAINS_ANY
 */
public class ContainsAnyEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.CONTAINS_ANY;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> actualList = AssertionUtils.requireList(actual, path);
        List<?> expectedList = AssertionUtils.requireList(expected, path + " (expected)");

        for (Object exp : expectedList) {
            for (Object act : actualList) {
                Object[] normalized = AssertionUtils.normalizeTypes(act, exp);
                if (java.util.Objects.equals(normalized[0], normalized[1])) {
                    return; // found at least one match
                }
            }
        }

        throw new HarnessAssertionException(
                AssertionOperator.CONTAINS_ANY,
                path,
                expected,
                actual,
                "CONTAINS_ANY failed at " + path +
                        ". Expected array to contain at least one of: " + expectedList +
                        ", Actual: " + actualList);
    }
}
