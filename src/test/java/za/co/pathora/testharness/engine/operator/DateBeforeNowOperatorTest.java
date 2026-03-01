package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateBeforeNowOperatorTest {

    private DateBeforeNowOperator operator;

    @BeforeEach
    void setUp() {
        operator = new DateBeforeNowOperator();
    }

    @Test
    @DisplayName("PASS: past date is before now")
    void shouldPassWithPastDate() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.expiry", "2020-01-01", null, true));
    }

    @Test
    @DisplayName("PASS: past datetime is before now")
    void shouldPassWithPastDatetime() {
        String past = LocalDateTime.now().minusHours(1)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        assertThatNoException().isThrownBy(() -> operator.apply("$.expiry", past, null, true));
    }

    @Test
    @DisplayName("FAIL: future date is not before now")
    void shouldFailWithFutureDate() {
        assertThatThrownBy(() -> operator.apply("$.expiry", "2099-12-31", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATE_BEFORE_NOW failed");
    }
}
