package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code STRING_STARTS_WITH_ANY}
 *
 * <p>Validates that the extracted string starts with at least one of the specified prefixes.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.referenceNumber",
 *   "Operator": "STRING_STARTS_WITH_ANY",
 *   "Value": ["REF-", "INV-", "ORD-"]
 * }
 * }</pre>
 *
 * @see AssertionOperator#STRING_STARTS_WITH_ANY
 */
public class StringStartsWithAnyEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.STRING_STARTS_WITH_ANY;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);
        CharSequence[] prefixes = StringHelper.toCharSequenceArray(expected);

        if (!StringHelper.startsWithAny(actualStr, prefixes)) {
            throw new HarnessAssertionException(
                AssertionOperator.STRING_STARTS_WITH_ANY,
                path,
                expected,
                actual,
                "STRING_STARTS_WITH_ANY failed at " + path +
                    ". Expected to start with any of: " + expected +
                    ", Actual: " + actualStr);
        }
    }
}

