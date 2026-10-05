package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code STRING_CONTAINS_IGNORE_CASE}
 *
 * <p>Validates that the extracted string contains the search substring ignoring case differences.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.message",
 *   "Operator": "STRING_CONTAINS_IGNORE_CASE",
 *   "Value": "approved"
 * }
 * }</pre>
 *
 * @see AssertionOperator#STRING_CONTAINS_IGNORE_CASE
 */
public class StringContainsIgnoreCaseEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.STRING_CONTAINS_IGNORE_CASE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);
        String searchStr = expected == null ? null : String.valueOf(expected);

        if (!StringHelper.containsIgnoreCase(actualStr, searchStr)) {
            throw new HarnessAssertionException(
                    AssertionOperator.STRING_CONTAINS_IGNORE_CASE,
                    path,
                    expected,
                    actual,
                    "STRING_CONTAINS_IGNORE_CASE failed at " + path +
                            ". Expected to contain (ignore case): " + expected +
                            ", Actual: " + actualStr);
        }
    }
}

