package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code STRING_STARTS_WITH_IGNORE_CASE}
 *
 * <p>Validates that the extracted string starts with the specified prefix ignoring case differences.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.sku",
 *   "Operator": "STRING_STARTS_WITH_IGNORE_CASE",
 *   "Value": "prod-"
 * }
 * }</pre>
 *
 * @see AssertionOperator#STRING_STARTS_WITH_IGNORE_CASE
 */
public class StringStartsWithIgnoreCaseEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.STRING_STARTS_WITH_IGNORE_CASE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);
        String prefix = expected == null ? null : String.valueOf(expected);

        if (!StringHelper.startsWithIgnoreCase(actualStr, prefix)) {
            throw new HarnessAssertionException(
                    AssertionOperator.STRING_STARTS_WITH_IGNORE_CASE,
                    path,
                    expected,
                    actual,
                    "STRING_STARTS_WITH_IGNORE_CASE failed at " + path +
                            ". Expected to start with (ignore case): " + prefix +
                            ", Actual: " + actualStr);
        }
    }
}

