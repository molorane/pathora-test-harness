package io.github.molorane.pathora.testharness.engine.expression.string;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UppercaseTokenEvaluatorTest {

    @Test
    @DisplayName("Should convert input string to uppercase")
    void testUppercase() {
        assertEquals("HELLO WORLD", ExpressionResolver.resolveToString("{{$UPPERCASE:hello world}}"));
        assertEquals("PATHORA_ENTERPRISE", ExpressionResolver.resolveToString("{{$UPPERCASE:Pathora_Enterprise}}"));
        assertEquals("", ExpressionResolver.resolveToString("{{$UPPERCASE:}}"));
    }
}
