package io.github.molorane.pathora.testharness.config;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PathoraConfigLoaderTest {

    @Test
    @DisplayName("Should automatically load properties and YAML from classpath into system properties")
    void testClasspathConfigLoading() {
        PathoraConfigLoader.reset();
        PathoraConfigLoader.init();

        // Property loaded dynamically from classpath pathora.yml
        assertEquals("STAGING", ExpressionResolver.resolveToString("{{$SYS:pathora.test.env}}"));
        assertEquals("5000", ExpressionResolver.resolveToString("{{$SYS:pathora.test.timeout}}"));

        // Fallback for non-existent property
        assertEquals("default_app_name", ExpressionResolver.resolveToString("{{$SYS:pathora.nonexistent.app:default_app_name}}"));
    }
}
