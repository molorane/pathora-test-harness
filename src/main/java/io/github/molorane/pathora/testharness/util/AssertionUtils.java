package io.github.molorane.pathora.testharness.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Utility methods for normalizing values, extracting payloads, and performing deep/partial equality comparisons.
 */
public final class AssertionUtils {

    private AssertionUtils() {
    }

    /**
     * Normalizes the numeric types of actual and expected objects if applicable (e.g. converting numeric strings or Integers to Doubles).
     *
     * @param actual   the actual value
     * @param expected the expected value
     * @return a two-element array containing the normalized actual and expected values
     */
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

    /**
     * Unwraps single-element collections into a single scalar result, validating that path matches are unique.
     *
     * @param result the raw object or collection returned by JSONPath
     * @param path   the JSONPath evaluated
     * @return the unwrapped single object
     * @throws RuntimeException if the collection is empty or contains multiple elements
     */
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

    /**
     * Validates that the provided value is a {@link List}, throwing an {@link AssertionError} if it is not.
     *
     * @param value the value to check
     * @param path  the JSONPath being evaluated
     * @return the value cast to a List
     */
    public static List<?> requireList(Object value, String path) {
        if (!(value instanceof List<?> list)) {
            throw new AssertionError(
                    "Expected array at path " + path +
                            " but got: " + value);
        }
        return list;
    }

    /**
     * Normalizes expected strings into boolean, null, or numeric primitives where appropriate.
     *
     * @param expected the raw expected value
     * @return the parsed/normalized expected value
     */
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

    /**
     * Performs a deep equality comparison between actual and expected structures (Maps, Lists, scalars).
     *
     * @param actual   the actual value
     * @param expected the expected value
     * @return {@code true} if deeply equal, {@code false} otherwise
     */
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

    /**
     * Performs a deep partial equality check, verifying that all fields/elements in expected exist and match in actual.
     *
     * @param actual   the actual object or collection
     * @param expected the expected partial object or collection
     * @return {@code true} if actual partially contains expected, {@code false} otherwise
     */
    public static boolean deepPartialEquals(Object actual, Object expected) {
        if (actual == null || expected == null) {
            return actual == expected;
        }

        if (actual instanceof Map && expected instanceof Map) {
            return objectContainsPartialFields(actual, expected, false);
        }

        if (actual instanceof List && expected instanceof List) {
            return listContainsPartialElements(actual, expected);
        }

        Object[] normalized = normalizeTypes(actual, expected);
        return Objects.equals(normalized[0], normalized[1]);
    }

    /**
     * Validates that all key-value entries in expectedMap exist in actualMap using partial deep equality.
     *
     * @param actual      the actual map object
     * @param expected    the expected map object
     * @param ignoreNulls whether null expected values should be ignored
     * @return {@code true} if actual contains the expected partial fields
     */
    public static boolean objectContainsPartialFields(Object actual,
                                                      Object expected,
                                                      boolean ignoreNulls) {
        if (!(actual instanceof Map) || !(expected instanceof Map)) {
            return false;
        }

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

            if (!deepPartialEquals(actualValue, expectedValue)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Validates that every item in the expected list matches at least one item in the actual list by partial deep equality.
     *
     * @param actual   the actual list
     * @param expected the expected list
     * @return {@code true} if every expected element is partially matched in actual
     */
    public static boolean listContainsPartialElements(Object actual, Object expected) {
        if (!(actual instanceof List<?> actualList) || !(expected instanceof List<?> expectedList)) {
            return false;
        }

        for (Object expectedItem : expectedList) {
            boolean matched = actualList.stream()
                    .anyMatch(actualItem -> deepPartialEquals(actualItem, expectedItem));
            if (!matched) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates that all fields in expected are present and strictly equal in actual.
     *
     * @param actual      the actual map object
     * @param expected    the expected map object
     * @param ignoreNulls whether null expected fields are ignored
     * @return {@code true} if actual contains all expected fields
     */
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

    /**
     * Casts or converts an object to a Map of String keys.
     *
     * @param value the object to convert
     * @return the map
     * @throws IllegalArgumentException if value is not a Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object value) {
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        throw new IllegalArgumentException(
                "Expected object but got: " + value);
    }

    /**
     * Converts a value to a list or returns an empty list if null, or null if not a list.
     *
     * @param actual the value
     * @return the list, empty list if null, or null if not a list
     */
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
