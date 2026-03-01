package za.co.pathora.testharness.engine.operator;

import za.co.pathora.testharness.exception.HarnessAssertionException;
import za.co.pathora.testharness.model.AssertionOperator;
import za.co.pathora.testharness.util.AssertionUtils;

import java.util.List;

public class ArrayContainsObjectWithFieldsOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> list = AssertionUtils.requireList(actual, path);

        boolean found = list.stream()
                .anyMatch(item -> AssertionUtils.objectContainsFields(item, expected, false));

        if (!found) {
            throw new HarnessAssertionException(
                    AssertionOperator.ARRAY_CONTAINS_OBJECT_WITH_FIELDS,
                    path,
                    expected,
                    list,
                    "ARRAY_CONTAINS_OBJECT_WITH_FIELDS failed at " + path +
                            ". Expected object fields: " + expected +
                            ", Actual: " + list);
        }
    }
}
