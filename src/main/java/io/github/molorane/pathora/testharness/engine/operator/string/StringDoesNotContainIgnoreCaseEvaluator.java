package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code STRING_DOES_NOT_CONTAIN_IGNORE_CASE}
 *
 * <p>Validates that the extracted string does not contain the specified substring ignoring case.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.notes",
 *   "Operator": "STRING_DOES_NOT_CONTAIN_IGNORE_CASE",
 *   "Value": "denied"
 * }
 * }</pre>
 *
 * @see AssertionOperator#STRING_DOES_NOT_CONTAIN_IGNORE_CASE
 */
public class StringDoesNotContainIgnoreCaseEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.STRING_DOES_NOT_CONTAIN_IGNORE_CASE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);
        String seq = expected == null ? null : String.valueOf(expected);

        if (StringHelper.containsIgnoreCase(actualStr, seq)) {
            throw new HarnessAssertionException(
                AssertionOperator.STRING_DOES_NOT_CONTAIN_IGNORE_CASE,
                path,
                expected,
                actual,
                "STRING_DOES_NOT_CONTAIN_IGNORE_CASE failed at " + path +
                    ". Expected not to contain (ignore case): " + seq +
                    ", Actual: " + actualStr);
        }
    }
}

