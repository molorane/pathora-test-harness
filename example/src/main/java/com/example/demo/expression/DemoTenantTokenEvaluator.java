package com.example.demo.expression;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;

import java.util.Set;

/**
 * Custom expression evaluator for generating demo tenant code tokens in JSON test files.
 *
 * <p>Supported tokens: {@code DEMO_TENANT}, {@code TENANT_CODE}.
 * Usage example: {@code {{$DEMO_TENANT:CYBERDYNE}} -> "TNT-CYBERDYNE-999"}</p>
 */
public class DemoTenantTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("DEMO_TENANT", "TENANT_CODE");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        String param = context.formatPattern() != null && !context.formatPattern().isBlank()
            ? context.formatPattern().trim()
            : (context.offsetStr() != null ? context.offsetStr().trim() : "DEFAULT");

        return "TNT-" + param.toUpperCase() + "-999";
    }
}
