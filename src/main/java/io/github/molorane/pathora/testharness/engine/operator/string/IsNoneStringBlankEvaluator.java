package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.apache.commons.lang3.StringUtils;

/**
 * Operator: {@code IS_NONE_STRING_BLANK}
 *
 * <p>Validates that none of the strings in the extracted array or sequence are blank (all contain non-whitespace text).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.requiredFields",
 *   "Operator": "IS_NONE_STRING_BLANK"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_NONE_STRING_BLANK
 */
public class IsNoneStringBlankEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_NONE_STRING_BLANK;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        CharSequence[] values = StringHelper.toCharSequenceArray(actual);

        if (!StringUtils.isNoneBlank(values)) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_NONE_STRING_BLANK,
                path,
                "none blank string(s)",
                actual,
                "IS_NONE_STRING_BLANK failed at " + path +
                    ". Expected no blank strings, Actual: " + actual);
        }
    }
}

