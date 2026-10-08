package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringLengthBetweenEvaluatorTest {

    private StringLengthBetweenEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new StringLengthBetweenEvaluator();
    }

    @Test
    @DisplayName("PASS: string length is within bounds")
    void shouldPassWhenLengthWithinBounds() {
        assertThatNoException().isThrownBy(() ->
            operator.apply("$.code", "ABCDEF", Map.of("min", 3, "max", 10), true));
    }

    @Test
    @DisplayName("FAIL: string length is outside bounds")
    void shouldFailWhenLengthOutsideBounds() {
        assertThatThrownBy(() ->
            operator.apply("$.code", "AB", Map.of("min", 3, "max", 10), true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("STRING_LENGTH_BETWEEN failed");
    }
}

