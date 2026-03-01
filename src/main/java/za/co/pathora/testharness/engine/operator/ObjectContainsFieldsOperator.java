package za.co.pathora.testharness.engine.operator;

import za.co.pathora.testharness.exception.HarnessAssertionException;
import za.co.pathora.testharness.model.AssertionOperator;
import za.co.pathora.testharness.util.AssertionUtils;

public class ObjectContainsFieldsOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);

        if (!AssertionUtils.objectContainsFields(normalizedActual, expected, false)) {
            throw new HarnessAssertionException(
                    AssertionOperator.OBJECT_CONTAINS_FIELDS,
                    path,
                    expected,
                    normalizedActual,
                    "OBJECT_CONTAINS_FIELDS failed at " + path +
                            ". Expected fields: " + expected +
                            ", Actual: " + normalizedActual);
        }
    }
}
