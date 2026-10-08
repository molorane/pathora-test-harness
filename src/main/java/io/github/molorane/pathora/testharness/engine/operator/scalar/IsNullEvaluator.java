package io.github.molorane.pathora.testharness.engine.operator.scalar;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import java.util.List;

/**
 * Operator: {@code IS_NULL}
 *
 * <p>Validates that the actual extracted value is {@code null}.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.deletedAt",
 *   "Operator": "IS_NULL"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_NULL
 */
public class IsNullEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_NULL;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object value = actual;
        if (value instanceof List<?> list) {
            if (list.isEmpty()) {
                value = null;
            } else if (list.size() == 1) {
                value = list.get(0);
            }
        }

        if (value != null) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_NULL,
                path,
                "null",
                actual,
                "IS_NULL failed at " + path +
                    ". Expected null but was: " + actual);
        }
    }
}

