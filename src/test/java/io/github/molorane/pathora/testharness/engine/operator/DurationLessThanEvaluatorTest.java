package io.github.molorane.pathora.testharness.engine.operator;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DurationLessThanEvaluatorTest {

    private DurationLessThanEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new DurationLessThanEvaluator();
    }

    private DocumentContext parse(String json) {
        return JsonPath.parse(json);
    }

    /**
     * ```json
     * { "Operator": "DURATION_LESS_THAN", "Value": { "startPath": "$.outputData.start", "endPath": "$.outputData.end", "unit": "MINUTES", "value": 180 } }
     * ```
     */
    @Test
    @DisplayName("PASS: duration < threshold")
    void shouldPassWhenLess() {
        DocumentContext ctx = parse("""
                {"outputData": {"start": "2026-01-01T10:00:00", "end": "2026-01-01T12:00:00"}}
                """);
        Object value = TestJsonHelper.parse("""
                {
                  "startPath": "$.outputData.start",
                  "endPath": "$.outputData.end",
                  "unit": "MINUTES",
                  "value": 180
                }
                """);
        assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
    }

    /**
     * ```json
     * { "Operator": "DURATION_LESS_THAN", "Value": { "startPath": "$.outputData.start", "endPath": "$.outputData.end", "unit": "MINUTES", "value": 180 } }
     * ```
     */
    @Test
    @DisplayName("FAIL: duration equals threshold (not strictly less)")
    void shouldFailWhenEqual() {
        DocumentContext ctx = parse("""
                {"outputData": {"start": "2026-01-01T10:00:00", "end": "2026-01-01T13:00:00"}}
                """);
        Object value = TestJsonHelper.parse("""
                {
                  "startPath": "$.outputData.start",
                  "endPath": "$.outputData.end",
                  "unit": "MINUTES",
                  "value": 180
                }
                """);
        assertThatThrownBy(() -> operator.apply(ctx, value))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DURATION_LESS_THAN failed");
    }

    /**
     * ```json
     * { "Operator": "DURATION_LESS_THAN", "Value": { "startPath": "$.outputData.start", "endPath": "$.outputData.end", "unit": "MINUTES", "value": 180 } }
     * ```
     */
    @Test
    @DisplayName("FAIL: duration > threshold")
    void shouldFailWhenGreater() {
        DocumentContext ctx = parse("""
                {"outputData": {"start": "2026-01-01T10:00:00", "end": "2026-01-01T14:00:00"}}
                """);
        Object value = TestJsonHelper.parse("""
                {
                  "startPath": "$.outputData.start",
                  "endPath": "$.outputData.end",
                  "unit": "MINUTES",
                  "value": 180
                }
                """);
        assertThatThrownBy(() -> operator.apply(ctx, value))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DURATION_LESS_THAN failed");
    }
}
