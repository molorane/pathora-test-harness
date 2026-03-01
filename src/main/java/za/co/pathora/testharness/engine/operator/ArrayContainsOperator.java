package za.co.pathora.testharness.engine.operator;

import za.co.pathora.testharness.exception.HarnessAssertionException;
import za.co.pathora.testharness.model.AssertionOperator;
import za.co.pathora.testharness.util.AssertionUtils;

import java.util.List;

public class ArrayContainsOperator implements OperatorAssertion {

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
