package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;

public class ArrayContainsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.ARRAY_CONTAINS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> list = AssertionUtils.requireList(actual, path);

        Object normalizedExpected = AssertionUtils.normalizeTypes(
                AssertionUtils.normalizeResult(expected, path), expected)[1];

        if (!list.contains(normalizedExpected)) {
            throw new HarnessAssertionException(
                    AssertionOperator.ARRAY_CONTAINS,
                    path,
                    normalizedExpected,
                    list,
                    "ARRAY_CONTAINS failed at " + path +
                            ". Expected array to contain: " + normalizedExpected +
                            ", Actual: " + list);
        }
    }
}
