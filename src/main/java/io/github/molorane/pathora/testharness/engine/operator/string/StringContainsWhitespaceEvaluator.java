package io.github.molorane.pathora.testharness.engine.operator.string;

import org.apache.commons.lang3.StringUtils;
import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code STRING_CONTAINS_WHITESPACE}
 *
 * <p>Validates that the extracted string contains at least one whitespace character.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.formattedAddress",
 *   "Operator": "STRING_CONTAINS_WHITESPACE"
 * }
 * }</pre>
 *
 * @see AssertionOperator#STRING_CONTAINS_WHITESPACE
 */
public class StringContainsWhitespaceEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.STRING_CONTAINS_WHITESPACE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);

        if (!StringUtils.containsWhitespace(actualStr)) {
            throw new HarnessAssertionException(
                    AssertionOperator.STRING_CONTAINS_WHITESPACE,
                    path,
                    "contains whitespace",
                    actual,
                    "STRING_CONTAINS_WHITESPACE failed at " + path +
                            ". Expected to contain whitespace, Actual: " + actualStr);
        }
    }
}

