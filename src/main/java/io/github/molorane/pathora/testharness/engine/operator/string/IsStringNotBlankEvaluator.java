package io.github.molorane.pathora.testharness.engine.operator.string;

import org.apache.commons.lang3.StringUtils;
import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code IS_STRING_NOT_BLANK}
 *
 * <p>Validates that the extracted string contains at least one non-whitespace character.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.referenceId",
 *   "Operator": "IS_STRING_NOT_BLANK"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_STRING_NOT_BLANK
 */
public class IsStringNotBlankEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_STRING_NOT_BLANK;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);

        if (!StringUtils.isNotBlank(actualStr)) {
            throw new HarnessAssertionException(
                    AssertionOperator.IS_STRING_NOT_BLANK,
                    path,
                    "not blank string",
                    actual,
                    "IS_STRING_NOT_BLANK failed at " + path +
                            ". Expected not blank string, Actual: " + actualStr);
        }
    }
}

