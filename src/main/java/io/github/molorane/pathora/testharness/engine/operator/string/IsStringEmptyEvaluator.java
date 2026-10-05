package io.github.molorane.pathora.testharness.engine.operator.string;

import org.apache.commons.lang3.StringUtils;
import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Operator: {@code IS_STRING_EMPTY}
 *
 * <p>Validates that the extracted string is completely empty ({@code ""}).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.errorMessage",
 *   "Operator": "IS_STRING_EMPTY"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_STRING_EMPTY
 */
public class IsStringEmptyEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_STRING_EMPTY;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);

        if (!StringUtils.isEmpty(actualStr)) {
            throw new HarnessAssertionException(
                    AssertionOperator.IS_STRING_EMPTY,
                    path,
                    "empty string",
                    actual,
                    "IS_STRING_EMPTY failed at " + path +
                            ". Expected empty string, Actual: " + actualStr);
        }
    }
}

