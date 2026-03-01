package za.co.nedbank.brm.testharness.engine.operator;

import za.co.nedbank.brm.testharness.exception.HarnessAssertionException;
import za.co.nedbank.brm.testharness.model.AssertionOperator;
import za.co.nedbank.brm.testharness.util.AssertionUtils;

import java.util.Collections;
import java.util.List;

public class ArraySizeEqualsOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> list;

        if (actual == null) {
            list = Collections.emptyList();
        } else if (actual instanceof List<?>) {
            list = (List<?>) actual;
        } else {
            throw new HarnessAssertionException(
                    AssertionOperator.ARRAY_SIZE_EQUALS,
                    path,
                    expected,
                    actual,
                    "Expected array at path " + path +
                            " but got: " + actual);
        }

        int expectedSize = ((Number) AssertionUtils.normalizeExpected(expected)).intValue();

        if (list.size() != expectedSize) {
            throw new HarnessAssertionException(
                    AssertionOperator.ARRAY_SIZE_EQUALS,
                    path,
                    expectedSize,
                    list.size(),
                    "ARRAY_SIZE_EQUALS failed at " + path +
                            ". Expected size: " + expectedSize +
                            ", Actual size: " + list.size());
        }
    }
}
