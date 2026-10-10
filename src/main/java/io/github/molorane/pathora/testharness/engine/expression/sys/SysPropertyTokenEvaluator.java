package io.github.molorane.pathora.testharness.engine.expression.sys;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;

import java.util.Set;

/**
 * Evaluates {@code $SYS:property.name} and {@code $SYS:property.name:default_value} expression tokens.
 */
public class SysPropertyTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("SYS", "SYSTEM_PROPERTY", "PROP", "PROPERTY", "PATHORA", "CONFIG");

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
        String propName = parts[0].trim();
        String defaultValue = parts.length > 1 ? parts[1] : "";

        String sysValue = System.getProperty(propName);
        if (sysValue != null && !sysValue.isBlank()) {
            return sysValue;
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
