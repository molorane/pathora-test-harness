package io.github.molorane.pathora.testharness.engine.operator.scalar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsNullEvaluatorTest {

    private IsNullEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsNullEvaluator();
    }

    @Test
    @DisplayName("PASS: actual is null")
    void shouldPassWhenActualIsNull() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", null, null, true));
    }

    @Test
    @DisplayName("PASS: actual is empty list")
    void shouldPassWhenActualIsEmptyList() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", Collections.emptyList(), null, true));
    }

    @Test
    @DisplayName("FAIL: actual is non-null")
    void shouldFailWhenActualIsNotNull() {
        assertThatThrownBy(() -> operator.apply("$.field", "hello", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_NULL failed");
    }

    @Test
    @DisplayName("FAIL: actual is non-empty list with value")
    void shouldFailWhenActualIsNonEmptyList() {
        assertThatThrownBy(() -> operator.apply("$.field", List.of("value"), null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_NULL failed");
    }
}

