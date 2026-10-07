package io.github.molorane.pathora.testharness.engine.operator.scalar;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.math.BigDecimal;

/**
 * Operator: {@code IS_DECIMAL}
 *
 * <p>Validates that the extracted value is a decimal number (containing a fractional non-integer component).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.rate",
 *   "Operator": "IS_DECIMAL"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_DECIMAL
 */
public class IsDecimalEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_DECIMAL;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalized = AssertionUtils.normalizeResult(actual, path);

        boolean isDecimal = false;
        if (normalized instanceof Double || normalized instanceof Float || normalized instanceof BigDecimal) {
            isDecimal = true;
        } else if (normalized instanceof String str) {
            isDecimal = str.trim().matches("^[+-]?\\d+\\.\\d+$");
        }

        if (!isDecimal) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_DECIMAL,
                path,
                "a decimal number",
                actual,
                "IS_DECIMAL failed at " + path +
                    ". Expected decimal value but was: " + normalized);
        }
    }
}

