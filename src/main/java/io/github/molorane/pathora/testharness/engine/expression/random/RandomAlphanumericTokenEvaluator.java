package io.github.molorane.pathora.testharness.engine.expression.random;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Evaluates {@code $RANDOM_ALPHANUMERIC} and {@code $RANDOM_STRING} expression tokens.
 *
 * <p>Supports custom length parameter (e.g. {@code REF-{{$RANDOM_ALPHANUMERIC:8}}}).</p>
 */
public class RandomAlphanumericTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("RANDOM_ALPHANUMERIC", "RANDOM_STRING");
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        int length = 8;
        String param = context.formatPattern() != null && !context.formatPattern().isBlank()
            ? context.formatPattern().trim()
            : context.offsetStr() != null ? context.offsetStr().trim() : "";

        if (!param.isBlank()) {
            try {
                length = Integer.parseInt(param);
            } catch (NumberFormatException ignored) {
            }
        }

        if (length <= 0) {
            length = 8;
        }

        StringBuilder sb = new StringBuilder(length);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
