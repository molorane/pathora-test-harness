package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.apache.commons.lang3.StringUtils;

/**
 * Operator: {@code IS_STRING_LOWER_CASE}
 *
 * <p>Validates that all alphabetic characters in the extracted string are lowercase.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.username",
 *   "Operator": "IS_STRING_LOWER_CASE"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_STRING_LOWER_CASE
 */
public class IsStringLowerCaseEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_STRING_LOWER_CASE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);

        if (!StringUtils.isAllLowerCase(actualStr)) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_STRING_LOWER_CASE,
                path,
                "lowercase string",
                actual,
                "IS_STRING_LOWER_CASE failed at " + path +
                    ". Expected all lowercase characters, Actual: " + actualStr);
        }
    }
}

