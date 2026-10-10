package io.github.molorane.pathora.testharness.engine.expression.random;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator.TokenContext;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomAlphanumericTokenEvaluatorTest {

    private final RandomAlphanumericTokenEvaluator evaluator = new RandomAlphanumericTokenEvaluator();

    @Test
    void testSupportedTokens() {
        Set<String> tokens = evaluator.supportedTokens();
        assertTrue(tokens.contains("RANDOM_ALPHANUMERIC"));
        assertTrue(tokens.contains("RANDOM_STRING"));
    }

    @Test
    void testEvaluateWithCustomLength() {
        Object val = evaluator.evaluate(new TokenContext("RANDOM_ALPHANUMERIC", null, "12", false));
        assertNotNull(val);
        assertEquals(12, val.toString().length());
        assertTrue(val.toString().matches("^[A-Z0-9]{12}$"));
    }

    @Test
    void testEvaluateDefaultLength() {
        Object val = evaluator.evaluate(new TokenContext("RANDOM_STRING", null, null, false));
        assertNotNull(val);
        assertEquals(8, val.toString().length());
    }
}
