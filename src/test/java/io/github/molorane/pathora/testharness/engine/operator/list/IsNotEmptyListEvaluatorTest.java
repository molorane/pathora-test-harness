package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsNotEmptyListEvaluatorTest {

    private IsNotEmptyListEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsNotEmptyListEvaluator();
    }

    @Test
    @DisplayName("PASS: list contains elements")
    void shouldPassWhenListIsNotEmpty() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", List.of("A", "B"), null, true));
    }

    @Test
    @DisplayName("FAIL: list is empty")
    void shouldFailWhenListIsEmpty() {
        assertThatThrownBy(() -> operator.apply("$.items", Collections.emptyList(), null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_NOT_EMPTY_LIST failed");
    }
}

