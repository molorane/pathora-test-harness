package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyEqualsEvaluatorTest {

    private MoneyEqualsEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new MoneyEqualsEvaluator();
    }

    @Test
    @DisplayName("PASS: money equals irrespective of trailing scale")
    void shouldPassWhenMoneyAmountsAreEqual() {
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.amount", new BigDecimal("100.50"), "100.5", true));
    }

    @Test
    @DisplayName("PASS: money maps with currency match")
    void shouldPassWhenMoneyMapsMatch() {
        Map<String, Object> actual = Map.of("amount", 100.50, "currency", "USD");
        Map<String, Object> expected = Map.of("amount", "100.50", "currency", "USD");

        assertThatNoException().isThrownBy(() -> operator.apply("$.amount", actual, expected, true));
    }

    @Test
    @DisplayName("FAIL: money amounts differ")
    void shouldFailWhenMoneyAmountsDiffer() {
        assertThatThrownBy(() -> operator.apply("$.amount", 100.50, 100.51, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("MONEY_EQUALS failed");
    }

    @Test
    @DisplayName("FAIL: currency mismatch")
    void shouldFailWhenCurrenciesMismatch() {
        Map<String, Object> actual = Map.of("amount", 100.50, "currency", "USD");
        Map<String, Object> expected = Map.of("amount", 100.50, "currency", "EUR");

        assertThatThrownBy(() -> operator.apply("$.amount", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("Currency mismatch");
    }
}
