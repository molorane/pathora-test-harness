package io.github.molorane.pathora.testharness.engine.expression.epoch;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator.TokenContext;
import io.github.molorane.pathora.testharness.util.PathoraClock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EpochMillisTokenEvaluatorTest {

    private final EpochMillisTokenEvaluator evaluator = new EpochMillisTokenEvaluator();

    @BeforeEach
    void setUp() {
        PathoraClock.freeze(Instant.parse("2026-10-07T12:00:00Z"));
    }

    @AfterEach
    void tearDown() {
        PathoraClock.reset();
    }

    @Test
    void testSupportedTokens() {
        Set<String> tokens = evaluator.supportedTokens();
        assertTrue(tokens.contains("EPOCH_MILLIS"));
        assertTrue(tokens.contains("TIMESTAMP"));
    }

    @Test
    void testEvaluateNumericStandalone() {
        Object millis = evaluator.evaluate(new TokenContext("EPOCH_MILLIS", null, null, true));
        assertEquals(1791374400000L, millis);
    }

    @Test
    void testEvaluateStringEmbedded() {
        Object millisStr = evaluator.evaluate(new TokenContext("TIMESTAMP", null, null, false));
        assertEquals("1791374400000", millisStr);
    }
}
