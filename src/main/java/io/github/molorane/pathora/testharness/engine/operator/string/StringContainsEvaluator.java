package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code STRING_CONTAINS}
 *
 * <p>Validates that the extracted string contains the specified search substring.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.description",
 *   "Operator": "STRING_CONTAINS",
 *   "Value": "SUCCESS"
 * }
 * }</pre>
 *
 * @see AssertionOperator#STRING_CONTAINS
 */
public class StringContainsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.STRING_CONTAINS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);
        String searchStr = expected == null ? null : String.valueOf(expected);

        if (!StringHelper.contains(actualStr, searchStr)) {
            throw new HarnessAssertionException(
                    AssertionOperator.STRING_CONTAINS,
                    path,
                    expected,
                    actual,
                    "STRING_CONTAINS failed at " + path +
                            ". Expected to contain: " + expected +
                            ", Actual: " + actualStr);
        }
    }
}

