package io.github.molorane.pathora.testharness.engine.expression.env;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;

import java.util.Set;

/**
 * Evaluates {@code $ENV:VAR_NAME} and {@code $ENV:VAR_NAME:default_value} expression tokens.
 */
public class EnvTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("ENV", "ENVIRONMENT");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        String param = extractParam(context);
        if (param.isBlank()) {
            return "";
        }

        String[] parts = param.split(":", 2);
        String varName = parts[0].trim();
        String defaultValue = parts.length > 1 ? parts[1] : "";

        String envValue = System.getenv(varName);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        return defaultValue;
    }

    private String extractParam(TokenContext context) {
        if (context.formatPattern() != null && !context.formatPattern().isBlank()) {
            return context.formatPattern().trim();
        }
        if (context.offsetStr() != null) {
            return context.offsetStr().trim();
        }
        return "";
    }
}
