package za.co.pathora.testharness.engine.operator;

import za.co.pathora.testharness.exception.HarnessAssertionException;
import za.co.pathora.testharness.model.AssertionOperator;
import za.co.pathora.testharness.util.AssertionUtils;

public class EndsWithOperator implements OperatorAssertion {

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
