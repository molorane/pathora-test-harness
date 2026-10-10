package io.github.molorane.pathora.testharness.engine.expression.base64;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;

/**
 * Evaluates {@code $BASE64_DECODE:string} expression tokens.
 */
public class Base64DecodeTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("BASE64_DECODE");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        String input = context.formatPattern() != null ? context.formatPattern() : "";
        if (input.isEmpty()) {
            return "";
        }
        byte[] decoded = Base64.getDecoder().decode(input);
        return new String(decoded, StandardCharsets.UTF_8);
    }
}
