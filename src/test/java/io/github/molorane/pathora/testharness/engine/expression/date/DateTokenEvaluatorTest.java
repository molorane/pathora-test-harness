package io.github.molorane.pathora.testharness.engine.expression.date;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator.TokenContext;
import io.github.molorane.pathora.testharness.util.PathoraClock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DateTokenEvaluatorTest {

    private final DateTokenEvaluator evaluator = new DateTokenEvaluator();

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
        assertTrue(tokens.contains("CURRENT_DATE"));
        assertTrue(tokens.contains("TODAY"));
    }

    @Test
    void testEvaluateBaseDate() {
        Object date = evaluator.evaluate(new TokenContext("CURRENT_DATE", null, null, false));
        assertEquals("2026-10-07", date);
    }

    @Test
    void testEvaluateWithOffset() {
        Object datePlus30 = evaluator.evaluate(new TokenContext("TODAY", "+30d", null, false));
        assertEquals("2026-11-06", datePlus30);

        Object dateMinus25y = evaluator.evaluate(new TokenContext("CURRENT_DATE", "-25y", null, false));
        assertEquals("2001-10-07", dateMinus25y);
    }

    @Test
    void testEvaluateWithCustomFormatPattern() {
        Object formatted = evaluator.evaluate(new TokenContext("CURRENT_DATE", "+5d", "dd/MM/yyyy", false));
        assertEquals("12/10/2026", formatted);
    }
}
