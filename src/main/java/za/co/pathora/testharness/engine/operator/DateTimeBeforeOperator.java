package za.co.pathora.testharness.engine.operator;

import za.co.pathora.testharness.exception.HarnessAssertionException;
import za.co.pathora.testharness.model.AssertionOperator;
import za.co.pathora.testharness.util.AssertionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class DateTimeBeforeOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDateTime actualDt = parseDateTime(String.valueOf(normalizedActual), path);
        LocalDateTime expectedDt = parseDateTime(String.valueOf(expected), path);

        if (!actualDt.isBefore(expectedDt)) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATETIME_BEFORE,
                    path,
                    expected,
                    actual,
                    "DATETIME_BEFORE failed at " + path +
                            ". Expected before: " + expectedDt +
                            ", Actual: " + actualDt);
        }
    }

    static LocalDateTime parseDateTime(String value, String path) {
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Cannot parse datetime at " + path + ": " + value +
                            ". Expected ISO datetime format (yyyy-MM-dd'T'HH:mm:ss)");
        }
    }
}
