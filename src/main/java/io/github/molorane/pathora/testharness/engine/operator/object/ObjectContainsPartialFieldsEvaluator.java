package io.github.molorane.pathora.testharness.engine.operator.object;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

/**
 * Operator: {@code OBJECT_CONTAINS_PARTIAL_FIELDS}
 *
 * <p>Validates that the target JSON object matches the expected partial structure recursively.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.customer",
 *   "Operator": "OBJECT_CONTAINS_PARTIAL_FIELDS",
 *   "Value": {
 *     "contact": {
 *       "email": "user@example.com"
 *     }
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#OBJECT_CONTAINS_PARTIAL_FIELDS
 */
public class ObjectContainsPartialFieldsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.OBJECT_CONTAINS_PARTIAL_FIELDS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);

        if (!AssertionUtils.objectContainsPartialFields(normalizedActual, expected, false)) {
            throw new HarnessAssertionException(
                    AssertionOperator.OBJECT_CONTAINS_PARTIAL_FIELDS,
                    path,
                    expected,
                    normalizedActual,
                    "OBJECT_CONTAINS_PARTIAL_FIELDS failed at " + path +
                            ". Expected partial fields: " + expected +
                            ", Actual: " + normalizedActual);
        }
    }
}

