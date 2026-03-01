package za.co.pathora.testharness.engine.operator;

import za.co.pathora.testharness.exception.HarnessAssertionException;
import za.co.pathora.testharness.model.AssertionOperator;
import za.co.pathora.testharness.util.AssertionUtils;

import java.util.List;
import java.util.Objects;

public class ContainsAllOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> actualList = AssertionUtils.requireList(actual, path);
        List<?> expectedList = AssertionUtils.requireList(expected, path + " (expected)");

        for (Object exp : expectedList) {
            boolean found = false;
            for (Object act : actualList) {
                Object[] normalized = AssertionUtils.normalizeTypes(act, exp);
                if (Objects.equals(normalized[0], normalized[1])) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new HarnessAssertionException(
                        AssertionOperator.CONTAINS_ALL,
                        path,
                        expected,
                        actual,
                        "CONTAINS_ALL failed at " + path +
                                ". Missing value: " + exp +
                                ", Expected all of: " + expectedList +
                                ", Actual: " + actualList);
            }
        }
    }
}
