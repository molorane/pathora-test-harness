package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;

public class ArrayContainsOnlyValuesOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> list = AssertionUtils.requireList(actual, path);
        List<?> expectedList = AssertionUtils.requireList(expected, path);

        if (list.size() != expectedList.size() ||
                !list.containsAll(expectedList)) {

            throw new HarnessAssertionException(
                    AssertionOperator.ARRAY_CONTAINS_ONLY_VALUES,
                    path,
                    expectedList,
                    list,
                    "ARRAY_CONTAINS_ONLY_VALUES failed at " + path +
                            ". Expected: " + expectedList +
                            ", Actual: " + list);
        }
    }
}
