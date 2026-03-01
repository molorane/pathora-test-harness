package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateWithinNextOperatorTest {

    private DateWithinNextOperator operator;

    @BeforeEach
    void setUp() {
        operator = new DateWithinNextOperator();
    }

    @Test
    @DisplayName("PASS: future date within next 7 days")
    void shouldPassWhenWithinNext() {
        String future = LocalDateTime.now().plusDays(3)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Object value = TestJsonHelper.parse("""
                {
                  "amount": 7,
                  "unit": "DAYS"
                }
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.expiry", future, value, true));
    }

    @Test
    @DisplayName("PASS: future datetime within next 24 hours")
    void shouldPassWithinNext24Hours() {
        String future = LocalDateTime.now().plusHours(5)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Object value = TestJsonHelper.parse("""
                {
                  "amount": 24,
                  "unit": "HOURS"
                }
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.expiry", future, value, true));
    }

    @Test
    @DisplayName("FAIL: date too far in the future")
    void shouldFailWhenTooFarInFuture() {
        String farFuture = LocalDateTime.now().plusDays(30)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Object value = TestJsonHelper.parse("""
                {
                  "amount": 7,
                  "unit": "DAYS"
                }
                """);
        assertThatThrownBy(() -> operator.apply("$.expiry", farFuture, value, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATE_WITHIN_NEXT failed");
    }

    @Test
    @DisplayName("FAIL: date in the past")
    void shouldFailWhenInPast() {
        String past = LocalDateTime.now().minusDays(1)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Object value = TestJsonHelper.parse("""
                {
                  "amount": 7,
                  "unit": "DAYS"
                }
                """);
        assertThatThrownBy(() -> operator.apply("$.expiry", past, value, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATE_WITHIN_NEXT failed");
    }
}
