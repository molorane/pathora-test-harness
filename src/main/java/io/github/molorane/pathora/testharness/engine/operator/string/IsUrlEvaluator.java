package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Operator: {@code IS_URL}
 *
 * <p>Validates that the extracted string conforms to a valid URI/URL format with scheme and host.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.callbackUrl",
 *   "Operator": "IS_URL"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_URL
 */
public class IsUrlEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_URL;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);

        boolean validUrl = false;
        if (actualStr != null && !actualStr.trim().isEmpty()) {
            try {
                URI uri = new URI(actualStr.trim());
                if (uri.getScheme() != null && uri.getHost() != null) {
                    validUrl = true;
                }
            } catch (URISyntaxException ignored) {
            }
        }

        if (!validUrl) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_URL,
                path,
                "valid URL",
                actual,
                "IS_URL failed at " + path +
                    ". Expected valid URL format, Actual: " + actualStr);
        }
    }
}

