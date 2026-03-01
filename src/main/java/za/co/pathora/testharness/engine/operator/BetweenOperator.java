package za.co.pathora.testharness.engine.operator;

import za.co.pathora.testharness.exception.HarnessAssertionException;
import za.co.pathora.testharness.model.AssertionOperator;
import za.co.pathora.testharness.util.AssertionUtils;

import java.util.Map;

public class BetweenOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);

        Map<String, Object> range = AssertionUtils.toMap(expected);

        Object minObj = range.get("min");
        Object maxObj = range.get("max");

        if (minObj == null || maxObj == null) {
            throw new IllegalArgumentException(
                    "BETWEEN operator requires 'min' and 'max' in Value at " + path);
        }

        Object[] normalizedMin = AssertionUtils.normalizeTypes(normalizedActual, minObj);
        Object[] normalizedMax = AssertionUtils.normalizeTypes(normalizedActual, maxObj);

        double actualValue = ((Number) normalizedMin[0]).doubleValue();
        double min = ((Number) normalizedMin[1]).doubleValue();
        double max = ((Number) normalizedMax[1]).doubleValue();

        if (actualValue < min || actualValue > max) {
            throw new HarnessAssertionException(
                    AssertionOperator.BETWEEN,
                    path,
                    "between " + min + " and " + max,
                    actualValue,
                    "BETWEEN failed at " + path +
                            ". Expected between " + min + " and " + max +
                            ", Actual: " + actualValue);
        }
    }
}
