package io.github.molorane.pathora.testharness.engine.operator.scalar;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

/**
 * Operator: {@code IS_TRUE}
 *
 * <p>Validates that the actual extracted value is a boolean {@code true} (or string {@code "true"}).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.active",
 *   "Operator": "IS_TRUE"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_TRUE
 */
public class IsTrueEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_TRUE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalized = AssertionUtils.normalizeResult(actual, path);

        boolean isTrue = Boolean.TRUE.equals(normalized)
            || normalized instanceof String str && "true".equalsIgnoreCase(str.trim());

        if (!isTrue) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_TRUE,
                path,
                true,
                actual,
                "IS_TRUE failed at " + path +
                    ". Expected true but was: " + normalized);
        }
    }
}

