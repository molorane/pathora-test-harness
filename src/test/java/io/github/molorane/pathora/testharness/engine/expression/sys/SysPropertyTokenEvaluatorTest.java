package io.github.molorane.pathora.testharness.engine.expression.sys;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysPropertyTokenEvaluatorTest {

    @Test
    @DisplayName("Should resolve JVM system property or fallback to default value")
    void testSysPropertyResolution() {
        String javaVersion = ExpressionResolver.resolveToString("{{$SYS:java.version}}");
        assertNotNull(javaVersion);
        assertFalse(javaVersion.isBlank());

        System.setProperty("pathora.test.prop", "custom_sys_value");
        try {
            assertEquals("custom_sys_value", ExpressionResolver.resolveToString("{{$SYS:pathora.test.prop}}"));
            assertEquals("custom_sys_value", ExpressionResolver.resolveToString("{{$SYS:pathora.test.prop:fallback}}"));
            assertEquals("custom_sys_value", ExpressionResolver.resolveToString("{{$PROP:pathora.test.prop}}"));
            assertEquals("custom_sys_value", ExpressionResolver.resolveToString("{{$PATHORA:pathora.test.prop}}"));
            assertEquals("custom_sys_value", ExpressionResolver.resolveToString("{{$CONFIG:pathora.test.prop}}"));
        } finally {
            System.clearProperty("pathora.test.prop");
        }

        assertEquals("default_sys_val", ExpressionResolver.resolveToString("{{$SYS:non.existent.prop:default_sys_val}}"));
        assertEquals("", ExpressionResolver.resolveToString("{{$SYS:non.existent.prop}}"));
    }
}
