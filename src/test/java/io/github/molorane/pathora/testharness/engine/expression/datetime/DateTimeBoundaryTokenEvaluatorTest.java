package io.github.molorane.pathora.testharness.engine.expression.datetime;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator.TokenContext;
import io.github.molorane.pathora.testharness.util.PathoraClock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeBoundaryTokenEvaluatorTest {

    private final DateTimeBoundaryTokenEvaluator evaluator = new DateTimeBoundaryTokenEvaluator();

    @BeforeEach
    void setUp() {
        // Freeze to 2026-10-10T14:30:00Z
        PathoraClock.freeze(Instant.parse("2026-10-10T14:30:00Z"));
    }

    @AfterEach
    void tearDown() {
        PathoraClock.reset();
    }

    @Test
    void testSupportedTokens() {
        Set<String> tokens = evaluator.supportedTokens();
        assertTrue(tokens.contains("START_OF_DAY"));
        assertTrue(tokens.contains("END_OF_DAY"));
    }

    @Test
    void testStartAndEndOfDay() {
        assertEquals("2026-10-10T00:00:00Z", evaluator.evaluate(new TokenContext("START_OF_DAY", null, null, false)));
        assertEquals("2026-10-10T23:59:59.999999999Z", evaluator.evaluate(new TokenContext("END_OF_DAY", null, null, false)));
    }

    @Test
    void testStartOfDayWithCustomFormat() {
        Object val = evaluator.evaluate(new TokenContext("START_OF_DAY", null, "yyyy-MM-dd HH:mm:ss", false));
        assertEquals("2026-10-10 00:00:00", val);
    }
}
