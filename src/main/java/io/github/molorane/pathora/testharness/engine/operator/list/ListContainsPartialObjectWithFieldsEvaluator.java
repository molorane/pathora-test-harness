package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;

/**
 * Operator: {@code LIST_CONTAINS_PARTIAL_OBJECT_WITH_FIELDS}
 *
 * <p>Validates that the list contains at least one object satisfying the partial nested fields condition.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.customers",
 *   "Operator": "LIST_CONTAINS_PARTIAL_OBJECT_WITH_FIELDS",
 *   "Value": {
 *     "address": {
 *       "city": "Johannesburg"
 *     }
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#LIST_CONTAINS_PARTIAL_OBJECT_WITH_FIELDS
 */
public class ListContainsPartialObjectWithFieldsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.LIST_CONTAINS_PARTIAL_OBJECT_WITH_FIELDS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        List<?> list = AssertionUtils.requireList(actual, path);

        boolean found = list.stream()
            .anyMatch(item -> AssertionUtils.objectContainsPartialFields(item, expected, false));

        if (!found) {
            throw new HarnessAssertionException(
                AssertionOperator.LIST_CONTAINS_PARTIAL_OBJECT_WITH_FIELDS,
                path,
                expected,
                list,
                "LIST_CONTAINS_PARTIAL_OBJECT_WITH_FIELDS failed at " + path +
                    ". Expected partial object fields: " + expected +
                    ", Actual: " + list);
        }
    }
}

