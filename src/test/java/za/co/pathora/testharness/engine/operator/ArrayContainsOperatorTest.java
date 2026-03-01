package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArrayContainsOperatorTest {

    private ArrayContainsOperator operator;

    @BeforeEach
    void setUp() {
        operator = new ArrayContainsOperator();
    }

    @Test
    @DisplayName("PASS: array contains the expected string value")
    void shouldPassWhenArrayContainsString() {
        List<String> list = Arrays.asList("1004", "1011");
        assertThatNoException().isThrownBy(() -> operator.apply("$.codes", list, "1004", true));
    }

    @Test
    @DisplayName("PASS: array contains the expected numeric value")
    void shouldPassWhenArrayContainsNumber() {
        List<Double> list = Arrays.asList(1.0, 2.0, 3.0);
        assertThatNoException().isThrownBy(() -> operator.apply("$.ids", list, 2.0, true));
    }

    @Test
    @DisplayName("PASS: array contains value — single element")
    void shouldPassWithSingleElementArray() {
        List<String> list = List.of("ONLY");
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", list, "ONLY", true));
    }

    @Test
    @DisplayName("PASS: array contains value — last element")
    void shouldPassWhenValueIsLastElement() {
        List<String> list = Arrays.asList("A", "B", "C");
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", list, "C", true));
    }

    @Test
    @DisplayName("FAIL: array does not contain expected value")
    void shouldFailWhenValueNotInArray() {
        List<String> list = Arrays.asList("1011", "1012");
        assertThatThrownBy(() -> operator.apply("$.codes", list, "1004", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_CONTAINS failed");
    }

    @Test
    @DisplayName("FAIL: empty array")
    void shouldFailWhenArrayIsEmpty() {
        List<String> list = List.of();
        assertThatThrownBy(() -> operator.apply("$.codes", list, "1004", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_CONTAINS failed");
    }

    @Test
    @DisplayName("FAIL: actual is not a list")
    void shouldFailWhenActualIsNotList() {
        assertThatThrownBy(() -> operator.apply("$.codes", "not-a-list", "1004", true))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expected array at path");
    }
}
