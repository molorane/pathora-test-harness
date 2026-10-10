package io.github.molorane.pathora.testharness.engine.expression.math;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MathTokenEvaluatorTest {

    @Test
    @DisplayName("Should evaluate arithmetic expressions correctly")
    void testMathEvaluation() {
        assertEquals("115", ExpressionResolver.resolveToString("{{$MATH: 100 * 1.15}}"));
        assertEquals("35", ExpressionResolver.resolveToString("{{$MATH: (50 + 20) / 2}}"));
        assertEquals("20", ExpressionResolver.resolveToString("{{$MATH: 10 + 2.5 * 4}}"));
        assertEquals("8", ExpressionResolver.resolveToString("{{$MATH: 2 ^ 3}}"));
        assertEquals("1", ExpressionResolver.resolveToString("{{$MATH: 10 % 3}}"));
    }

    @Test
    @DisplayName("Should return numeric scalar when allowNumeric is true")
    void testNumericReturn() {
        Object resolvedInt = ExpressionResolver.resolve("{{$MATH: 100 * 1.15}}");
        assertTrue(resolvedInt instanceof Number);
        assertEquals(115L, ((Number) resolvedInt).longValue());

        Object resolvedDec = ExpressionResolver.resolve("{{$MATH: 10.5 + 2.25}}");
        assertTrue(resolvedDec instanceof Number);
        assertEquals(12.75, ((Number) resolvedDec).doubleValue(), 0.0001);
    }
}
