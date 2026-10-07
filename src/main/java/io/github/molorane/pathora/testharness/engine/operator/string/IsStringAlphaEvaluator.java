package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.apache.commons.lang3.StringUtils;

/**
 * Operator: {@code IS_STRING_ALPHA}
 *
 * <p>Validates that the extracted string contains only Unicode alphabetic characters.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.firstName",
 *   "Operator": "IS_STRING_ALPHA"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_STRING_ALPHA
 */
public class IsStringAlphaEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_STRING_ALPHA;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);

        if (!StringUtils.isAlpha(actualStr)) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_STRING_ALPHA,
                path,
                "alpha string",
                actual,
                "IS_STRING_ALPHA failed at " + path +
                    ". Expected letters only, Actual: " + actualStr);
        }
    }
}

