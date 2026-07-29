package io.github.molorane.pathora.testharness.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class AssertionUtils {

    private AssertionUtils() {
    }

    public static Object[] normalizeTypes(Object actual, Object expected) {
        if (actual == null || expected == null) {
            return new Object[]{actual, expected};
        }
        Object[] normalized = tryNormalizeNumbers(actual, expected);
        if (normalized != null) {
            return normalized;
        }
        return new Object[]{actual, expected};
    }

    private static Double tryParseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static Object[] tryNormalizeNumbers(Object actual, Object expected) {
        if (actual instanceof Number actNum) {
            if (expected instanceof Number expNum) {
                return new Object[]{actNum.doubleValue(), expNum.doubleValue()};
            }
            if (expected instanceof String expStr) {
                Double d = tryParseDouble(expStr);
                if (d != null) {
                    return new Object[]{actNum.doubleValue(), d};
                }
            }
        } else if (expected instanceof Number expNum && actual instanceof String actStr) {
            Double d = tryParseDouble(actStr);
            if (d != null) {
                return new Object[]{d, expNum.doubleValue()};
            }
        }
        return null;
    }

    public static Object normalizeResult(Object result, String path) {
        if (result instanceof List<?> list) {

            if (list.isEmpty()) {
                throw new RuntimeException(
                        "No match found for path: " + path);
            }

            if (list.size() > 1) {
                throw new RuntimeException(
                        "Multiple matches found for path: " + path);
            }

            return list.get(0);
        }

        return result;
    }

    public static List<?> requireList(Object value, String path) {
        if (!(value instanceof List<?> list)) {
            throw new AssertionError(
                    "Expected array at path " + path +
                            " but got: " + value);
        }
        return list;
    }

    public static Object normalizeExpected(Object expected) {
        if (expected == null) {
            return null;
        }
        if (!(expected instanceof String str)) {
            return expected;
        }
        return parseString(str.trim());
    }

    private static Object parseString(String str) {
        if ("null".equalsIgnoreCase(str)) {
            return null;
        }
        if ("true".equalsIgnoreCase(str)) {
            return true;
        }
        if ("false".equalsIgnoreCase(str)) {
            return false;
        }
        Object num = tryParseNumericString(str);
        if (num != null) {
            return num;
        }
        return str;
    }

    private static Object tryParseNumericString(String str) {
        if (str.matches("-?\\d+")) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException ignored) {
            }
        }
        if (str.matches("-?\\d+\\.\\d+")) {
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    public static boolean deepEquals(Object actual, Object expected) {

        if (actual == null || expected == null) {
            return actual == expected;
        }

        if (actual instanceof Map && expected instanceof Map) {
            return objectContainsFields(actual, expected, false);
        }

        if (actual instanceof List && expected instanceof List) {
            return Objects.equals(actual, expected);
        }

        Object[] normalized = normalizeTypes(actual, expected);
        return Objects.equals(normalized[0], normalized[1]);
    }

    public static boolean objectContainsFields(Object actual,
                                               Object expected,
                                               boolean ignoreNulls) {

        Map<String, Object> actualMap = toMap(actual);
        Map<String, Object> expectedMap = toMap(expected);

        for (Map.Entry<String, Object> entry : expectedMap.entrySet()) {

            String key = entry.getKey();
            Object expectedValue = entry.getValue();

            if (ignoreNulls && expectedValue == null) {
                continue;
            }

            if (!actualMap.containsKey(key)) {
                return false;
            }

            Object actualValue = actualMap.get(key);

            if (!deepEquals(actualValue, expectedValue)) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object value) {
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        throw new IllegalArgumentException(
                "Expected object but got: " + value);
    }

    public static List<?> toListOrEmpty(Object actual) {
        if (actual == null) {
            return Collections.emptyList();
        }
        if (actual instanceof List<?>) {
            return (List<?>) actual;
        }
        return null;
    }
}
