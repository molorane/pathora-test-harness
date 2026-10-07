package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code STRING_ENDS_WITH_ANY}
 *
 * <p>Validates that the extracted string ends with at least one of the specified suffixes.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.documentName",
 *   "Operator": "STRING_ENDS_WITH_ANY",
 *   "Value": [".pdf", ".docx", ".xlsx"]
 * }
 * }</pre>
 *
 * @see AssertionOperator#STRING_ENDS_WITH_ANY
 */
public class StringEndsWithAnyEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.STRING_ENDS_WITH_ANY;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);
        CharSequence[] suffixes = StringHelper.toCharSequenceArray(expected);

        if (!StringHelper.endsWithAny(actualStr, suffixes)) {
            throw new HarnessAssertionException(
                AssertionOperator.STRING_ENDS_WITH_ANY,
                path,
                expected,
                actual,
                "STRING_ENDS_WITH_ANY failed at " + path +
                    ". Expected to end with any of: " + expected +
                    ", Actual: " + actualStr);
        }
    }
}

