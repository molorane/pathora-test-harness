package io.github.molorane.pathora.testharness.engine.operator.duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * Shared utility functions for parsing temporal units and evaluating elapsed duration between timestamps.
 */
public final class DurationHelper {

    private DurationHelper() {
    }

    /**
     * Calculates the duration between two date/datetime strings in the specified temporal unit.
     *
     * @param startStr the start date/datetime string
     * @param endStr   the end date/datetime string
     * @param unit     the ChronoUnit (e.g. DAYS, HOURS)
     * @param path     the JSONPath context
     * @return the numeric duration (end - start) in terms of the given unit
     */
    public static long calculateDuration(String startStr, String endStr, ChronoUnit unit, String path) {
        try {
            LocalDateTime start = parseDateTime(startStr, path);
            LocalDateTime end = parseDateTime(endStr, path);
            return unit.between(start, end);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Parses a date or datetime string into a {@link LocalDateTime} (dates default to start of day).
     *
     * @param value the string representation
     * @param path  the JSONPath context
     * @return the parsed {@link LocalDateTime}
     */
    public static LocalDateTime parseDateTime(String value, String path) {
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException ignored) {
        }
        try {
            return OffsetDateTime.parse(value).toLocalDateTime();
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

    /**
     * Parses a string into a {@link ChronoUnit}.
     *
     * @param unit the unit name (e.g. "DAYS", "HOURS")
     * @return the corresponding {@link ChronoUnit}
     */
    public static ChronoUnit parseUnit(String unit) {
        try {
            return ChronoUnit.valueOf(unit.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Invalid duration unit: " + unit +
                    ". Supported: DAYS, HOURS, MINUTES, SECONDS, MONTHS, YEARS");
        }
    }

    /**
     * Converts a numeric or string value to a long primitive.
     *
     * @param value the object value
     * @return the long value
     */
    public static long toLong(Object value) {
        if (value instanceof Number num)
            return num.longValue();
        return Long.parseLong(String.valueOf(value));
    }
}


