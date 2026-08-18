package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;
import java.util.Objects;

public class ValueInEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.VALUE_IN;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        List<?> expectedList = AssertionUtils.requireList(expected, path + " (expected)");

        for (Object exp : expectedList) {
            Object[] normalized = AssertionUtils.normalizeTypes(normalizedActual, exp);
            if (Objects.equals(normalized[0], normalized[1])) {
                return; // found match
            }
        }

        throw new HarnessAssertionException(
                AssertionOperator.VALUE_IN,
                path,
                expected,
                actual,
                "VALUE_IN failed at " + path +
                        ". Expected value to be in: " + expectedList +
                        ", Actual: " + normalizedActual);
    }
}

