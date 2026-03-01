package za.co.pathora.testharness.engine.operator;

import za.co.pathora.testharness.exception.HarnessAssertionException;
import za.co.pathora.testharness.model.AssertionOperator;
import za.co.pathora.testharness.util.AssertionUtils;

public class StartsWithOperator implements OperatorAssertion {

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
