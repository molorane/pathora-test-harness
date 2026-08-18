package io.github.molorane.pathora.testharness.util;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MoneyUtils {

    private static final Pattern CURRENCY_PATTERN = Pattern.compile("([A-Za-z]{3})");

    private MoneyUtils() {
    }

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

    public static BigDecimal requireAmount(String path, Object value) {
        BigDecimal bd = extractAmount(value);
        if (bd == null) {
            throw new IllegalArgumentException("Expected monetary figure at path " + path + " but got: " + value);
        }
        return bd;
    }
}
