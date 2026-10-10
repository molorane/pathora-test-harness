package io.github.molorane.pathora.testharness.util;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionResolver;

import java.time.*;
import java.time.format.DateTimeParseException;

/**
 * Utility functions for parsing and validating dates, times, timestamps, and calendar components from heterogeneous JSON inputs.
 */
public class DateUtils {

    /**
     * Parses a string into a {@link LocalDate} supporting ISO format (e.g. {@code yyyy-MM-dd}).
     *
     * @param value the string representation to parse
     * @param path  the JSONPath context for error messages
     * @return the parsed {@link LocalDate}
     * @throws IllegalArgumentException if value is null or cannot be parsed as a date
     */
    public static LocalDate parseDate(String value, String path) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot parse date at " + path + ": null");
        }
        String resolved = ExpressionResolver.resolveToString(value).trim();
        LocalDate parsed = tryParseDateString(resolved);
        if (parsed != null) {
            return parsed;
        }
        throw new IllegalArgumentException(
            "Cannot parse date at " + path + ": " + value +
                ". Expected ISO date format (yyyy-MM-dd)");
    }

    /**
     * Parses a string into a {@link LocalDateTime} expecting ISO datetime format.
     *
     * @param value the string value to parse
     * @param path  the JSONPath context for error reporting
     * @return the parsed {@link LocalDateTime}
     * @throws IllegalArgumentException if value cannot be parsed
     */
    public static LocalDateTime parseDateTime(String value, String path) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot parse datetime at " + path + ": null");
        }
        String resolved = ExpressionResolver.resolveToString(value).trim();
        try {
            return LocalDateTime.parse(resolved);
        } catch (DateTimeParseException ignored) {
        }
        try {
            return OffsetDateTime.parse(resolved).toLocalDateTime();
        } catch (DateTimeParseException ignored) {
        }
        try {
            return ZonedDateTime.parse(resolved).toLocalDateTime();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                "Cannot parse datetime at " + path + ": " + value +
                    ". Expected ISO datetime format (yyyy-MM-dd'T'HH:mm:ss)");
        }
    }

    /**
     * Parses an object (LocalDate, LocalDateTime, or ISO string) into a {@link LocalDate}.
     *
     * @param value the object value
     * @param path  the JSONPath context for error reporting
     * @return the parsed {@link LocalDate}
     * @throws IllegalArgumentException if parsing fails or value is null
     */
    public static LocalDate parseDateOrDateTime(Object value, String path) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot parse date/datetime at " + path + ": null");
        }
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        if (value instanceof LocalDateTime localDateTime) {
            return localDateTime.toLocalDate();
        }
        String str = ExpressionResolver.resolveToString(String.valueOf(value)).trim();
        LocalDate parsed = tryParseDateString(str);
        if (parsed != null) {
            return parsed;
        }
        throw new IllegalArgumentException(
            "Cannot parse date/datetime at " + path + ": " + value +
                ". Expected ISO date or datetime format (e.g. yyyy-MM-dd or yyyy-MM-dd'T'HH:mm:ss)");
    }

    private static LocalDate tryParseDateString(String str) {
        try {
            return LocalDate.parse(str);
        } catch (DateTimeParseException ignored) {
        }
        try {
            return LocalDateTime.parse(str).toLocalDate();
        } catch (DateTimeParseException ignored) {
        }
        try {
            return OffsetDateTime.parse(str).toLocalDate();
        } catch (DateTimeParseException ignored) {
        }
        try {
            return ZonedDateTime.parse(str).toLocalDate();
        } catch (DateTimeParseException ignored) {
        }
        return null;
    }

    /**
     * Parses a string into a {@link LocalTime} in ISO time format.
     *
     * @param value the string time
     * @param path  the JSONPath context
     * @return the parsed {@link LocalTime}
     * @throws IllegalArgumentException if parsing fails
     */
    public static LocalTime parseTime(String value, String path) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot parse time at " + path + ": null");
        }
        LocalTime parsed = tryParseTimeString(value.trim());
        if (parsed != null) {
            return parsed;
        }
        throw new IllegalArgumentException(
            "Cannot parse time at " + path + ": " + value +
                ". Expected ISO time format (e.g. HH:mm or HH:mm:ss)");
    }

    /**
     * Parses an object (LocalTime, LocalDateTime, or ISO string) into a {@link LocalTime}.
     *
     * @param value the object time
     * @param path  the JSONPath context
     * @return the parsed {@link LocalTime}
     * @throws IllegalArgumentException if parsing fails
     */
    public static LocalTime parseTimeOrDateTime(Object value, String path) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot parse time at " + path + ": null");
        }
        if (value instanceof LocalTime localTime) {
            return localTime;
        }
        if (value instanceof LocalDateTime localDateTime) {
            return localDateTime.toLocalTime();
        }
        String str = String.valueOf(value).trim();
        LocalTime parsed = tryParseTimeString(str);
        if (parsed != null) {
            return parsed;
        }
        throw new IllegalArgumentException(
            "Cannot parse time at " + path + ": " + value +
                ". Expected ISO time or datetime format (e.g. HH:mm, HH:mm:ss, or yyyy-MM-dd'T'HH:mm:ss)");
    }

    private static LocalTime tryParseTimeString(String str) {
        try {
            return LocalTime.parse(str);
        } catch (DateTimeParseException ignored) {
        }
        try {
            return LocalDateTime.parse(str).toLocalTime();
        } catch (DateTimeParseException ignored) {
        }
        try {
            return OffsetDateTime.parse(str).toLocalTime();
        } catch (DateTimeParseException ignored) {
        }
        try {
            return ZonedDateTime.parse(str).toLocalTime();
        } catch (DateTimeParseException ignored) {
        }
        return null;
    }

    /**
     * Parses a year integer from number or string input.
     *
     * @param expected the raw input
     * @param path     the JSONPath context
     * @return the year integer
     */
    public static int parseYear(Object expected, String path) {
        if (expected == null) {
            throw new IllegalArgumentException("Expected year value at " + path + " cannot be null");
        }
        if (expected instanceof Number num) {
            return num.intValue();
        }
        try {
            return Integer.parseInt(expected.toString().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid year value at " + path + ": " + expected +
                    ". Expected a valid integer year (e.g. 2025)");
        }
    }

    /**
     * Parses a month value from number (1-12) or month name (e.g. "JUNE", "Jun").
     *
     * @param expected the raw input
     * @param path     the JSONPath context
     * @return the month number (1-12)
     */
    public static int parseMonth(Object expected, String path) {
        if (expected == null) {
            throw new IllegalArgumentException("Expected month value at " + path + " cannot be null");
        }
        if (expected instanceof Number num) {
            return validateMonthNumber(num.intValue(), path);
        }
        String str = expected.toString().trim();
        try {
            return validateMonthNumber(Integer.parseInt(str), path);
        } catch (NumberFormatException ignored) {
        }
        Integer monthVal = matchMonthName(str);
        if (monthVal != null) {
            return monthVal;
        }
        throw new IllegalArgumentException(
            "Invalid month value at " + path + ": " + expected +
                ". Expected month number (1-12) or month name (e.g. JUNE, June, JUN)");
    }

    private static int validateMonthNumber(int month, String path) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException(
                "Invalid month number at " + path + ": " + month + ". Must be between 1 and 12");
        }
        return month;
    }

    private static Integer matchMonthName(String str) {
        String upper = str.toUpperCase();
        if ("SEPT".equals(upper)) {
            return Month.SEPTEMBER.getValue();
        }
        for (Month month : Month.values()) {
            if (month.name().equals(upper) || month.name().substring(0, 3).equals(upper)) {
                return month.getValue();
            }
        }
        return null;
    }

    /**
     * Parses a day of month value (1-31).
     *
     * @param expected the day value
     * @param path     the JSONPath context
     * @return the day number (1-31)
     */
    public static int parseDayOfMonth(Object expected, String path) {
        if (expected == null) {
            throw new IllegalArgumentException("Expected day value at " + path + " cannot be null");
        }
        if (expected instanceof Number num) {
            return validateDayNumber(num.intValue(), path);
        }
        try {
            return validateDayNumber(Integer.parseInt(expected.toString().trim()), path);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid day value at " + path + ": " + expected +
                    ". Expected a valid day of month (1-31)");
        }
    }

    private static int validateDayNumber(int day, String path) {
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException(
                "Invalid day of month at " + path + ": " + day + ". Must be between 1 and 31");
        }
        return day;
    }

    /**
     * Parses a day of week from number (1=Monday ... 7=Sunday) or name (e.g. "MONDAY", "Mon").
     *
     * @param expected the day of week representation
     * @param path     the JSONPath context
     * @return the parsed {@link DayOfWeek}
     */
    public static DayOfWeek parseDayOfWeek(Object expected, String path) {
        if (expected == null) {
            throw new IllegalArgumentException("Expected day of week value at " + path + " cannot be null");
        }
        if (expected instanceof Number num) {
            return validateDayOfWeekNumber(num.intValue(), path);
        }
        String str = expected.toString().trim();
        try {
            return validateDayOfWeekNumber(Integer.parseInt(str), path);
        } catch (NumberFormatException ignored) {
        }
        DayOfWeek dow = matchDayOfWeekName(str);
        if (dow != null) {
            return dow;
        }
        throw new IllegalArgumentException(
            "Invalid day of week value at " + path + ": " + expected +
                ". Expected day of week number (1-7) or name (e.g. MONDAY, Monday, MON)");
    }

    private static DayOfWeek validateDayOfWeekNumber(int dow, String path) {
        if (dow < 1 || dow > 7) {
            throw new IllegalArgumentException(
                "Invalid day of week number at " + path + ": " + dow +
                    ". Must be between 1 (Monday) and 7 (Sunday)");
        }
        return DayOfWeek.of(dow);
    }

    private static DayOfWeek matchDayOfWeekName(String str) {
        String upper = str.toUpperCase();
        for (DayOfWeek dow : DayOfWeek.values()) {
            if (dow.name().equals(upper) || dow.name().substring(0, 3).equals(upper)) {
                return dow;
            }
        }
        return null;
    }

    /**
     * Parses an hour value (0-23).
     *
     * @param expected the hour value
     * @param path     the JSONPath context
     * @return the hour integer (0-23)
     */
    public static int parseHour(Object expected, String path) {
        if (expected == null) {
            throw new IllegalArgumentException("Expected hour value at " + path + " cannot be null");
        }
        if (expected instanceof Number num) {
            return validateHourNumber(num.intValue(), path);
        }
        try {
            return validateHourNumber(Integer.parseInt(expected.toString().trim()), path);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid hour value at " + path + ": " + expected +
                    ". Expected a valid hour (0-23)");
        }
    }

    private static int validateHourNumber(int hour, String path) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException(
                "Invalid hour at " + path + ": " + hour + ". Must be between 0 and 23");
        }
        return hour;
    }

    /**
     * Parses a minute or second component (0-59).
     *
     * @param expected the value
     * @param name     the component name ("minute" or "second")
     * @param path     the JSONPath context
     * @return the integer value (0-59)
     */
    public static int parseMinuteOrSecond(Object expected, String name, String path) {
        if (expected == null) {
            throw new IllegalArgumentException("Expected " + name + " value at " + path + " cannot be null");
        }
        if (expected instanceof Number num) {
            return validateMinuteOrSecondNumber(num.intValue(), name, path);
        }
        try {
            return validateMinuteOrSecondNumber(Integer.parseInt(expected.toString().trim()), name, path);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid " + name + " value at " + path + ": " + expected +
                    ". Expected a valid " + name + " (0-59)");
        }
    }

    private static int validateMinuteOrSecondNumber(int val, String name, String path) {
        if (val < 0 || val > 59) {
            throw new IllegalArgumentException(
                "Invalid " + name + " at " + path + ": " + val + ". Must be between 0 and 59");
        }
        return val;
    }
}


