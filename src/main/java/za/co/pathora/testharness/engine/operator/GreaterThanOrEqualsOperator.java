package za.co.pathora.testharness.engine.operator;

import za.co.pathora.testharness.exception.HarnessAssertionException;
import za.co.pathora.testharness.model.AssertionOperator;
import za.co.pathora.testharness.util.AssertionUtils;

public class GreaterThanOrEqualsOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        Object[] normalized = AssertionUtils.normalizeTypes(normalizedActual, expected);

        double actualValue = ((Number) normalized[0]).doubleValue();
        double expectedValue = ((Number) normalized[1]).doubleValue();

        if (actualValue < expectedValue) {
            throw new HarnessAssertionException(
                    AssertionOperator.GREATER_THAN_OR_EQUALS,
                    path,
                    expected,
                    actual,
                    "GREATER_THAN_OR_EQUALS failed at " + path +
                            ". Expected >= " + expectedValue +
                            ", Actual: " + actualValue);
        }
    }
}
