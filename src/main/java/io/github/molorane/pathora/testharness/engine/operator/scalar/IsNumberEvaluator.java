package io.github.molorane.pathora.testharness.engine.operator.scalar;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Operator: {@code IS_NUMBER}
 *
 * <p>Validates that the extracted value is a valid numeric type or numeric string representation.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.amount",
 *   "Operator": "IS_NUMBER"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_NUMBER
 */
public class IsNumberEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_NUMBER;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalized = AssertionUtils.normalizeResult(actual, path);

        boolean isNumber = normalized instanceof Number
            || normalized instanceof String str && NumberUtils.isCreatable(str.trim());

        if (!isNumber) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_NUMBER,
                path,
                "a number",
                actual,
                "IS_NUMBER failed at " + path +
                    ". Expected a number but was: " + normalized);
        }
    }
}

