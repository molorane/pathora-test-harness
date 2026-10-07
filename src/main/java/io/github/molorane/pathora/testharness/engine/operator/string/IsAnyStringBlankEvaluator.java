package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Operator: {@code IS_ANY_STRING_BLANK}
 *
 * <p>Validates that at least one string in the extracted array or single value is blank, empty, or whitespace-only.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.optionalComments",
 *   "Operator": "IS_ANY_STRING_BLANK"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_ANY_STRING_BLANK
 */
public class IsAnyStringBlankEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_ANY_STRING_BLANK;
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

        if (!StringUtils.isAnyBlank(charSequences)) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_ANY_STRING_BLANK,
                path,
                "at least one blank string",
                actual,
                "IS_ANY_STRING_BLANK failed at " + path +
                    ". Expected at least one blank string, Actual: " + actual);
        }
    }
}

