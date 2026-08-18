package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

public class ObjectContainsFieldsIgnoreNullsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.OBJECT_CONTAINS_FIELDS_IGNORE_NULLS;
    }

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
