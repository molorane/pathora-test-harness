package io.github.molorane.pathora.testharness.engine.operator.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ListIsSortedDescEvaluatorTest {

    private ListIsSortedDescEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new ListIsSortedDescEvaluator();
    }

    @Test
    @DisplayName("PASS: list is sorted in descending order")
    void shouldPassWhenSortedDesc() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.numbers", List.of(5, 4, 3, 2, 1), null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.names", List.of("Charlie", "Bob", "Alice"), null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.single", List.of(1), null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.empty", Collections.emptyList(), null, true));
    }

    @Test
    @DisplayName("FAIL: list is not sorted descending")
    void shouldFailWhenNotSortedDesc() {
        assertThatThrownBy(() -> operator.apply("$.numbers", List.of(5, 2, 3), null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("LIST_IS_SORTED_DESC failed");
    }
}

