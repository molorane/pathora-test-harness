package io.github.molorane.pathora.testharness.engine.expression.random;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator.TokenContext;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomBooleanTokenEvaluatorTest {

    private final RandomBooleanTokenEvaluator evaluator = new RandomBooleanTokenEvaluator();

    @Test
    void testSupportedTokens() {
        Set<String> tokens = evaluator.supportedTokens();
        assertTrue(tokens.contains("RANDOM_BOOLEAN"));
        assertTrue(tokens.contains("RANDOM_BOOL"));
    }

    @Test
    void testEvaluateStandaloneBoolean() {
        Object val = evaluator.evaluate(new TokenContext("RANDOM_BOOLEAN", null, null, true));
        assertNotNull(val);
        assertTrue(val instanceof Boolean);
    }

    @Test
    void testEvaluateStringEmbedded() {
        Object val = evaluator.evaluate(new TokenContext("RANDOM_BOOL", null, null, false));
        assertNotNull(val);
        assertTrue(val.equals("true") || val.equals("false"));
    }
}
