package io.github.molorane.pathora.testharness.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for parsing and resolving dynamic date, time, and timestamp expressions.
 *
 * <p>Supports tokens such as {@code {{$CURRENT_DATE}}}, {@code {{$CURRENT_DATETIME}}},
 * {@code {{$TODAY}}}, {@code {{$NOW}}}, {@code {{$EPOCH_MILLIS}}}, relative offsets
 * (e.g., {@code +30d}, {@code -25y}, {@code +2h}), and custom formatting patterns.</p>
 */
public final class DateExpressionResolver {

    private static final Pattern BRACED_PATTERN = Pattern.compile(
        "\\{\\{\\s*\\$([a-zA-Z_]+)([^}:]*?)(?::([^}]+))?\\s*\\}\\}"
    );

    private static final Pattern STANDALONE_PATTERN = Pattern.compile(
        "^\\$([a-zA-Z_]+)([^:]*?)(?::(.+))?$"
    );

    private static final Pattern OFFSET_PATTERN = Pattern.compile(
        "([+-])\\s*(\\d+)\\s*([a-zA-Z]+)"
    );

    private DateExpressionResolver() {
    }

    /**
     * Resolves dynamic date expressions contained in the input object.
     *
     * @param input the input value (String, Map, List, or scalar)
     * @return the resolved object
     */
    public static Object resolve(Object input) {
        if (input == null) {
            return null;
        }
        if (input instanceof String str) {
            return resolveString(str);
        }
        if (input instanceof List<?> list) {
            return resolveList(list);
        }
        if (input instanceof Map<?, ?> map) {
            return resolveMap(map);
        }
        return input;
    }

    /**
     * Resolves dynamic date expressions in a string and returns the resulting string.
     *
     * @param input the input string
     * @return the resolved string
     */
    public static String resolveToString(String input) {
        Object resolved = resolve(input);
        return resolved != null ? String.valueOf(resolved) : null;
    }

    private static Object resolveString(String str) {
        String trimmed = str.trim();

        // Check if string is a standalone expression that may evaluate to a numeric type
        Matcher standaloneMatcher = STANDALONE_PATTERN.matcher(trimmed);
        if (standaloneMatcher.matches()) {
            return evaluateToken(standaloneMatcher.group(1), standaloneMatcher.group(2), standaloneMatcher.group(3), true);
        }

        Matcher bracedMatcher = BRACED_PATTERN.matcher(str);
        if (bracedMatcher.find()) {
            // Check if entire trimmed string is just one braced expression
            if (trimmed.startsWith("{{") && trimmed.endsWith("}}") && countMatches(str, "{{") == 1) {
                bracedMatcher.reset();
                if (bracedMatcher.matches()) {
                    return evaluateToken(bracedMatcher.group(1), bracedMatcher.group(2), bracedMatcher.group(3), true);
                }
            }
            return replaceBracedExpressions(str);
        }

        return str;
    }

    private static String replaceBracedExpressions(String str) {
        Matcher matcher = BRACED_PATTERN.matcher(str);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            Object val = evaluateToken(matcher.group(1), matcher.group(2), matcher.group(3), false);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(String.valueOf(val)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static int countMatches(String str, String sub) {
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }

    private static List<Object> resolveList(List<?> list) {
        List<Object> resolved = new ArrayList<>(list.size());
        for (Object item : list) {
            resolved.add(resolve(item));
        }
        return resolved;
    }

    private static Map<Object, Object> resolveMap(Map<?, ?> map) {
        Map<Object, Object> resolved = new LinkedHashMap<>(map.size());
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            resolved.put(entry.getKey(), resolve(entry.getValue()));
        }
        return resolved;
    }

    private static Object evaluateToken(String tokenName, String offsetStr, String formatPattern, boolean allowNumeric) {
        String tokenUpper = tokenName.toUpperCase();

        return switch (tokenUpper) {
            case "CURRENT_DATE", "TODAY" -> evaluateDateToken(offsetStr, formatPattern);
            case "CURRENT_DATETIME", "NOW" -> evaluateDateTimeToken(offsetStr, formatPattern);
            case "EPOCH_MILLIS", "TIMESTAMP" -> evaluateEpochMillis(offsetStr, allowNumeric);
            case "EPOCH_SECONDS" -> evaluateEpochSeconds(offsetStr, allowNumeric);
            default -> throw new IllegalArgumentException("Unknown dynamic date token: $" + tokenName);
        };
    }

    private static Object evaluateDateToken(String offsetStr, String formatPattern) {
        LocalDate date = PathoraClock.today();
        if (offsetStr != null && !offsetStr.isBlank()) {
            date = applyDateOffsets(date, offsetStr);
        }
        if (formatPattern != null && !formatPattern.isBlank()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern.trim());
            return date.format(formatter);
        }
        return date.toString();
    }

    private static Object evaluateDateTimeToken(String offsetStr, String formatPattern) {
        ZonedDateTime zdt = PathoraClock.nowZoned();
        if (offsetStr != null && !offsetStr.isBlank()) {
            zdt = applyZonedDateTimeOffsets(zdt, offsetStr);
        }
        if (formatPattern != null && !formatPattern.isBlank()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern.trim());
            return zdt.format(formatter);
        }
        return zdt.toInstant().toString();
    }

    private static Object evaluateEpochMillis(String offsetStr, boolean allowNumeric) {
        Instant instant = PathoraClock.instant();
        if (offsetStr != null && !offsetStr.isBlank()) {
            ZonedDateTime zdt = instant.atZone(PathoraClock.getZoneId());
            zdt = applyZonedDateTimeOffsets(zdt, offsetStr);
            instant = zdt.toInstant();
        }
        long millis = instant.toEpochMilli();
        return allowNumeric ? millis : String.valueOf(millis);
    }

    private static Object evaluateEpochSeconds(String offsetStr, boolean allowNumeric) {
        Instant instant = PathoraClock.instant();
        if (offsetStr != null && !offsetStr.isBlank()) {
            ZonedDateTime zdt = instant.atZone(PathoraClock.getZoneId());
            zdt = applyZonedDateTimeOffsets(zdt, offsetStr);
            instant = zdt.toInstant();
        }
        long seconds = instant.getEpochSecond();
        return allowNumeric ? seconds : String.valueOf(seconds);
    }

    private static LocalDate applyDateOffsets(LocalDate date, String offsetStr) {
        Matcher matcher = OFFSET_PATTERN.matcher(offsetStr);
        LocalDate result = date;
        while (matcher.find()) {
            String sign = matcher.group(1);
            long amount = Long.parseLong(matcher.group(2));
            String unit = matcher.group(3).toLowerCase();
            long factor = "-".equals(sign) ? -amount : amount;

            result = switch (unit) {
                case "d", "day", "days" -> result.plusDays(factor);
                case "w", "week", "weeks" -> result.plusWeeks(factor);
                case "m", "month", "months" -> result.plusMonths(factor);
                case "y", "year", "years" -> result.plusYears(factor);
                default -> throw new IllegalArgumentException("Unsupported date offset unit: " + unit);
            };
        }
        return result;
    }

    private static ZonedDateTime applyZonedDateTimeOffsets(ZonedDateTime zdt, String offsetStr) {
        Matcher matcher = OFFSET_PATTERN.matcher(offsetStr);
        ZonedDateTime result = zdt;
        while (matcher.find()) {
            String sign = matcher.group(1);
            long amount = Long.parseLong(matcher.group(2));
            String unit = matcher.group(3).toLowerCase();
            long factor = "-".equals(sign) ? -amount : amount;

            result = switch (unit) {
                case "d", "day", "days" -> result.plusDays(factor);
                case "w", "week", "weeks" -> result.plusWeeks(factor);
                case "m", "month", "months" -> result.plusMonths(factor);
                case "y", "year", "years" -> result.plusYears(factor);
                case "h", "hour", "hours" -> result.plusHours(factor);
                case "min", "minute", "minutes" -> result.plusMinutes(factor);
                case "s", "sec", "second", "seconds" -> result.plusSeconds(factor);
                case "ms", "millis", "milliseconds" -> result.plusNanos(factor * 1_000_000);
                default -> throw new IllegalArgumentException("Unsupported datetime offset unit: " + unit);
            };
        }
        return result;
    }
}

