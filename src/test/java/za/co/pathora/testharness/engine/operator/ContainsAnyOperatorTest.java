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

class ContainsAnyOperatorTest {

    private ContainsAnyOperator operator;

    @BeforeEach
    void setUp() {
        operator = new ContainsAnyOperator();
    }

    @Test
    @DisplayName("PASS: array contains one of the expected values")
    void shouldPassWhenOneMatches() {
        List<String> actual = Arrays.asList("1004", "1020", "1030");
        List<String> expected = Arrays.asList("1004", "1011");
        assertThatNoException().isThrownBy(() -> operator.apply("$.codes", actual, expected, true));
    }

    @Test
    @DisplayName("PASS: array contains all expected values")
    void shouldPassWhenAllMatch() {
        List<String> actual = Arrays.asList("1004", "1011", "1020");
        List<String> expected = Arrays.asList("1004", "1011");
        assertThatNoException().isThrownBy(() -> operator.apply("$.codes", actual, expected, true));
    }

    @Test
    @DisplayName("PASS: last expected value found")
    void shouldPassWhenLastExpectedFound() {
        List<String> actual = Arrays.asList("A", "B", "C");
        List<String> expected = Arrays.asList("X", "Y", "C");
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", actual, expected, true));
    }

    @Test
    @DisplayName("FAIL: none of the expected values found")
    void shouldFailWhenNoneFound() {
        List<String> actual = Arrays.asList("1020", "1030");
        List<String> expected = Arrays.asList("1004", "1011");
        assertThatThrownBy(() -> operator.apply("$.codes", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("CONTAINS_ANY failed");
    }

    @Test
    @DisplayName("FAIL: empty actual array")
    void shouldFailWithEmptyActual() {
        List<String> expected = Arrays.asList("1004", "1011");
        assertThatThrownBy(() -> operator.apply("$.codes", Collections.emptyList(), expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("CONTAINS_ANY failed");
    }

    @Test
    @DisplayName("FAIL: actual is not a list")
    void shouldFailWhenActualIsNotList() {
        List<String> expected = Arrays.asList("1004", "1011");
        assertThatThrownBy(() -> operator.apply("$.codes", "scalar", expected, true))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expected array at path");
    }
}
