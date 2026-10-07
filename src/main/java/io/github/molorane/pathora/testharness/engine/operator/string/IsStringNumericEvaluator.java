package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.apache.commons.lang3.StringUtils;

/**
 * Operator: {@code IS_STRING_NUMERIC}
 *
 * <p>Validates that the extracted string contains only numeric digits.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.pin",
 *   "Operator": "IS_STRING_NUMERIC"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_STRING_NUMERIC
 */
public class IsStringNumericEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_STRING_NUMERIC;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);

        if (!StringUtils.isNumeric(actualStr)) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_STRING_NUMERIC,
                path,
                "numeric string (digits only)",
                actual,
                "IS_STRING_NUMERIC failed at " + path +
                    ". Expected digits only, Actual: " + actualStr);
        }
    }
}

