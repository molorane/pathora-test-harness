package io.github.molorane.pathora.testharness.engine.expression.env;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnvTokenEvaluatorTest {

    @Test
    @DisplayName("Should resolve environment variable or fallback to default value")
    void testEnvResolution() {
        String path = ExpressionResolver.resolveToString("{{$ENV:PATH}}");
        assertNotNull(path);
        assertFalse(path.isBlank());

        assertEquals("fallback_val", ExpressionResolver.resolveToString("{{$ENV:PATHORA_NON_EXISTENT_VAR:fallback_val}}"));
        assertEquals("", ExpressionResolver.resolveToString("{{$ENV:PATHORA_NON_EXISTENT_VAR}}"));
    }
}
