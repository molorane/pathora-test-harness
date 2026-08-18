package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyEqualsWithToleranceEvaluatorTest {

    private MoneyEqualsWithToleranceEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new MoneyEqualsWithToleranceEvaluator();
    }

    @Test
    @DisplayName("PASS: monetary values equal within specified tolerance")
    void shouldPassWhenWithinTolerance() {
        Map<String, Object> config = Map.of("expected", "100.05", "tolerance", "0.01");

        assertThatNoException().isThrownBy(() -> evaluator.apply("$.amount", "100.06", config, true));
        assertThatNoException().isThrownBy(() -> evaluator.apply("$.amount", "100.04", config, true));
        assertThatNoException().isThrownBy(() -> evaluator.apply("$.amount", "100.05", config, true));
    }

    @Test
    @DisplayName("FAIL: monetary values exceed specified tolerance")
    void shouldFailWhenExceedsTolerance() {
        Map<String, Object> config = Map.of("expected", "100.05", "tolerance", "0.01");

        assertThatThrownBy(() -> evaluator.apply("$.amount", "100.07", config, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("MONEY_EQUALS_WITH_TOLERANCE failed");
    }

    @Test
    @DisplayName("FAIL: currency mismatch with tolerance")
    void shouldFailWhenCurrencyMismatches() {
        Map<String, Object> actual = Map.of("amount", "100.05", "currency", "USD");
        Map<String, Object> config = Map.of("expected", "100.05", "tolerance", "0.01", "currency", "EUR");

        assertThatThrownBy(() -> evaluator.apply("$.amount", actual, config, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("Currency mismatch");
    }
}
