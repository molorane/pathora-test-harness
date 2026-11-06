package io.github.molorane.pathora.testharness.engine.operator.duration;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.github.molorane.pathora.testharness.engine.operator.TestJsonHelper;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DurationEqualsEvaluatorTest {

    private DurationEqualsEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new DurationEqualsEvaluator();
    }

    private DocumentContext parse(String json) {
        return JsonPath.parse(json);
    }

    /**
     * ```json
     * { "Operator": "DURATION_EQUALS", "Value": { "startPath": "$.outputData.createdAt", "endPath": "$.outputData.processedAt", "unit": "MINUTES", "expected": 150 } }
     * ```
     */
    @Test
    @DisplayName("PASS: duration equals expected — 150 minutes")
    void shouldPassWhenEqual() {
        DocumentContext ctx = parse(
            """
                {"outputData": {"createdAt": "2026-01-01T10:00:00", "processedAt": "2026-01-01T12:30:00"}}
                """);
        Object value = TestJsonHelper.parse("""
            {
              "startPath": "$.outputData.createdAt",
              "endPath": "$.outputData.processedAt",
              "unit": "MINUTES",
              "expected": 150
            }
            """);
        assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
    }

    /**
     * ```json
     * { "Operator": "DURATION_EQUALS", "Value": { "startPath": "$.outputData.start", "endPath": "$.outputData.end", "unit": "DAYS", "expected": 5 } }
     * ```
     */
    @Test
    @DisplayName("PASS: duration equals expected — 5 days")
    void shouldPassWithDays() {
        DocumentContext ctx = parse("""
            {"outputData": {"start": "2026-01-01", "end": "2026-01-06"}}
            """);
        Object value = TestJsonHelper.parse("""
            {
              "startPath": "$.outputData.start",
              "endPath": "$.outputData.end",
              "unit": "DAYS",
              "expected": 5
            }
            """);
        assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
    }

    /**
     * ```json
     * { "Operator": "DURATION_EQUALS", "Value": { "startPath": "$.outputData.createdAt", "endPath": "$.outputData.processedAt", "unit": "MINUTES", "expected": 150 } }
     * ```
     */
    @Test
    @DisplayName("FAIL: duration does not match")
    void shouldFailWhenNotEqual() {
        DocumentContext ctx = parse(
            """
                {"outputData": {"createdAt": "2026-01-01T10:00:00", "processedAt": "2026-01-01T12:00:00"}}
                """);
        Object value = TestJsonHelper.parse("""
            {
              "startPath": "$.outputData.createdAt",
              "endPath": "$.outputData.processedAt",
              "unit": "MINUTES",
              "expected": 150
            }
            """);
        assertThatThrownBy(() -> operator.apply(ctx, value))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("DURATION_EQUALS failed")
            .hasMessageContaining("120");
    }

    @Test
    @DisplayName("PASS: legacy 'value' fallback when duration equals expected")
    void shouldPassWithLegacyValueFallback() {
        DocumentContext ctx = parse("""
            {"outputData": {"start": "2026-01-01", "end": "2026-01-06"}}
            """);
        Object value = TestJsonHelper.parse("""
            {
              "startPath": "$.outputData.start",
              "endPath": "$.outputData.end",
              "unit": "DAYS",
              "value": 5
            }
            """);
        assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
    }

    @Test
    @DisplayName("FAIL: missing 'expected' key")
    void shouldFailWhenExpectedKeyIsMissing() {
        DocumentContext ctx = parse("""
            {"outputData": {"start": "2026-01-01", "end": "2026-01-06"}}
            """);
        Object value = TestJsonHelper.parse("""
            {
              "startPath": "$.outputData.start",
              "endPath": "$.outputData.end",
              "unit": "DAYS"
            }
            """);
        assertThatThrownBy(() -> operator.apply(ctx, value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("'expected'");
    }
}
