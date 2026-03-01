package za.co.nedbank.brm.testharness.engine.operator;

import za.co.nedbank.brm.testharness.exception.HarnessAssertionException;
import za.co.nedbank.brm.testharness.model.AssertionOperator;
import za.co.nedbank.brm.testharness.util.AssertionUtils;

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
