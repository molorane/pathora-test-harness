package io.github.molorane.pathora.testharness.engine.operator.string;

import org.apache.commons.lang3.StringUtils;
import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code IS_STRING_ALPHA_NUMERIC}
 *
 * <p>Validates that the extracted string contains only alphanumeric characters (letters and digits).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.accountNumber",
 *   "Operator": "IS_STRING_ALPHA_NUMERIC"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_STRING_ALPHA_NUMERIC
 */
public class IsStringAlphaNumericEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_STRING_ALPHA_NUMERIC;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);

        if (!StringUtils.isAlphanumeric(actualStr)) {
            throw new HarnessAssertionException(
                    AssertionOperator.IS_STRING_ALPHA_NUMERIC,
                    path,
                    "alphanumeric string",
                    actual,
                    "IS_STRING_ALPHA_NUMERIC failed at " + path +
                            ". Expected alphanumeric characters only, Actual: " + actualStr);
        }
    }
}

