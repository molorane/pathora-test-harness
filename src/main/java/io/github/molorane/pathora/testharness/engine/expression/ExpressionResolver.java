package io.github.molorane.pathora.testharness.engine.expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Universal resolver for parsing and evaluating dynamic expressions across payload mutations, assertions, and utilities.
 *
 * <p>Supports braced expressions (e.g. {@code {{$CURRENT_DATE + 30d}}}, {@code {{$UUID}}}), standalone tokens
 * (e.g. {@code $EPOCH_MILLIS}), relative temporal offsets, and custom formatting patterns.</p>
 *
 * <p>Delegates token evaluation to {@link ExpressionRegistry} to adhere to the Open-Closed Principle (OCP).</p>
 */
public final class ExpressionResolver {

    private static final Pattern BRACED_PATTERN = Pattern.compile(
        "\\{\\{\\s*\\$([a-zA-Z0-9_]+)([^}:]*?)(?::([^}]*))?\\s*\\}\\}"
    );

    private static final Pattern STANDALONE_PATTERN = Pattern.compile(
        "^\\$([a-zA-Z0-9_]+)([^:]*?)(?::(.*))?$"
    );

    private ExpressionResolver() {
    }

    /**
     * Resolves dynamic expressions contained in the input object (String, List, Map, or scalar).
     *
     * @param input the input value
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
     * Resolves dynamic expressions in a string and returns the resulting string.
     *
     * @param input the input string
     * @return the resolved string, or {@code null} if input is null
     */
    public static String resolveToString(String input) {
        Object resolved = resolve(input);
        return resolved != null ? String.valueOf(resolved) : null;
    }

    private static Object resolveString(String str) {
        String trimmed = str.trim();

        // Check if string is a standalone expression that may evaluate to a numeric or typed scalar
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
        ExpressionTokenEvaluator evaluator = ExpressionRegistry.get(tokenName);
        if (evaluator == null) {
            throw new IllegalArgumentException("Unknown dynamic expression token: $" + tokenName);
        }
        return evaluator.evaluate(new ExpressionTokenEvaluator.TokenContext(
            tokenName, offsetStr, formatPattern, allowNumeric
        ));
    }
}
