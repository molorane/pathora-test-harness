package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;
import java.util.Objects;

public class ArrayContainsOnlyOneValueOperator implements OperatorAssertion {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.ARRAY_CONTAINS_ONLY_ONE_VALUE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> list = AssertionUtils.requireList(actual, path);

        if (list.size() != 1) {
            throw new HarnessAssertionException(
                    AssertionOperator.ARRAY_CONTAINS_ONLY_ONE_VALUE,
                    path,
                    expected,
                    list,
                    "ARRAY_CONTAINS_ONLY_ONE_VALUE failed at " + path +
                            ". Expected exactly one element, Actual: " + list);
        }

        Object actualValue = AssertionUtils.normalizeResult(list.get(0), path);
        Object[] normalized = AssertionUtils.normalizeTypes(actualValue, expected);

        if (!Objects.equals(normalized[0], normalized[1])) {
            throw new HarnessAssertionException(
                    AssertionOperator.ARRAY_CONTAINS_ONLY_ONE_VALUE,
                    path,
                    normalized[1],
                    normalized[0],
                    "ARRAY_CONTAINS_ONLY_ONE_VALUE failed at " + path +
                            ". Expected: " + normalized[1] +
                            ", Actual: " + normalized[0]);
        }
    }
}
