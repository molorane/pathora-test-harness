package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import java.util.regex.Pattern;

/**
 * Operator: {@code IS_UUID}
 *
 * <p>Validates that the extracted string conforms to standard UUID formatting (8-4-4-4-12 hex format).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.transactionId",
 *   "Operator": "IS_UUID"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_UUID
 */
public class IsUuidEvaluator implements AssertionEvaluator {

    private static final Pattern UUID_PATTERN = Pattern.compile(
        "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_UUID;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);

        if (actualStr == null || !UUID_PATTERN.matcher(actualStr.trim()).matches()) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_UUID,
                path,
                "valid UUID string",
                actual,
                "IS_UUID failed at " + path +
                    ". Expected valid UUID format, Actual: " + actualStr);
        }
    }
}

