package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code STRING_DOES_NOT_CONTAIN}
 *
 * <p>Validates that the extracted string does not contain the specified substring.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.errorMessage",
 *   "Operator": "STRING_DOES_NOT_CONTAIN",
 *   "Value": "FATAL"
 * }
 * }</pre>
 *
 * @see AssertionOperator#STRING_DOES_NOT_CONTAIN
 */
public class StringDoesNotContainEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.STRING_DOES_NOT_CONTAIN;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);
        String seq = expected == null ? null : String.valueOf(expected);

        if (StringHelper.contains(actualStr, seq)) {
            throw new HarnessAssertionException(
                    AssertionOperator.STRING_DOES_NOT_CONTAIN,
                    path,
                    expected,
                    actual,
                    "STRING_DOES_NOT_CONTAIN failed at " + path +
                            ". Expected not to contain: " + seq +
                            ", Actual: " + actualStr);
        }
    }
}

