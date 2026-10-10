package io.github.molorane.pathora.testharness.engine.expression.hash;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HashSha256TokenEvaluatorTest {

    @Test
    @DisplayName("Should generate SHA-256 hex digest for input text")
    void testHashSha256() {
        assertEquals("2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824",
                ExpressionResolver.resolveToString("{{$HASH_SHA256:hello}}"));
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                ExpressionResolver.resolveToString("{{$HASH_SHA256:}}"));
    }
}
