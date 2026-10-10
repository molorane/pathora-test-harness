package io.github.molorane.pathora.testharness.engine.expression;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for parsing and applying relative temporal offsets (e.g., {@code +30d}, {@code -25y}, {@code +2h}).
 */
public final class ExpressionOffsetUtils {

    private static final Pattern OFFSET_PATTERN = Pattern.compile(
        "([+-])\\s*(\\d+)\\s*([a-zA-Z]+)"
    );

    private ExpressionOffsetUtils() {
    }

    /**
     * Applies date unit offsets to a {@link LocalDate}.
     *
     * @param date      the base local date
     * @param offsetStr the offset string expression
     * @return the offset local date
     */
    public static LocalDate applyDateOffsets(LocalDate date, String offsetStr) {
        if (offsetStr == null || offsetStr.isBlank()) {
            return date;
        }
        Matcher matcher = OFFSET_PATTERN.matcher(offsetStr);
        LocalDate result = date;
        while (matcher.find()) {
            String sign = matcher.group(1);
            long amount = Long.parseLong(matcher.group(2));
            String unit = matcher.group(3).toLowerCase();
            long factor = "-".equals(sign) ? -amount : amount;
            result = applySingleDateOffset(result, unit, factor);
        }
        return result;
    }

    private static LocalDate applySingleDateOffset(LocalDate date, String unit, long factor) {
        return switch (unit) {
            case "d", "day", "days" -> date.plusDays(factor);
            case "w", "week", "weeks" -> date.plusWeeks(factor);
            case "m", "month", "months" -> date.plusMonths(factor);
            case "y", "year", "years" -> date.plusYears(factor);
            default -> throw new IllegalArgumentException("Unsupported date offset unit: " + unit);
        };
    }

    /**
     * Applies date and datetime unit offsets to a {@link ZonedDateTime}.
     *
     * @param zdt       the base zoned date time
     * @param offsetStr the offset string expression
     * @return the offset zoned date time
     */
    public static ZonedDateTime applyZonedDateTimeOffsets(ZonedDateTime zdt, String offsetStr) {
        if (offsetStr == null || offsetStr.isBlank()) {
            return zdt;
        }
        Matcher matcher = OFFSET_PATTERN.matcher(offsetStr);
        ZonedDateTime result = zdt;
        while (matcher.find()) {
            String sign = matcher.group(1);
            long amount = Long.parseLong(matcher.group(2));
            String unit = matcher.group(3).toLowerCase();
            long factor = "-".equals(sign) ? -amount : amount;
            result = applySingleZonedDateTimeOffset(result, unit, factor);
        }
        return result;
    }

    private static ZonedDateTime applySingleZonedDateTimeOffset(ZonedDateTime zdt, String unit, long factor) {
        return switch (unit) {
            case "d", "day", "days" -> zdt.plusDays(factor);
            case "w", "week", "weeks" -> zdt.plusWeeks(factor);
            case "m", "month", "months" -> zdt.plusMonths(factor);
            case "y", "year", "years" -> zdt.plusYears(factor);
            default -> applySubDayOffset(zdt, unit, factor);
        };
    }

    private static ZonedDateTime applySubDayOffset(ZonedDateTime zdt, String unit, long factor) {
        return switch (unit) {
            case "h", "hour", "hours" -> zdt.plusHours(factor);
            case "min", "minute", "minutes" -> zdt.plusMinutes(factor);
            case "s", "sec", "second", "seconds" -> zdt.plusSeconds(factor);
            case "ms", "millis", "milliseconds" -> zdt.plusNanos(factor * 1_000_000);
            default -> throw new IllegalArgumentException("Unsupported datetime offset unit: " + unit);
        };
    }
}
