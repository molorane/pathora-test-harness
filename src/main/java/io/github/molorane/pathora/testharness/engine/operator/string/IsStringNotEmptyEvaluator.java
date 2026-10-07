package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.apache.commons.lang3.StringUtils;

/**
 * Operator: {@code IS_STRING_NOT_EMPTY}
 *
 * <p>Validates that the extracted string is not empty (length > 0).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.trackingNumber",
 *   "Operator": "IS_STRING_NOT_EMPTY"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_STRING_NOT_EMPTY
 */
public class IsStringNotEmptyEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_STRING_NOT_EMPTY;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);

        if (!StringUtils.isNotEmpty(actualStr)) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_STRING_NOT_EMPTY,
                path,
                "not empty string",
                actual,
                "IS_STRING_NOT_EMPTY failed at " + path +
                    ". Expected not empty string, Actual: " + actualStr);
        }
    }
}

