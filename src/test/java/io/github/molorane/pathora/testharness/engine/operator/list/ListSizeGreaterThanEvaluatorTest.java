package io.github.molorane.pathora.testharness.engine.operator.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ListSizeGreaterThanEvaluatorTest {

    private ListSizeGreaterThanEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new ListSizeGreaterThanEvaluator();
    }

    @Test
    @DisplayName("PASS: list size is strictly greater than expected")
    void shouldPassWhenSizeGreaterThan() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", List.of("A", "B", "C"), 2, true));
    }

    @Test
    @DisplayName("FAIL: list size is less than or equal to expected")
    void shouldFailWhenSizeNotGreaterThan() {
        assertThatThrownBy(() -> operator.apply("$.items", List.of("A", "B"), 2, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("LIST_SIZE_GREATER_THAN failed");
    }
}

