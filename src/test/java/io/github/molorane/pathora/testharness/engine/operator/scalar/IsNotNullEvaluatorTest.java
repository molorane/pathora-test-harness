package io.github.molorane.pathora.testharness.engine.operator.scalar;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsNotNullEvaluatorTest {

    private IsNotNullEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsNotNullEvaluator();
    }

    @Test
    @DisplayName("PASS: actual is not null")
    void shouldPassWhenActualIsNotNull() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", "value", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", 123, null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", List.of("item"), null, true));
    }

    @Test
    @DisplayName("FAIL: actual is null")
    void shouldFailWhenActualIsNull() {
        assertThatThrownBy(() -> operator.apply("$.field", null, null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_NOT_NULL failed");
    }

    @Test
    @DisplayName("FAIL: actual is empty list (treated as null/no match)")
    void shouldFailWhenActualIsEmptyList() {
        assertThatThrownBy(() -> operator.apply("$.field", Collections.emptyList(), null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_NOT_NULL failed");
    }
}

