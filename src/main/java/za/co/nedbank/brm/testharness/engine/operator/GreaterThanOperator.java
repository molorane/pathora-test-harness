package za.co.nedbank.brm.testharness.engine.operator;

import za.co.nedbank.brm.testharness.exception.HarnessAssertionException;
import za.co.nedbank.brm.testharness.model.AssertionOperator;
import za.co.nedbank.brm.testharness.util.AssertionUtils;

public class GreaterThanOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        Object[] normalized = AssertionUtils.normalizeTypes(normalizedActual, expected);

        double a = ((Number) normalized[0]).doubleValue();
        double e = ((Number) normalized[1]).doubleValue();

        if (!(a > e)) {
            throw new HarnessAssertionException(
                    AssertionOperator.GREATER_THAN,
                    path,
                    e,
                    a,
                    "GREATER_THAN failed at " + path +
                            ". Expected > " + e +
                            ", Actual: " + a);
        }
    }
}
