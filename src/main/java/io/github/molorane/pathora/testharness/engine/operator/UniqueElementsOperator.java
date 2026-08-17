package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueElementsOperator implements OperatorAssertion {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.UNIQUE_ELEMENTS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> list = AssertionUtils.requireList(actual, path);
        Set<Object> seen = new HashSet<>();

        for (int i = 0; i < list.size(); i++) {
            if (!seen.add(list.get(i))) {
                throw new HarnessAssertionException(
                        AssertionOperator.UNIQUE_ELEMENTS,
                        path,
                        "all unique elements",
                        actual,
                        "UNIQUE_ELEMENTS failed at " + path +
                                ". Duplicate found: " + list.get(i) +
                                " at index " + i);
            }
        }
    }
}
