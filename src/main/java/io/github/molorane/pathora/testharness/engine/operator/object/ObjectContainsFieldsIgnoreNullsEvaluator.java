package io.github.molorane.pathora.testharness.engine.operator.object;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

/**
 * Operator: {@code OBJECT_CONTAINS_FIELDS_IGNORE_NULLS}
 *
 * <p>Validates that the target JSON object contains specified fields and values, ignoring fields in the expected specification whose value is {@code null}.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.settings",
 *   "Operator": "OBJECT_CONTAINS_FIELDS_IGNORE_NULLS",
 *   "Value": {
 *     "theme": "DARK",
 *     "optionalNote": null
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#OBJECT_CONTAINS_FIELDS_IGNORE_NULLS
 */
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
