package io.github.molorane.pathora.testharness.engine.operator.object;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

/**
 * Operator: {@code OBJECT_CONTAINS_FIELDS}
 *
 * <p>Validates that the target JSON object contains all specified fields and exact matching values.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.user",
 *   "Operator": "OBJECT_CONTAINS_FIELDS",
 *   "Value": {
 *     "id": 101,
 *     "status": "ACTIVE"
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#OBJECT_CONTAINS_FIELDS
 */
public class ObjectContainsFieldsEvaluator implements AssertionEvaluator {

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
