package io.github.molorane.pathora.testharness.engine.expression.random;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomDecimalTokenEvaluatorTest {

    @Test
    @DisplayName("Should resolve random decimal within bounds and scale")
    void testRandomDecimal() {
        Object resolved = ExpressionResolver.resolve("{{$RANDOM_DECIMAL:10.00:500.00:2}}");
        assertNotNull(resolved);
        assertTrue(resolved instanceof Number);

        double val = ((Number) resolved).doubleValue();
        assertTrue(val >= 10.00 && val <= 500.00, "Value " + val + " must be between 10.00 and 500.00");
    }

    @Test
    @DisplayName("Should format fixed decimal to exact scale in string context")
    void testFixedDecimalInStringContext() {
        assertEquals("Amount: 42.50", ExpressionResolver.resolveToString("Amount: {{$RANDOM_DECIMAL:42.50:42.50:2}}"));
    }
}
