package io.github.molorane.pathora.testharness.engine.expression.random;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator.TokenContext;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomEmailTokenEvaluatorTest {

    private final RandomEmailTokenEvaluator evaluator = new RandomEmailTokenEvaluator();

    @Test
    void testSupportedTokens() {
        Set<String> tokens = evaluator.supportedTokens();
        assertTrue(tokens.contains("RANDOM_EMAIL"));
    }

    @Test
    void testEvaluateEmailFormat() {
        Object email = evaluator.evaluate(new TokenContext("RANDOM_EMAIL", null, null, false));
        assertNotNull(email);
        assertTrue(email.toString().startsWith("user_"));
        assertTrue(email.toString().endsWith("@test.com"));
    }
}
