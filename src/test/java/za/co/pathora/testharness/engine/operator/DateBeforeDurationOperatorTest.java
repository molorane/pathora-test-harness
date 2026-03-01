package za.co.pathora.testharness.engine.operator;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;


import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateBeforeDurationOperatorTest {

        private DateBeforeDurationOperator operator;

        @BeforeEach
        void setUp() {
                operator = new DateBeforeDurationOperator();
        }

        private DocumentContext parse(String json) {
                return JsonPath.parse(json);
        }

        @Test
        @DisplayName("PASS: compare date is before base + 5 days")
        void shouldPassWhenBefore() {
                DocumentContext ctx = parse("""
                                {"outputData": {"applicationDate": "2026-01-01", "approvalDate": "2026-01-03"}}
                                """);
                Object value = TestJsonHelper.parse("""
                                {
                                  "basePath": "$.outputData.applicationDate",
                                  "comparePath": "$.outputData.approvalDate",
                                  "amount": 5,
                                  "unit": "DAYS"
                                }
                                """);
                assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
        }

        @Test
        @DisplayName("FAIL: compare date is after base + 3 days")
        void shouldFailWhenAfter() {
                DocumentContext ctx = parse("""
                                {"outputData": {"applicationDate": "2026-01-01", "approvalDate": "2026-01-10"}}
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
                                .hasMessageContaining("DATE_BEFORE_DURATION failed");
        }

        @Test
        @DisplayName("FAIL: compare date equals threshold (not strictly before)")
        void shouldFailWhenEqualToThreshold() {
                DocumentContext ctx = parse("""
                                {"outputData": {"start": "2026-01-01", "end": "2026-01-04"}}
                                """);
                Object value = TestJsonHelper.parse("""
                                {
                                  "basePath": "$.outputData.start",
                                  "comparePath": "$.outputData.end",
                                  "amount": 3,
                                  "unit": "DAYS"
                                }
                                """);
                assertThatThrownBy(() -> operator.apply(ctx, value))
                                .isInstanceOf(HarnessAssertionException.class)
                                .hasMessageContaining("DATE_BEFORE_DURATION failed");
        }
}
