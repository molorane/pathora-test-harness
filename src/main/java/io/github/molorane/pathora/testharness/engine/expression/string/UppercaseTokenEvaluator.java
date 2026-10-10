package io.github.molorane.pathora.testharness.engine.expression.string;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;

import java.util.Locale;
import java.util.Set;

/**
 * Evaluates {@code $UPPERCASE:text} expression tokens.
 */
public class UppercaseTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("UPPERCASE");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        String input = context.formatPattern() != null ? context.formatPattern() : "";
        return input.toUpperCase(Locale.ROOT);
    }
}
