package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Operator: {@code IS_STRING_NONE_EMPTY}
 *
 * <p>Validates that none of the strings in the extracted array or sequence are empty (all have length > 0).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.lineItemCodes",
 *   "Operator": "IS_STRING_NONE_EMPTY"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_STRING_NONE_EMPTY
 */
public class IsStringNoneEmptyEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_STRING_NONE_EMPTY;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        CharSequence[] charSequences;
        if (actual instanceof List<?>) {
            charSequences = StringHelper.toCharSequenceArray(actual);
        } else {
            String actualStr = StringHelper.toSingleString(actual, path);
            charSequences = new CharSequence[]{actualStr};
        }

        if (!StringUtils.isNoneEmpty(charSequences)) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_STRING_NONE_EMPTY,
                path,
                "non-empty string(s)",
                actual,
                "IS_STRING_NONE_EMPTY failed at " + path +
                    ". Expected all strings to be non-empty, Actual: " + actual);
        }
    }
}

