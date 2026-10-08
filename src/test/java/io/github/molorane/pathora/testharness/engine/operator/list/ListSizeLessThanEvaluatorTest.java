package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ListSizeLessThanEvaluatorTest {

    private ListSizeLessThanEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new ListSizeLessThanEvaluator();
    }

    @Test
    @DisplayName("PASS: list size is strictly less than expected")
    void shouldPassWhenSizeLessThan() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", List.of("A"), 2, true));
    }

    @Test
    @DisplayName("FAIL: list size is greater than or equal to expected")
    void shouldFailWhenSizeNotLessThan() {
        assertThatThrownBy(() -> operator.apply("$.items", List.of("A", "B"), 2, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("LIST_SIZE_LESS_THAN failed");
    }
}

