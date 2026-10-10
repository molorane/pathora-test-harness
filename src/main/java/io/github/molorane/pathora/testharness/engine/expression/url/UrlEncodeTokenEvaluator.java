package io.github.molorane.pathora.testharness.engine.expression.url;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * Evaluates {@code $URL_ENCODE:string} expression tokens.
 */
public class UrlEncodeTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("URL_ENCODE");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        String input = context.formatPattern() != null ? context.formatPattern() : "";
        return URLEncoder.encode(input, StandardCharsets.UTF_8);
    }
}
