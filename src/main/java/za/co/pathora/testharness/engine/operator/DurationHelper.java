package za.co.pathora.testharness.engine.operator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * Shared utility for date/duration operators.
 */
final class DurationHelper {

    private DurationHelper() {
    }

    static long calculateDuration(String startStr, String endStr, ChronoUnit unit, String path) {
        try {
            LocalDateTime start = parseDateTime(startStr, path);
            LocalDateTime end = parseDateTime(endStr, path);
            return unit.between(start, end);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    static LocalDateTime parseDateTime(String value, String path) {
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException ignored) {
        }
        try {
            return LocalDate.parse(value).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Cannot parse date/datetime at " + path + ": " + value +
                            ". Expected ISO format (yyyy-MM-dd or yyyy-MM-dd'T'HH:mm:ss)");
        }
    }

    static ChronoUnit parseUnit(String unit) {
        try {
            return ChronoUnit.valueOf(unit.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid duration unit: " + unit +
                            ". Supported: DAYS, HOURS, MINUTES, SECONDS, MONTHS, YEARS");
        }
    }

    static long toLong(Object value) {
        if (value instanceof Number num)
            return num.longValue();
        return Long.parseLong(String.valueOf(value));
    }
}
