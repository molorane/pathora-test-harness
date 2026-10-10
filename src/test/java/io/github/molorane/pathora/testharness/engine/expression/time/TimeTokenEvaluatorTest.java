package io.github.molorane.pathora.testharness.engine.expression.time;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator.TokenContext;
import io.github.molorane.pathora.testharness.util.PathoraClock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TimeTokenEvaluatorTest {

    private final TimeTokenEvaluator evaluator = new TimeTokenEvaluator();

    @BeforeEach
    void setUp() {
        PathoraClock.freeze(Instant.parse("2026-10-07T14:30:15Z"));
    }

    @AfterEach
    void tearDown() {
        PathoraClock.reset();
    }

    @Test
    void testSupportedTokens() {
        Set<String> tokens = evaluator.supportedTokens();
        assertTrue(tokens.contains("CURRENT_TIME"));
    }

    @Test
    void testEvaluateDefaultTimeFormat() {
        Object val = evaluator.evaluate(new TokenContext("CURRENT_TIME", null, null, false));
        assertEquals("14:30:15", val);
    }

    @Test
    void testEvaluateCustomTimeFormat() {
        Object val = evaluator.evaluate(new TokenContext("CURRENT_TIME", null, "HH:mm", false));
        assertEquals("14:30", val);
    }
}
