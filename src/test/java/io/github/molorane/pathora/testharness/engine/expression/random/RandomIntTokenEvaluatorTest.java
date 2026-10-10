package io.github.molorane.pathora.testharness.engine.expression.random;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator.TokenContext;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomIntTokenEvaluatorTest {

    private final RandomIntTokenEvaluator evaluator = new RandomIntTokenEvaluator();

    @Test
    void testSupportedTokens() {
        Set<String> tokens = evaluator.supportedTokens();
        assertTrue(tokens.contains("RANDOM_INT"));
        assertTrue(tokens.contains("RANDOM_INTEGER"));
    }

    @Test
    void testEvaluateWithMinMaxBounds() {
        Object val = evaluator.evaluate(new TokenContext("RANDOM_INT", null, "1000:9999", true));
        assertNotNull(val);
        assertTrue(val instanceof Integer || val instanceof Long);
        int num = ((Number) val).intValue();
        assertTrue(num >= 1000 && num <= 9999);
    }

    @Test
    void testEvaluateStringEmbedded() {
        Object val = evaluator.evaluate(new TokenContext("RANDOM_INT", null, "10:20", false));
        assertNotNull(val);
        assertTrue(val instanceof String);
        int num = Integer.parseInt((String) val);
        assertTrue(num >= 10 && num <= 20);
    }
}
