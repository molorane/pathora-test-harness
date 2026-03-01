package za.co.pathora.testharness.engine.operator;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;


import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DurationGreaterThanOperatorTest {

        private DurationGreaterThanOperator operator;

        @BeforeEach
        void setUp() {
                operator = new DurationGreaterThanOperator();
        }

        private DocumentContext parse(String json) {
                return JsonPath.parse(json);
        }

       /**
         * ```json
         * { "Operator": "DURATION_GREATER_THAN", "Value": { "startPath": "$.outputData.start", "endPath": "$.outputData.end", "unit": "HOURS", "value": 2 } }
         * ```
         */
        @Test
        @DisplayName("PASS: duration > threshold")
        void shouldPassWhenGreater() {
                DocumentContext ctx = parse("""
                                {"outputData": {"start": "2026-01-01T10:00:00", "end": "2026-01-01T14:00:00"}}
                                """);
                Object value = TestJsonHelper.parse("""
                                {
                                  "startPath": "$.outputData.start",
                                  "endPath": "$.outputData.end",
                                  "unit": "HOURS",
                                  "value": 2
                                }
                                """);
                assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
        }

       /**
         * ```json
         * { "Operator": "DURATION_GREATER_THAN", "Value": { "startPath": "$.outputData.start", "endPath": "$.outputData.end", "unit": "HOURS", "value": 2 } }
         * ```
         */
        @Test
        @DisplayName("FAIL: duration equals threshold (not strictly greater)")
        void shouldFailWhenEqual() {
                DocumentContext ctx = parse("""
                                {"outputData": {"start": "2026-01-01T10:00:00", "end": "2026-01-01T12:00:00"}}
                                """);
                Object value = TestJsonHelper.parse("""
                                {
                                  "startPath": "$.outputData.start",
                                  "endPath": "$.outputData.end",
                                  "unit": "HOURS",
                                  "value": 2
                                }
                                """);
                assertThatThrownBy(() -> operator.apply(ctx, value))
                                .isInstanceOf(HarnessAssertionException.class)
                                .hasMessageContaining("DURATION_GREATER_THAN failed");
        }

       /**
         * ```json
         * { "Operator": "DURATION_GREATER_THAN", "Value": { "startPath": "$.outputData.start", "endPath": "$.outputData.end", "unit": "HOURS", "value": 2 } }
         * ```
         */
        @Test
        @DisplayName("FAIL: duration < threshold")
        void shouldFailWhenLess() {
                DocumentContext ctx = parse("""
                                {"outputData": {"start": "2026-01-01T10:00:00", "end": "2026-01-01T11:00:00"}}
                                """);
                Object value = TestJsonHelper.parse("""
                                {
                                  "startPath": "$.outputData.start",
                                  "endPath": "$.outputData.end",
                                  "unit": "HOURS",
                                  "value": 2
                                }
                                """);
                assertThatThrownBy(() -> operator.apply(ctx, value))
                                .isInstanceOf(HarnessAssertionException.class)
                                .hasMessageContaining("DURATION_GREATER_THAN failed");
        }
}
