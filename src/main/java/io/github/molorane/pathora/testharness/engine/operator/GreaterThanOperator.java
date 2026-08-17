package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

public class GreaterThanOperator implements OperatorAssertion {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.GREATER_THAN;
    }

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
