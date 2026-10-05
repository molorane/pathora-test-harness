package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code STRING_EQUALS_ANY}
 *
 * <p>Validates that the extracted string exactly equals at least one of the specified target strings.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.state",
 *   "Operator": "STRING_EQUALS_ANY",
 *   "Value": ["PENDING", "PROCESSING", "APPROVED"]
 * }
 * }</pre>
 *
 * @see AssertionOperator#STRING_EQUALS_ANY
 */
public class StringEqualsAnyEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.STRING_EQUALS_ANY;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);
        CharSequence[] searchStrings = StringHelper.toCharSequenceArray(expected);

        if (!StringHelper.equalsAny(actualStr, searchStrings)) {
            throw new HarnessAssertionException(
                    AssertionOperator.STRING_EQUALS_ANY,
                    path,
                    expected,
                    actual,
                    "STRING_EQUALS_ANY failed at " + path +
                            ". Expected to equal any of: " + expected +
                            ", Actual: " + actualStr);
        }
    }
}

