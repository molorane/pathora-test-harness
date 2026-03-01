package za.co.nedbank.brm.testharness.engine.operator;

import za.co.nedbank.brm.testharness.exception.HarnessAssertionException;
import za.co.nedbank.brm.testharness.model.AssertionOperator;
import za.co.nedbank.brm.testharness.util.AssertionUtils;

public class ObjectContainsFieldsIgnoreNullsOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);

        if (!AssertionUtils.objectContainsFields(normalizedActual, expected, true)) {
            throw new HarnessAssertionException(
                    AssertionOperator.OBJECT_CONTAINS_FIELDS_IGNORE_NULLS,
                    path,
                    expected,
                    normalizedActual,
                    "OBJECT_CONTAINS_FIELDS_IGNORE_NULLS failed at " + path +
                            ". Expected fields: " + expected +
                            ", Actual: " + normalizedActual);
        }
    }
}
