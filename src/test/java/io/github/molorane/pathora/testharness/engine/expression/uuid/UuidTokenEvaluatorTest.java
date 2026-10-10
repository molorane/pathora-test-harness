package io.github.molorane.pathora.testharness.engine.expression.uuid;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator.TokenContext;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UuidTokenEvaluatorTest {

    private final UuidTokenEvaluator evaluator = new UuidTokenEvaluator();

    @Test
    void testSupportedTokens() {
        Set<String> tokens = evaluator.supportedTokens();
        assertTrue(tokens.contains("UUID"));
        assertTrue(tokens.contains("RANDOM_UUID"));
    }

    @Test
    void testEvaluateUuidFormat() {
        Object val = evaluator.evaluate(new TokenContext("UUID", null, null, false));
        assertNotNull(val);
        assertTrue(val.toString().matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"));
    }
}
