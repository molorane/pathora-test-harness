package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;


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
        Object actual = TestJsonHelper.parse("""
                [
                  "A",
                  "B"
                ]
                """);
        Object expected = TestJsonHelper.parse("""
                [
                  "A",
                  "B"
                ]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.tags", actual, expected, true));
    }

    @Test
    @DisplayName("PASS: same values — different order")
    void shouldPassWithDifferentOrder() {
        Object actual = TestJsonHelper.parse("""
                [
                  "B",
                  "A"
                ]
                """);
        Object expected = TestJsonHelper.parse("""
                [
                  "A",
                  "B"
                ]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.tags", actual, expected, true));
    }

    @Test
    @DisplayName("PASS: single element match")
    void shouldPassWithSingleElement() {
        Object actual = TestJsonHelper.parse("""
                [
                  "ONLY"
                ]
                """);
        Object expected = TestJsonHelper.parse("""
                [
                  "ONLY"
                ]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.tags", actual, expected, true));
    }

    @Test
    @DisplayName("FAIL: extra element in actual")
    void shouldFailWithExtraElement() {
        Object actual = TestJsonHelper.parse("""
                [
                  "A",
                  "B",
                  "C"
                ]
                """);
        Object expected = TestJsonHelper.parse("""
                [
                  "A",
                  "B"
                ]
                """);
        assertThatThrownBy(() -> operator.apply("$.tags", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_CONTAINS_ONLY_VALUES failed");
    }

    @Test
    @DisplayName("FAIL: missing element in actual")
    void shouldFailWithMissingElement() {
        Object actual = TestJsonHelper.parse("""
                [
                  "A"
                ]
                """);
        Object expected = TestJsonHelper.parse("""
                [
                  "A",
                  "B"
                ]
                """);
        assertThatThrownBy(() -> operator.apply("$.tags", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_CONTAINS_ONLY_VALUES failed");
    }

    @Test
    @DisplayName("FAIL: same size but different values")
    void shouldFailWithDifferentValues() {
        Object actual = TestJsonHelper.parse("""
                [
                  "A",
                  "C"
                ]
                """);
        Object expected = TestJsonHelper.parse("""
                [
                  "A",
                  "B"
                ]
                """);
        assertThatThrownBy(() -> operator.apply("$.tags", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_CONTAINS_ONLY_VALUES failed");
    }

    @Test
    @DisplayName("FAIL: actual is not a list")
    void shouldFailWhenActualIsNotList() {
        Object expected = TestJsonHelper.parse("""
                [
                  "A",
                  "B"
                ]
                """);
        assertThatThrownBy(() -> operator.apply("$.tags", "not-a-list", expected, true))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expected array at path");
    }

    @Test
    @DisplayName("FAIL: expected is not a list")
    void shouldFailWhenExpectedIsNotList() {
        Object actual = TestJsonHelper.parse("""
                [
                  "A",
                  "B"
                ]
                """);
        assertThatThrownBy(() -> operator.apply("$.tags", actual, "not-a-list", true))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expected array at path");
    }
}
