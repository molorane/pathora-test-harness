package io.github.molorane.pathora.testharness.engine.operator.string;

import org.apache.commons.lang3.StringUtils;
import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code IS_STRING_UPPER_CASE}
 *
 * <p>Validates that all alphabetic characters in the extracted string are uppercase.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.currencyCode",
 *   "Operator": "IS_STRING_UPPER_CASE"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_STRING_UPPER_CASE
 */
public class IsStringUpperCaseEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_STRING_UPPER_CASE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);

        if (!StringUtils.isAllUpperCase(actualStr)) {
            throw new HarnessAssertionException(
                    AssertionOperator.IS_STRING_UPPER_CASE,
                    path,
                    "uppercase string",
                    actual,
                    "IS_STRING_UPPER_CASE failed at " + path +
                            ". Expected all uppercase characters, Actual: " + actualStr);
        }
    }
}

