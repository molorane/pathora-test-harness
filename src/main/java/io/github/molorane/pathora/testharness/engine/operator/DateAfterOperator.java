package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDate;

public class DateAfterOperator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATE_AFTER;
    }

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
