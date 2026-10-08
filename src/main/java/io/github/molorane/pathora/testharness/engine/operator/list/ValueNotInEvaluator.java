package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Operator: {@code VALUE_NOT_IN}
 *
 * <p>Validates that the extracted scalar value is not contained within the prohibited list.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.status",
 *   "Operator": "VALUE_NOT_IN",
 *   "Value": ["REJECTED", "ERROR", "BLOCKED"]
 * }
 * }</pre>
 *
 * @see AssertionOperator#VALUE_NOT_IN
 */
public class ValueNotInEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.VALUE_NOT_IN;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        List<?> expectedList = AssertionUtils.requireList(expected, path + " (expected)");

        for (Object exp : expectedList) {
            Object[] normalized = AssertionUtils.normalizeTypes(normalizedActual, exp);
            if (Objects.equals(normalized[0], normalized[1])) {
                throw new HarnessAssertionException(
                    AssertionOperator.VALUE_NOT_IN,
                    path,
                    expected,
                    actual,
                    "VALUE_NOT_IN failed at " + path +
                        ". Expected value not to be in: " + expectedList +
                        ", Actual: " + normalizedActual);
            }
        }
    }
}

