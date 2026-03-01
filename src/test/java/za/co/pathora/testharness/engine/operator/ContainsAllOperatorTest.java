package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContainsAllOperatorTest {

    private ContainsAllOperator operator;

    @BeforeEach
    void setUp() {
        operator = new ContainsAllOperator();
    }

    @Test
    @DisplayName("PASS: array contains all expected values")
    void shouldPassWhenAllFound() {
        List<String> actual = Arrays.asList("1004", "1011", "1020");
        List<String> expected = Arrays.asList("1004", "1011");
        assertThatNoException().isThrownBy(() -> operator.apply("$.codes", actual, expected, true));
    }

    @Test
    @DisplayName("PASS: exact match")
    void shouldPassWithExactMatch() {
        List<String> actual = Arrays.asList("A", "B");
        List<String> expected = Arrays.asList("A", "B");
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", actual, expected, true));
    }

    @Test
    @DisplayName("PASS: different order")
    void shouldPassWithDifferentOrder() {
        List<String> actual = Arrays.asList("B", "A");
        List<String> expected = Arrays.asList("A", "B");
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", actual, expected, true));
    }

    @Test
    @DisplayName("FAIL: one expected value missing")
    void shouldFailWhenOneMissing() {
        List<String> actual = Arrays.asList("1004", "1020");
        List<String> expected = Arrays.asList("1004", "1011");
        assertThatThrownBy(() -> operator.apply("$.codes", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("CONTAINS_ALL failed")
                .hasMessageContaining("Missing value: 1011");
    }

    @Test
    @DisplayName("FAIL: none of the expected values found")
    void shouldFailWhenNoneFound() {
        List<String> actual = Arrays.asList("X", "Y");
        List<String> expected = Arrays.asList("A", "B");
        assertThatThrownBy(() -> operator.apply("$.items", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("CONTAINS_ALL failed");
    }

    @Test
    @DisplayName("FAIL: empty actual array")
    void shouldFailWithEmptyActual() {
        List<String> expected = Arrays.asList("A", "B");
        assertThatThrownBy(() -> operator.apply("$.items", Collections.emptyList(), expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("CONTAINS_ALL failed");
    }

    @Test
    @DisplayName("PASS: empty expected — vacuously true")
    void shouldPassWithEmptyExpected() {
        List<String> actual = Arrays.asList("A", "B");
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", actual, Collections.emptyList(), true));
    }
}
