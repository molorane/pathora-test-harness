package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Shared utility helper methods for string assertions and evaluations.
 */
final class StringHelper {

    private StringHelper() {
    }

    /**
     * Converts a raw value or single-element list into a normalized string.
     *
     * @param value the raw value
     * @param path  the JSONPath context
     * @return the string value, or null
     */
    static String toSingleString(Object value, String path) {
        if (value == null) {
            return null;
        }
        Object normalized = AssertionUtils.normalizeResult(value, path);
        return normalized == null ? null : String.valueOf(normalized);
    }

    /**
     * Normalizes a value (array, collection, or scalar) into an array of {@link CharSequence}.
     *
     * @param value the raw object
     * @return array of char sequences
     */
    static CharSequence[] toCharSequenceArray(Object value) {
        if (value == null) {
            return new CharSequence[0];
        }
        if (value instanceof List<?> list) {
            return list.stream()
                    .map(item -> item == null ? null : String.valueOf(item))
                    .toArray(CharSequence[]::new);
        }
        if (value instanceof Object[] array) {
            return Arrays.stream(array)
                    .map(item -> item == null ? null : String.valueOf(item))
                    .toArray(CharSequence[]::new);
        }
        return new CharSequence[]{String.valueOf(value)};
    }

    /**
     * Checks if {@code str} contains {@code searchStr}.
     *
     * @param str       the string to search within
     * @param searchStr the substring to search for
     * @return {@code true} if present
     */
    static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.contains(searchStr);
    }

    /**
     * Checks if {@code str} contains {@code searchStr} ignoring case.
     *
     * @param str       the target string
     * @param searchStr the search substring
     * @return {@code true} if present (case-insensitive)
     */
    static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        int len = searchStr.length();
        int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (str.regionMatches(true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if {@code str} starts with {@code prefix} ignoring case.
     *
     * @param str    the target string
     * @param prefix the prefix to check
     * @return {@code true} if matched
     */
    static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) {
            return str == null && prefix == null;
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    /**
     * Checks if {@code str} starts with any of the provided prefixes.
     *
     * @param str      the target string
     * @param prefixes the candidate prefixes
     * @return {@code true} if starts with any
     */
    static boolean startsWithAny(String str, CharSequence... prefixes) {
        if (str == null || prefixes == null) {
            return false;
        }
        return Arrays.stream(prefixes)
                .filter(Objects::nonNull)
                .anyMatch(p -> str.startsWith(p.toString()));
    }

    /**
     * Checks if {@code str} ends with {@code suffix} ignoring case.
     *
     * @param str    the target string
     * @param suffix the suffix to check
     * @return {@code true} if matched
     */
    static boolean endsWithIgnoreCase(String str, String suffix) {
        if (str == null || suffix == null) {
            return str == null && suffix == null;
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        return str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length());
    }

    /**
     * Checks if {@code str} ends with any of the candidate suffixes.
     *
     * @param str      the target string
     * @param suffixes the candidate suffixes
     * @return {@code true} if ends with any
     */
    static boolean endsWithAny(String str, CharSequence... suffixes) {
        if (str == null || suffixes == null) {
            return false;
        }
        return Arrays.stream(suffixes)
                .filter(Objects::nonNull)
                .anyMatch(s -> str.endsWith(s.toString()));
    }

    /**
     * Checks if {@code str} ends with any of the candidate suffixes ignoring case.
     *
     * @param str      the target string
     * @param suffixes the candidate suffixes
     * @return {@code true} if ends with any
     */
    static boolean endsWithAnyIgnoreCase(String str, CharSequence... suffixes) {
        if (str == null || suffixes == null) {
            return false;
        }
        return Arrays.stream(suffixes)
                .filter(Objects::nonNull)
                .anyMatch(s -> endsWithIgnoreCase(str, s.toString()));
    }

    /**
     * Checks equality of two strings ignoring case.
     *
     * @param str1 the first string
     * @param str2 the second string
     * @return {@code true} if equal ignoring case
     */
    static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return str1 == str2;
        }
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * Checks if {@code str} equals any of the candidate strings.
     *
     * @param str           the target string
     * @param searchStrings the candidate strings
     * @return {@code true} if matched
     */
    static boolean equalsAny(String str, CharSequence... searchStrings) {
        if (str == null || searchStrings == null) {
            return false;
        }
        return Arrays.stream(searchStrings)
                .anyMatch(s -> Objects.equals(str, s == null ? null : s.toString()));
    }

    /**
     * Checks if {@code str} equals any of the candidate strings ignoring case.
     *
     * @param str           the target string
     * @param searchStrings the candidate strings
     * @return {@code true} if matched
     */
    static boolean equalsAnyIgnoreCase(String str, CharSequence... searchStrings) {
        if (str == null || searchStrings == null) {
            return false;
        }
        return Arrays.stream(searchStrings)
                .anyMatch(s -> s != null && str.equalsIgnoreCase(s.toString()));
    }
}




