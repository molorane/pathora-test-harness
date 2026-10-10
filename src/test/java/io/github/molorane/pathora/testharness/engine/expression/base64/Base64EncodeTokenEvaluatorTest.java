package io.github.molorane.pathora.testharness.engine.expression.base64;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Base64EncodeTokenEvaluatorTest {

    @Test
    @DisplayName("Should encode string to Base64")
    void testBase64Encode() {
        assertEquals("YWRtaW46c2VjcmV0", ExpressionResolver.resolveToString("{{$BASE64_ENCODE:admin:secret}}"));
        assertEquals("SGVsbG8gV29ybGQ=", ExpressionResolver.resolveToString("{{$BASE64_ENCODE:Hello World}}"));
        assertEquals("", ExpressionResolver.resolveToString("{{$BASE64_ENCODE:}}"));
    }
}
