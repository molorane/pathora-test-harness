package za.co.pathora.testharness.engine.operator;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;


import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DurationBetweenDatesOperatorTest {

        private DurationBetweenDatesOperator operator;

        @BeforeEach
        void setUp() {
                operator = new DurationBetweenDatesOperator();
        }

        private DocumentContext parse(String json) {
                return JsonPath.parse(json);
        }

       /**
         * ```json
         * { "Operator": "DURATION_BETWEEN_DATES", "Value": { "startPath": "$.outputData.applicationDate", "endPath": "$.outputData.approvalDate", "unit": "DAYS", "min": 0, "max": 7 } }
         * ```
         */
        @Test
        @DisplayName("PASS: 5 days within 0-7 range")
        void shouldPassWhenWithinRange() {
                DocumentContext ctx = parse("""
                                {"outputData": {"applicationDate": "2026-01-01", "approvalDate": "2026-01-06"}}
                                """);
                Object value = TestJsonHelper.parse("""
                                {
                                  "startPath": "$.outputData.applicationDate",
                                  "endPath": "$.outputData.approvalDate",
                                  "unit": "DAYS",
                                  "min": 0,
                                  "max": 7
                                }
                                """);
                assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
        }

       /**
         * ```json
         * { "Operator": "DURATION_BETWEEN_DATES", "Value": { "startPath": "$.outputData.start", "endPath": "$.outputData.end", "unit": "DAYS", "min": 0, "max": 7 } }
         * ```
         */
        @Test
        @DisplayName("PASS: at min boundary")
        void shouldPassAtMin() {
                DocumentContext ctx = parse("""
                                {"outputData": {"start": "2026-01-01", "end": "2026-01-01"}}
                                """);
                Object value = TestJsonHelper.parse("""
                                {
                                  "startPath": "$.outputData.start",
                                  "endPath": "$.outputData.end",
                                  "unit": "DAYS",
                                  "min": 0,
                                  "max": 7
                                }
                                """);
                assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
        }

       /**
         * ```json
         * { "Operator": "DURATION_BETWEEN_DATES", "Value": { "startPath": "$.outputData.start", "endPath": "$.outputData.end", "unit": "DAYS", "min": 0, "max": 7 } }
         * ```
         */
        @Test
        @DisplayName("FAIL: 10 days exceeds max 7")
        void shouldFailWhenExceedsMax() {
                DocumentContext ctx = parse("""
                                {"outputData": {"start": "2026-01-01", "end": "2026-01-11"}}
                                """);
                Object value = TestJsonHelper.parse("""
                                {
                                  "startPath": "$.outputData.start",
                                  "endPath": "$.outputData.end",
                                  "unit": "DAYS",
                                  "min": 0,
                                  "max": 7
                                }
                                """);
                assertThatThrownBy(() -> operator.apply(ctx, value))
                                .isInstanceOf(HarnessAssertionException.class)
                                .hasMessageContaining("DURATION_BETWEEN failed");
        }
}
