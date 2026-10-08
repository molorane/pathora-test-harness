package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

/**
 * Operator: {@code STARTS_WITH}
 *
 * <p>Validates that the extracted string starts with the specified prefix.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.referenceNumber",
 *   "Operator": "STARTS_WITH",
 *   "Value": "REF-"
 * }
 * }</pre>
 *
 * @see AssertionOperator#STARTS_WITH
 */
public class StartsWithEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.STARTS_WITH;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        String actualStr = String.valueOf(normalizedActual);
        String prefix = String.valueOf(expected);

        if (!actualStr.startsWith(prefix)) {
            throw new HarnessAssertionException(
                AssertionOperator.STARTS_WITH,
                path,
                prefix,
                actualStr,
                "STARTS_WITH failed at " + path +
                    ". Expected to start with: " + prefix +
                    ", Actual: " + actualStr);
        }
    }
}
