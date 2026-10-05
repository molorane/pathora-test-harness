package io.github.molorane.pathora.testharness.engine.operator.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ListDoesNotContainEvaluatorTest {

    private ListDoesNotContainEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new ListDoesNotContainEvaluator();
    }

    @Test
    @DisplayName("PASS: list does not contain value")
    void shouldPassWhenNotContained() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", List.of("A", "B", "C"), "D", true));
    }

    @Test
    @DisplayName("FAIL: list contains value")
    void shouldFailWhenContained() {
        assertThatThrownBy(() -> operator.apply("$.items", List.of("A", "B", "C"), "B", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("LIST_DOES_NOT_CONTAIN failed");
    }
}

