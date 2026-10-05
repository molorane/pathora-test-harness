package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

/**
 * Operator: {@code ENDS_WITH}
 *
 * <p>Validates that the extracted string ends with the specified expected suffix.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.filename",
 *   "Operator": "ENDS_WITH",
 *   "Value": ".pdf"
 * }
 * }</pre>
 *
 * @see AssertionOperator#ENDS_WITH
 */
public class EndsWithEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.ENDS_WITH;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        String actualStr = String.valueOf(normalizedActual);
        String suffix = String.valueOf(expected);

        if (!actualStr.endsWith(suffix)) {
            throw new HarnessAssertionException(
                    AssertionOperator.ENDS_WITH,
                    path,
                    suffix,
                    actualStr,
                    "ENDS_WITH failed at " + path +
                            ". Expected to end with: " + suffix +
                            ", Actual: " + actualStr);
        }
    }
}
