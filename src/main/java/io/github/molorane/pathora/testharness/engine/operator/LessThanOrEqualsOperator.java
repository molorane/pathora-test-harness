package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

public class LessThanOrEqualsOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        Object[] normalized = AssertionUtils.normalizeTypes(normalizedActual, expected);

        double actualValue = ((Number) normalized[0]).doubleValue();
        double expectedValue = ((Number) normalized[1]).doubleValue();

        if (actualValue > expectedValue) {
            throw new HarnessAssertionException(
                    AssertionOperator.LESS_THAN_OR_EQUALS,
                    path,
                    expected,
                    actual,
                    "LESS_THAN_OR_EQUALS failed at " + path +
                            ". Expected <= " + expectedValue +
                            ", Actual: " + actualValue);
        }
    }
}
