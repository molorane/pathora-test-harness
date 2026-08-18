package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyBetweenEvaluatorTest {

    private MoneyBetweenEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new MoneyBetweenEvaluator();
    }

    @Test
    @DisplayName("PASS: money amount within min and max")
    void shouldPassWhenWithinRange() {
        Map<String, Object> range = Map.of("min", "100.00", "max", "200.00");

        assertThatNoException().isThrownBy(() -> operator.apply("$.amount", "150.50", range, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.amount", "100.00", range, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.amount", "200.00", range, true));
    }

    @Test
    @DisplayName("FAIL: money amount outside min and max")
    void shouldFailWhenOutsideRange() {
        Map<String, Object> range = Map.of("min", "100.00", "max", "200.00");

        assertThatThrownBy(() -> operator.apply("$.amount", "99.99", range, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("MONEY_BETWEEN failed");

        assertThatThrownBy(() -> operator.apply("$.amount", "200.01", range, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("MONEY_BETWEEN failed");
    }
}
