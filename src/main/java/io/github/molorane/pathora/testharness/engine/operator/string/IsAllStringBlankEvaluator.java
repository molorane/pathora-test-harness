package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.apache.commons.lang3.StringUtils;

/**
 * Operator: {@code IS_ALL_STRING_BLANK}
 *
 * <p>Validates that all strings in an extracted array or sequence are blank, empty, or whitespace-only.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.notes",
 *   "Operator": "IS_ALL_STRING_BLANK"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_ALL_STRING_BLANK
 */
public class IsAllStringBlankEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_ALL_STRING_BLANK;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        CharSequence[] values = StringHelper.toCharSequenceArray(actual);

        if (!StringUtils.isAllBlank(values)) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_ALL_STRING_BLANK,
                path,
                "all blank strings",
                actual,
                "IS_ALL_STRING_BLANK failed at " + path +
                    ". Expected all strings to be blank, Actual: " + actual);
        }
    }
}

