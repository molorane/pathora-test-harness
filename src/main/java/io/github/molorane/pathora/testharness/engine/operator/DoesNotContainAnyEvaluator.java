package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;
import java.util.Objects;

public class DoesNotContainAnyEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DOES_NOT_CONTAIN_ANY;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> actualList = AssertionUtils.requireList(actual, path);
        List<?> expectedList = AssertionUtils.requireList(expected, path + " (expected)");

        for (Object exp : expectedList) {
            for (Object act : actualList) {
                Object[] normalized = AssertionUtils.normalizeTypes(act, exp);
                if (Objects.equals(normalized[0], normalized[1])) {
                    // Found a match, so it DOES contain something, which means this fails
                    throw new HarnessAssertionException(
                            AssertionOperator.DOES_NOT_CONTAIN_ANY,
                            path,
                            expected,
                            actual,
                            "DOES_NOT_CONTAIN_ANY failed at " + path +
                                    ". Array contains at least one of: " + expectedList +
                                    ", Actual: " + actualList);
                }
            }
        }
        // No match found, so DOES_NOT_CONTAIN_ANY passes
    }
}

