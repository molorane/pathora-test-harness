package io.github.molorane.pathora.testharness.engine.expression.url;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UrlEncodeTokenEvaluatorTest {

    @Test
    @DisplayName("Should URL encode string parameters")
    void testUrlEncode() {
        assertEquals("hello+world%26foo%3Dbar", ExpressionResolver.resolveToString("{{$URL_ENCODE:hello world&foo=bar}}"));
        assertEquals("user%40example.com", ExpressionResolver.resolveToString("{{$URL_ENCODE:user@example.com}}"));
        assertEquals("", ExpressionResolver.resolveToString("{{$URL_ENCODE:}}"));
    }
}
