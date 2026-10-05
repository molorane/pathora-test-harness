package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code STRING_EQUALS_ANY_IGNORE_CASE}
 *
 * <p>Validates that the extracted string equals at least one of the specified candidate strings ignoring case.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.status",
 *   "Operator": "STRING_EQUALS_ANY_IGNORE_CASE",
 *   "Value": ["success", "approved", "ok"]
 * }
 * }</pre>
 *
 * @see AssertionOperator#STRING_EQUALS_ANY_IGNORE_CASE
 */
public class StringEqualsAnyIgnoreCaseEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.STRING_EQUALS_ANY_IGNORE_CASE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);
        CharSequence[] searchStrings = StringHelper.toCharSequenceArray(expected);

        if (!StringHelper.equalsAnyIgnoreCase(actualStr, searchStrings)) {
            throw new HarnessAssertionException(
                    AssertionOperator.STRING_EQUALS_ANY_IGNORE_CASE,
                    path,
                    expected,
                    actual,
                    "STRING_EQUALS_ANY_IGNORE_CASE failed at " + path +
                            ". Expected to equal any of (ignore case): " + expected +
                            ", Actual: " + actualStr);
        }
    }
}

