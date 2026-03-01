package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateAfterNowOperatorTest {

    private DateAfterNowOperator operator;

    @BeforeEach
    void setUp() {
        operator = new DateAfterNowOperator();
    }

    @Test
    @DisplayName("PASS: future date is after now")
    void shouldPassWithFutureDate() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.expiry", "2099-12-31", null, true));
    }

    @Test
    @DisplayName("PASS: future datetime is after now")
    void shouldPassWithFutureDatetime() {
        String future = LocalDateTime.now().plusHours(1)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        assertThatNoException().isThrownBy(() -> operator.apply("$.expiry", future, null, true));
    }

    @Test
    @DisplayName("FAIL: past date is not after now")
    void shouldFailWithPastDate() {
        assertThatThrownBy(() -> operator.apply("$.expiry", "2020-01-01", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATE_AFTER_NOW failed");
    }
}
