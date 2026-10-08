package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import java.util.regex.Pattern;

/**
 * Operator: {@code IS_EMAIL}
 *
 * <p>Validates that the extracted string conforms to a standard email address format.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.emailAddress",
 *   "Operator": "IS_EMAIL"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_EMAIL
 */
public class IsEmailEvaluator implements AssertionEvaluator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_EMAIL;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);

        if (actualStr == null || !EMAIL_PATTERN.matcher(actualStr.trim()).matches()) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_EMAIL,
                path,
                "valid email address",
                actual,
                "IS_EMAIL failed at " + path +
                    ". Expected valid email format, Actual: " + actualStr);
        }
    }
}

