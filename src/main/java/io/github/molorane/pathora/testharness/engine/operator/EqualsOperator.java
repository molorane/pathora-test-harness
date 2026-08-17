package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.Objects;

public class EqualsOperator implements OperatorAssertion {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.EQUALS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        Object[] normalized = AssertionUtils.normalizeTypes(normalizedActual, expected);

        Object finalActual = normalized[0];
        Object finalExpected = normalized[1];

        if (!Objects.equals(finalActual, finalExpected)) {
            throw new HarnessAssertionException(
                    AssertionOperator.EQUALS,
                    path,
                    finalExpected,
                    finalActual,
                    "EQUALS failed at " + path +
                            ". Expected: " + finalExpected +
                            ", Actual: " + finalActual);
        }
    }
}
