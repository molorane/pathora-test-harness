package io.github.molorane.pathora.testharness.engine.operator.string;

import org.apache.commons.lang3.StringUtils;
import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code IS_STRING_ALPHA_SPACE}
 *
 * <p>Validates that the extracted string contains only letters and spaces.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.fullName",
 *   "Operator": "IS_STRING_ALPHA_SPACE"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_STRING_ALPHA_SPACE
 */
public class IsStringAlphaSpaceEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_STRING_ALPHA_SPACE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);

        if (!StringUtils.isAlphaSpace(actualStr)) {
            throw new HarnessAssertionException(
                    AssertionOperator.IS_STRING_ALPHA_SPACE,
                    path,
                    "alpha space string",
                    actual,
                    "IS_STRING_ALPHA_SPACE failed at " + path +
                            ". Expected letters and spaces only, Actual: " + actualStr);
        }
    }
}

