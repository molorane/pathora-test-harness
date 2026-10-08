package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Operator: {@code CONTAINS_ALL}
 *
 * <p>Validates that the extracted list contains all items present in the expected list.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.permissions",
 *   "Operator": "CONTAINS_ALL",
 *   "Value": ["READ", "WRITE"]
 * }
 * }</pre>
 *
 * @see AssertionOperator#CONTAINS_ALL
 */
public class ContainsAllEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.CONTAINS_ALL;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> actualList = AssertionUtils.requireList(actual, path);
        List<?> expectedList = AssertionUtils.requireList(expected, path + " (expected)");

        for (Object exp : expectedList) {
            boolean found = false;
            for (Object act : actualList) {
                Object[] normalized = AssertionUtils.normalizeTypes(act, exp);
                if (Objects.equals(normalized[0], normalized[1])) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new HarnessAssertionException(
                    AssertionOperator.CONTAINS_ALL,
                    path,
                    expected,
                    actual,
                    "CONTAINS_ALL failed at " + path +
                        ". Missing value: " + exp +
                        ", Expected all of: " + expectedList +
                        ", Actual: " + actualList);
            }
        }
    }
}
