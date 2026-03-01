package za.co.pathora.testharness.engine.operator;

import za.co.pathora.testharness.exception.HarnessAssertionException;
import za.co.pathora.testharness.model.AssertionOperator;
import za.co.pathora.testharness.util.AssertionUtils;

import java.time.LocalDate;

public class DateAfterOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDate actualDate = DateBeforeOperator.parseDate(String.valueOf(normalizedActual), path);
        LocalDate expectedDate = DateBeforeOperator.parseDate(String.valueOf(expected), path);

        if (!actualDate.isAfter(expectedDate)) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATE_AFTER,
                    path,
                    expected,
                    actual,
                    "DATE_AFTER failed at " + path +
                            ". Expected after: " + expectedDate +
                            ", Actual: " + actualDate);
        }
    }
}
