package io.github.molorane.pathora.testharness.util;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility functions for parsing monetary figures, amounts, currency codes, and evaluating monetary tolerances.
 */
public final class MoneyUtils {

    private static final Pattern CURRENCY_PATTERN = Pattern.compile("([A-Za-z]{3})");

    private MoneyUtils() {
    }

    /**
     * Extracts a {@link BigDecimal} amount from various representations (BigDecimal, Number, Map with amount/value/price key, String).
     *
     * @param value the raw monetary value
     * @return the extracted {@link BigDecimal}, or {@code null} if it cannot be extracted
     */
    public static BigDecimal extractAmount(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal bd) {
            return bd;
        }
        if (value instanceof BigInteger bi) {
            return new BigDecimal(bi);
        }
        if (value instanceof Number num) {
            return new BigDecimal(num.toString());
        }
        if (value instanceof Map<?, ?> map) {
            return extractFromMap(map);
        }
        if (value instanceof String str) {
            return parseString(str);
        }
        return null;
    }

    private static BigDecimal extractFromMap(Map<?, ?> map) {
        for (String key : new String[]{"amount", "value", "price"}) {
            Object amtObj = map.get(key);
            if (amtObj != null) {
                return extractAmount(amtObj);
            }
        }
        return null;
    }

    private static BigDecimal parseString(String str) {
        String sanitized = str.replaceAll("[^0-9.-]", "");
        if (sanitized.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(sanitized);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    /**
     * Extracts a 3-letter currency code (e.g., "USD", "ZAR", "EUR") from a Map or String.
     *
     * @param value the raw object containing currency information
     * @return the uppercase 3-letter currency code, or {@code null} if not found
     */
    public static String extractCurrency(Object value) {
        if (value instanceof Map<?, ?> map) {
            Object currObj = map.get("currency");
            if (currObj == null) {
                currObj = map.get("currencyCode");
            }
            if (currObj != null) {
                return String.valueOf(currObj).trim().toUpperCase();
            }
        }
        if (value instanceof String str) {
            Matcher matcher = CURRENCY_PATTERN.matcher(str);
            if (matcher.find()) {
                return matcher.group(1).toUpperCase();
            }
        }
        return null;
    }

    /**
     * Validates that both actual and expected monetary values share the same currency code if both specify currency.
     *
     * @param path     the JSONPath being evaluated
     * @param actual   the actual monetary value
     * @param expected the expected monetary value
     * @param operator the assertion operator being executed
     * @throws HarnessAssertionException if currency codes are present and do not match
     */
    public static void validateCurrencyMatch(String path, Object actual, Object expected, AssertionOperator operator) {
        String actualCurrency = extractCurrency(actual);
        String expectedCurrency = extractCurrency(expected);

        if (actualCurrency != null && expectedCurrency != null && !actualCurrency.equals(expectedCurrency)) {
            throw new HarnessAssertionException(
                operator,
                path,
                expectedCurrency,
                actualCurrency,
                "Currency mismatch at " + path + ". Expected currency: " + expectedCurrency +
                    ", Actual currency: " + actualCurrency);
        }
    }

    /**
     * Extracts a {@link BigDecimal} amount or throws an {@link IllegalArgumentException} if extraction fails.
     *
     * @param path  the JSONPath being evaluated
     * @param value the raw monetary value
     * @return the extracted {@link BigDecimal}
     * @throws IllegalArgumentException if the monetary figure cannot be parsed
     */
    public static BigDecimal requireAmount(String path, Object value) {
        BigDecimal bd = extractAmount(value);
        if (bd == null) {
            throw new IllegalArgumentException("Expected monetary figure at path " + path + " but got: " + value);
        }
        return bd;
    }

    /**
     * Checks if actual and expected amounts differ by no more than tolerance inclusive.
     *
     * @param actual    the actual amount
     * @param expected  the expected amount
     * @param tolerance the maximum permitted absolute difference
     * @return {@code true} if within tolerance, {@code false} otherwise
     */
    public static boolean isWithinTolerance(BigDecimal actual, BigDecimal expected, BigDecimal tolerance) {
        if (actual == null || expected == null || tolerance == null) {
            return false;
        }
        BigDecimal diff = actual.subtract(expected).abs();
        return diff.compareTo(tolerance) <= 0;
    }
}


