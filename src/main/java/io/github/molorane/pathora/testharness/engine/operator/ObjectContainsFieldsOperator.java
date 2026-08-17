package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

public class ObjectContainsFieldsOperator implements OperatorAssertion {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.OBJECT_CONTAINS_FIELDS;
    }

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
