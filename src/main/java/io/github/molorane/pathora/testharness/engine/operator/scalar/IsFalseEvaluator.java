package io.github.molorane.pathora.testharness.engine.operator.scalar;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

/**
 * Operator: {@code IS_FALSE}
 *
 * <p>Validates that the extracted value is a boolean {@code false} (or string {@code "false"}).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.isFlagged",
 *   "Operator": "IS_FALSE"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_FALSE
 */
public class IsFalseEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_FALSE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalized = AssertionUtils.normalizeResult(actual, path);

        boolean isFalse = Boolean.FALSE.equals(normalized)
                || normalized instanceof String str && "false".equalsIgnoreCase(str.trim());

        if (!isFalse) {
            throw new HarnessAssertionException(
                    AssertionOperator.IS_FALSE,
                    path,
                    false,
                    actual,
                    "IS_FALSE failed at " + path +
                            ". Expected false but was: " + normalized);
        }
    }
}

