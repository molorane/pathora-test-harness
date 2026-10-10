package io.github.molorane.pathora.testharness.engine.expression.hash;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HashMd5TokenEvaluatorTest {

    @Test
    @DisplayName("Should generate MD5 hex digest for input text")
    void testHashMd5() {
        assertEquals("5d41402abc4b2a76b9719d911017c592",
                ExpressionResolver.resolveToString("{{$HASH_MD5:hello}}"));
        assertEquals("d41d8cd98f00b204e9800998ecf8427e",
                ExpressionResolver.resolveToString("{{$HASH_MD5:}}"));
    }
}
