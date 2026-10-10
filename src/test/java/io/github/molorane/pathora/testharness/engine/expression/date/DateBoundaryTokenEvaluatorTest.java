package io.github.molorane.pathora.testharness.engine.expression.date;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator.TokenContext;
import io.github.molorane.pathora.testharness.util.PathoraClock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DateBoundaryTokenEvaluatorTest {

    private final DateBoundaryTokenEvaluator evaluator = new DateBoundaryTokenEvaluator();

    @BeforeEach
    void setUp() {
        // Freeze to 2026-10-15T12:00:00Z
        PathoraClock.freeze(Instant.parse("2026-10-15T12:00:00Z"));
    }

    @AfterEach
    void tearDown() {
        PathoraClock.reset();
    }

    @Test
    void testSupportedTokens() {
        Set<String> tokens = evaluator.supportedTokens();
        assertTrue(tokens.contains("START_OF_MONTH"));
        assertTrue(tokens.contains("END_OF_MONTH"));
        assertTrue(tokens.contains("FIRST_DAY_OF_NEXT_MONTH"));
        assertTrue(tokens.contains("START_OF_YEAR"));
        assertTrue(tokens.contains("END_OF_YEAR"));
    }

    @Test
    void testStartAndEndOfMonth() {
        assertEquals("2026-10-01", evaluator.evaluate(new TokenContext("START_OF_MONTH", null, null, false)));
        assertEquals("2026-10-31", evaluator.evaluate(new TokenContext("END_OF_MONTH", null, null, false)));
        assertEquals("2026-11-01", evaluator.evaluate(new TokenContext("FIRST_DAY_OF_NEXT_MONTH", null, null, false)));
    }

    @Test
    void testStartAndEndOfYear() {
        assertEquals("2026-01-01", evaluator.evaluate(new TokenContext("START_OF_YEAR", null, null, false)));
        assertEquals("2026-12-31", evaluator.evaluate(new TokenContext("END_OF_YEAR", null, null, false)));
    }

    @Test
    void testBoundaryWithOffsetsAndFormatting() {
        Object formatted = evaluator.evaluate(new TokenContext("START_OF_MONTH", "+5d", "dd/MM/yyyy", false));
        assertEquals("06/10/2026", formatted);
    }
}
