package za.co.pathora.testharness.engine.operator;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;


import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateAfterDurationOperatorTest {

        private DateAfterDurationOperator operator;

        @BeforeEach
        void setUp() {
                operator = new DateAfterDurationOperator();
        }

        private DocumentContext parse(String json) {
                return JsonPath.parse(json);
        }

       /**
         * ```json
         * { "Operator": "DATE_AFTER_DURATION", "Value": { "basePath": "$.outputData.applicationDate", "comparePath": "$.outputData.approvalDate", "amount": 3, "unit": "DAYS" } }
         * ```
         */
        @Test
        @DisplayName("PASS: approval is 5 days after application (min 3)")
        void shouldPassWhenSufficientDuration() {
                DocumentContext ctx = parse("""
                                {"outputData": {"applicationDate": "2026-01-01", "approvalDate": "2026-01-06"}}
                                """);
                Object value = TestJsonHelper.parse("""
                                {
                                  "basePath": "$.outputData.applicationDate",
                                  "comparePath": "$.outputData.approvalDate",
                                  "amount": 3,
                                  "unit": "DAYS"
                                }
                                """);
                assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
        }

       /**
         * ```json
         * { "Operator": "DATE_AFTER_DURATION", "Value": { "basePath": "$.outputData.applicationDate", "comparePath": "$.outputData.approvalDate", "amount": 3, "unit": "DAYS" } }
         * ```
         */
        @Test
        @DisplayName("PASS: exactly at threshold — 3 days")
        void shouldPassAtExactThreshold() {
                DocumentContext ctx = parse("""
                                {"outputData": {"applicationDate": "2026-01-01", "approvalDate": "2026-01-04"}}
                                """);
                Object value = TestJsonHelper.parse("""
                                {
                                  "basePath": "$.outputData.applicationDate",
                                  "comparePath": "$.outputData.approvalDate",
                                  "amount": 3,
                                  "unit": "DAYS"
                                }
                                """);
                assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
        }

       /**
         * ```json
         * { "Operator": "DATE_AFTER_DURATION", "Value": { "basePath": "$.outputData.start", "comparePath": "$.outputData.end", "amount": 3, "unit": "HOURS" } }
         * ```
         */
        @Test
        @DisplayName("PASS: datetime — 4 hours after base")
        void shouldPassWithDatetimeHours() {
                DocumentContext ctx = parse("""
                                {"outputData": {"start": "2026-01-01T10:00:00", "end": "2026-01-01T14:00:00"}}
                                """);
                Object value = TestJsonHelper.parse("""
                                {
                                  "basePath": "$.outputData.start",
                                  "comparePath": "$.outputData.end",
                                  "amount": 3,
                                  "unit": "HOURS"
                                }
                                """);
                assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
        }

       /**
         * ```json
         * { "Operator": "DATE_AFTER_DURATION", "Value": { "basePath": "$.outputData.applicationDate", "comparePath": "$.outputData.approvalDate", "amount": 3, "unit": "DAYS" } }
         * ```
         */
        @Test
        @DisplayName("FAIL: approval only 1 day after application (min 3)")
        void shouldFailWhenInsufficientDuration() {
                DocumentContext ctx = parse("""
                                {"outputData": {"applicationDate": "2026-01-01", "approvalDate": "2026-01-02"}}
                                """);
                Object value = TestJsonHelper.parse("""
                                {
                                  "basePath": "$.outputData.applicationDate",
                                  "comparePath": "$.outputData.approvalDate",
                                  "amount": 3,
                                  "unit": "DAYS"
                                }
                                """);
                assertThatThrownBy(() -> operator.apply(ctx, value))
                                .isInstanceOf(HarnessAssertionException.class)
                                .hasMessageContaining("DATE_AFTER_DURATION failed");
        }

       /**
         * ```json
         * { "Operator": "DATE_AFTER_DURATION", "Value": { "basePath": "$.outputData.applicationDate", "comparePath": "$.outputData.approvalDate", "amount": 3, "unit": "DAYS" } }
         * ```
         */
        @Test
        @DisplayName("FAIL: compare date before base date")
        void shouldFailWhenCompareBeforeBase() {
                DocumentContext ctx = parse("""
                                {"outputData": {"applicationDate": "2026-01-10", "approvalDate": "2026-01-05"}}
                                """);
                Object value = TestJsonHelper.parse("""
                                {
                                  "basePath": "$.outputData.applicationDate",
                                  "comparePath": "$.outputData.approvalDate",
                                  "amount": 3,
                                  "unit": "DAYS"
                                }
                                """);
                assertThatThrownBy(() -> operator.apply(ctx, value))
                                .isInstanceOf(HarnessAssertionException.class)
                                .hasMessageContaining("DATE_AFTER_DURATION failed");
        }

       /**
         * ```json
         * { "Operator": "DATE_AFTER_DURATION", "Value": { "basePath": "$.outputData.applicationDate", "comparePath": "$.outputData.approvalDate", "amount": 3, "unit": "DAYS" } }
         * ```
         */
        @Test
        @DisplayName("FAIL: invalid date format")
        void shouldFailWithInvalidDate() {
                DocumentContext ctx = parse("""
                                {"outputData": {"applicationDate": "not-a-date", "approvalDate": "2026-01-06"}}
                                """);
                Object value = TestJsonHelper.parse("""
                                {
                                  "basePath": "$.outputData.applicationDate",
                                  "comparePath": "$.outputData.approvalDate",
                                  "amount": 3,
                                  "unit": "DAYS"
                                }
                                """);
                assertThatThrownBy(() -> operator.apply(ctx, value))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessageContaining("Cannot parse date");
        }
}
