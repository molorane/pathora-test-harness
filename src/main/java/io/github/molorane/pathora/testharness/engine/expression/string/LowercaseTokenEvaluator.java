package io.github.molorane.pathora.testharness.engine.expression.string;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;

import java.util.Locale;
import java.util.Set;

/**
 * Evaluates {@code $LOWERCASE:text} expression tokens.
 */
public class LowercaseTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("LOWERCASE");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        String input = context.formatPattern() != null ? context.formatPattern() : "";
        return input.toLowerCase(Locale.ROOT);
    }
}
