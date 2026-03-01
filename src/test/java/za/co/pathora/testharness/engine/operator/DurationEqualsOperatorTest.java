package za.co.pathora.testharness.engine.operator;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;


import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DurationEqualsOperatorTest {

        private DurationEqualsOperator operator;

        @BeforeEach
        void setUp() {
                operator = new DurationEqualsOperator();
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
}
