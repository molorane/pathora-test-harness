package io.github.molorane.pathora.testharness.engine.operator.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ListSizeBetweenEvaluatorTest {

    private ListSizeBetweenEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new ListSizeBetweenEvaluator();
    }

    @Test
    @DisplayName("PASS: list size is within bounds")
    void shouldPassWhenSizeWithinBounds() {
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.items", List.of("A", "B", "C"), Map.of("min", 2, "max", 5), true));
    }

    @Test
    @DisplayName("FAIL: list size is outside bounds")
    void shouldFailWhenSizeOutsideBounds() {
        assertThatThrownBy(() ->
                operator.apply("$.items", List.of("A"), Map.of("min", 2, "max", 5), true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("LIST_SIZE_BETWEEN failed");
    }
}

