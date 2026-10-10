package io.github.molorane.pathora.testharness.engine.expression.base64;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;

/**
 * Evaluates {@code $BASE64_ENCODE:string} expression tokens.
 */
public class Base64EncodeTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("BASE64_ENCODE");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        String input = context.formatPattern() != null ? context.formatPattern() : "";
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }
}
