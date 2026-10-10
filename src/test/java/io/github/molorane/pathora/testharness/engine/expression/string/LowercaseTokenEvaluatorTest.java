package io.github.molorane.pathora.testharness.engine.expression.string;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LowercaseTokenEvaluatorTest {

    @Test
    @DisplayName("Should convert input string to lowercase")
    void testLowercase() {
        assertEquals("hello world", ExpressionResolver.resolveToString("{{$LOWERCASE:HELLO WORLD}}"));
        assertEquals("pathora_enterprise", ExpressionResolver.resolveToString("{{$LOWERCASE:Pathora_Enterprise}}"));
        assertEquals("", ExpressionResolver.resolveToString("{{$LOWERCASE:}}"));
    }
}
