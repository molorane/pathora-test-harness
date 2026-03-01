package za.co.pathora.testharness.engine.operator;

import za.co.pathora.testharness.exception.HarnessAssertionException;
import za.co.pathora.testharness.model.AssertionOperator;
import za.co.pathora.testharness.util.AssertionUtils;

import java.util.List;

public class ArrayIsEmptyOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> list = AssertionUtils.requireList(actual, path);

        if (!list.isEmpty()) {
            throw new HarnessAssertionException(
                    AssertionOperator.ARRAY_IS_EMPTY,
                    path,
                    "empty array",
                    actual,
                    "ARRAY_IS_EMPTY failed at " + path +
                            ". Expected empty array but found " + list.size() + " elements: " + list);
        }
    }
}
