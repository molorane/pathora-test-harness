package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.apache.commons.lang3.StringUtils;

/**
 * Operator: {@code IS_STRING_MIXED_CASE}
 *
 * <p>Validates that the extracted string contains both uppercase and lowercase characters.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.password",
 *   "Operator": "IS_STRING_MIXED_CASE"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_STRING_MIXED_CASE
 */
public class IsStringMixedCaseEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_STRING_MIXED_CASE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);

        if (!StringUtils.isMixedCase(actualStr)) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_STRING_MIXED_CASE,
                path,
                "mixed case string",
                actual,
                "IS_STRING_MIXED_CASE failed at " + path +
                    ". Expected mixed case, Actual: " + actualStr);
        }
    }
}

