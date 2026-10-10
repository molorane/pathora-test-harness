package io.github.molorane.pathora.testharness.engine.expression.datetime;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator.TokenContext;
import io.github.molorane.pathora.testharness.util.PathoraClock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeTokenEvaluatorTest {

    private final DateTimeTokenEvaluator evaluator = new DateTimeTokenEvaluator();

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
        assertTrue(tokens.contains("CURRENT_DATETIME"));
        assertTrue(tokens.contains("NOW"));
    }

    @Test
    void testEvaluateBaseDateTime() {
        Object now = evaluator.evaluate(new TokenContext("NOW", null, null, false));
        assertEquals("2026-10-07T12:00:00Z", now);
    }

    @Test
    void testEvaluateWithHourOffset() {
        Object nowPlus2h = evaluator.evaluate(new TokenContext("CURRENT_DATETIME", "+2h", null, false));
        assertEquals("2026-10-07T14:00:00Z", nowPlus2h);
    }

    @Test
    void testEvaluateWithCustomFormat() {
        Object formatted = evaluator.evaluate(new TokenContext("NOW", "+2h", "yyyy/MM/dd HH:mm", false));
        assertEquals("2026/10/07 14:00", formatted);
    }
}
