package io.github.molorane.pathora.testharness.engine.expression.base64;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Base64DecodeTokenEvaluatorTest {

    @Test
    @DisplayName("Should decode Base64 string to original text")
    void testBase64Decode() {
        assertEquals("admin:secret", ExpressionResolver.resolveToString("{{$BASE64_DECODE:YWRtaW46c2VjcmV0}}"));
        assertEquals("Hello World", ExpressionResolver.resolveToString("{{$BASE64_DECODE:SGVsbG8gV29ybGQ=}}"));
        assertEquals("", ExpressionResolver.resolveToString("{{$BASE64_DECODE:}}"));
    }
}
