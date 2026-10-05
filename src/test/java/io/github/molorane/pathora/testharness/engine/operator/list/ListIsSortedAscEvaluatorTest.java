package io.github.molorane.pathora.testharness.engine.operator.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ListIsSortedAscEvaluatorTest {

    private ListIsSortedAscEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new ListIsSortedAscEvaluator();
    }

    @Test
    @DisplayName("PASS: list is sorted in ascending order")
    void shouldPassWhenSortedAsc() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.numbers", List.of(1, 2, 3, 4, 5), null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.names", List.of("Alice", "Bob", "Charlie"), null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.single", List.of(1), null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.empty", Collections.emptyList(), null, true));
    }

    @Test
    @DisplayName("FAIL: list is not sorted ascending")
    void shouldFailWhenNotSortedAsc() {
        assertThatThrownBy(() -> operator.apply("$.numbers", List.of(1, 3, 2), null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("LIST_IS_SORTED_ASC failed");
    }
}

