package za.co.nedbank.brm.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.nedbank.brm.testharness.exception.HarnessAssertionException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArrayContainsOnlyValuesOperatorTest {

    private ArrayContainsOnlyValuesOperator operator;

    @BeforeEach
    void setUp() {
        operator = new ArrayContainsOnlyValuesOperator();
    }

    @Test
    @DisplayName("PASS: exact match — same values same order")
    void shouldPassWithExactMatch() {
        List<String> actual = Arrays.asList("A", "B");
        List<String> expected = Arrays.asList("A", "B");
        assertThatNoException().isThrownBy(() -> operator.apply("$.tags", actual, expected, true));
    }

    @Test
    @DisplayName("PASS: same values — different order")
    void shouldPassWithDifferentOrder() {
        List<String> actual = Arrays.asList("B", "A");
        List<String> expected = Arrays.asList("A", "B");
        assertThatNoException().isThrownBy(() -> operator.apply("$.tags", actual, expected, true));
    }

    @Test
    @DisplayName("PASS: single element match")
    void shouldPassWithSingleElement() {
        List<String> actual = List.of("ONLY");
        List<String> expected = List.of("ONLY");
        assertThatNoException().isThrownBy(() -> operator.apply("$.tags", actual, expected, true));
    }

    @Test
    @DisplayName("FAIL: extra element in actual")
    void shouldFailWithExtraElement() {
        List<String> actual = Arrays.asList("A", "B", "C");
        List<String> expected = Arrays.asList("A", "B");
        assertThatThrownBy(() -> operator.apply("$.tags", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_CONTAINS_ONLY_VALUES failed");
    }

    @Test
    @DisplayName("FAIL: missing element in actual")
    void shouldFailWithMissingElement() {
        List<String> actual = List.of("A");
        List<String> expected = Arrays.asList("A", "B");
        assertThatThrownBy(() -> operator.apply("$.tags", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_CONTAINS_ONLY_VALUES failed");
    }

    @Test
    @DisplayName("FAIL: same size but different values")
    void shouldFailWithDifferentValues() {
        List<String> actual = Arrays.asList("A", "C");
        List<String> expected = Arrays.asList("A", "B");
        assertThatThrownBy(() -> operator.apply("$.tags", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_CONTAINS_ONLY_VALUES failed");
    }

    @Test
    @DisplayName("FAIL: actual is not a list")
    void shouldFailWhenActualIsNotList() {
        List<String> expected = Arrays.asList("A", "B");
        assertThatThrownBy(() -> operator.apply("$.tags", "not-a-list", expected, true))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expected array at path");
    }

    @Test
    @DisplayName("FAIL: expected is not a list")
    void shouldFailWhenExpectedIsNotList() {
        List<String> actual = Arrays.asList("A", "B");
        assertThatThrownBy(() -> operator.apply("$.tags", actual, "not-a-list", true))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expected array at path");
    }
}
