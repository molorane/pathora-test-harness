package io.github.molorane.pathora.testharness.engine.expression;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionRegistryTest {

    @Test
    void testBuiltInTokensAreRegistered() {
        assertNotNull(ExpressionRegistry.get("CURRENT_DATE"));
        assertNotNull(ExpressionRegistry.get("TODAY"));
        assertNotNull(ExpressionRegistry.get("CURRENT_DATETIME"));
        assertNotNull(ExpressionRegistry.get("NOW"));
        assertNotNull(ExpressionRegistry.get("EPOCH_MILLIS"));
        assertNotNull(ExpressionRegistry.get("TIMESTAMP"));
        assertNotNull(ExpressionRegistry.get("EPOCH_SECONDS"));
    }

    @Test
    void testCustomExpressionEvaluatorRegistrationWithoutModifyingCoreCode() {
        // Demonstrate OCP by registering a custom token "CUSTOM_GREETING" dynamically
        ExpressionTokenEvaluator customEvaluator = new ExpressionTokenEvaluator() {
            @Override
            public Set<String> supportedTokens() {
                return Set.of("CUSTOM_GREETING");
            }

            @Override
            public Object evaluate(TokenContext context) {
                return "Hello " + (context.offsetStr() != null ? context.offsetStr().trim() : "World");
            }
        };

        ExpressionRegistry.register(customEvaluator);

        // Verify that ExpressionResolver resolves the custom expression seamlessly
        assertEquals("Hello Pathora", ExpressionResolver.resolve("{{$CUSTOM_GREETING Pathora}}"));
    }
}
