package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateBeforeOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDate actualDate = parseDate(String.valueOf(normalizedActual), path);
        LocalDate expectedDate = parseDate(String.valueOf(expected), path);

        if (!actualDate.isBefore(expectedDate)) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATE_BEFORE,
                    path,
                    expected,
                    actual,
                    "DATE_BEFORE failed at " + path +
                            ". Expected before: " + expectedDate +
                            ", Actual: " + actualDate);
        }
    }

    static LocalDate parseDate(String value, String path) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Cannot parse date at " + path + ": " + value +
                            ". Expected ISO date format (yyyy-MM-dd)");
        }
    }
}
