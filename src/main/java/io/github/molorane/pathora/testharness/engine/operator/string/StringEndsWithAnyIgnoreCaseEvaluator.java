package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code STRING_ENDS_WITH_ANY_IGNORE_CASE}
 *
 * <p>Validates that the extracted string ends with at least one of the specified suffixes ignoring case.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.imageFile",
 *   "Operator": "STRING_ENDS_WITH_ANY_IGNORE_CASE",
 *   "Value": [".png", ".jpg", ".jpeg"]
 * }
 * }</pre>
 *
 * @see AssertionOperator#STRING_ENDS_WITH_ANY_IGNORE_CASE
 */
public class StringEndsWithAnyIgnoreCaseEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.STRING_ENDS_WITH_ANY_IGNORE_CASE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);
        CharSequence[] suffixes = StringHelper.toCharSequenceArray(expected);

        boolean matched = StringHelper.endsWithAnyIgnoreCase(actualStr, suffixes);

        if (!matched) {
            throw new HarnessAssertionException(
                    AssertionOperator.STRING_ENDS_WITH_ANY_IGNORE_CASE,
                    path,
                    expected,
                    actual,
                    "STRING_ENDS_WITH_ANY_IGNORE_CASE failed at " + path +
                            ". Expected to end with any of (ignore case): " + expected +
                            ", Actual: " + actualStr);
        }
    }
}

