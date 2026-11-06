package io.github.molorane.pathora.testharness.engine.operator.scalar;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EqualsWithToleranceEvaluatorTest {

    private EqualsWithToleranceEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new EqualsWithToleranceEvaluator();
    }

    @Test
    @DisplayName("PASS: value within tolerance")
    void shouldPassWhenWithinTolerance() {
        assertThatNoException().isThrownBy(() ->
            operator.apply("$.val", 100.05, Map.of("expected", 100.0, "tolerance", 0.1), true));
    }

    @Test
    @DisplayName("FAIL: value outside tolerance")
    void shouldFailWhenOutsideTolerance() {
        assertThatThrownBy(() ->
            operator.apply("$.val", 100.25, Map.of("expected", 100.0, "tolerance", 0.1), true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("EQUALS_WITH_TOLERANCE failed");
    }

    @Test
    @DisplayName("PASS: legacy 'value' fallback within tolerance")
    void shouldPassWithLegacyValueFallback() {
        assertThatNoException().isThrownBy(() ->
            operator.apply("$.val", 100.05, Map.of("value", 100.0, "tolerance", 0.1), true));
    }

    @Test
    @DisplayName("FAIL: missing 'expected' key")
    void shouldFailWhenExpectedKeyIsMissing() {
        assertThatThrownBy(() ->
            operator.apply("$.val", 100.05, Map.of("tolerance", 0.1), true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("'expected'");
    }

    @Test
    @DisplayName("PASS: string actual within tolerance")
    void shouldPassWithStringActual() {
        assertThatNoException().isThrownBy(() ->
            operator.apply("$.val", "1085", Map.of("expected", 1085.0, "tolerance", 0.1), true));
    }
}
