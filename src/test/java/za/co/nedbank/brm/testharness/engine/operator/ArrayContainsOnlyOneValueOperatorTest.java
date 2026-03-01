package za.co.nedbank.brm.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.nedbank.brm.testharness.exception.HarnessAssertionException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArrayContainsOnlyOneValueOperatorTest {

    private ArrayContainsOnlyOneValueOperator operator;

    @BeforeEach
    void setUp() {
        operator = new ArrayContainsOnlyOneValueOperator();
    }

    @Test
    @DisplayName("PASS: single element matching expected string")
    void shouldPassWithSingleMatchingString() {
        List<String> list = List.of("SUCCESS");
        assertThatNoException().isThrownBy(() -> operator.apply("$.results", list, "SUCCESS", true));
    }

    @Test
    @DisplayName("PASS: single element matching expected number")
    void shouldPassWithSingleMatchingNumber() {
        List<Integer> list = List.of(42);
        assertThatNoException().isThrownBy(() -> operator.apply("$.results", list, 42, true));
    }

    @Test
    @DisplayName("PASS: numeric type coercion — int vs double")
    void shouldPassWithNumericCoercion() {
        List<Integer> list = List.of(5);
        assertThatNoException().isThrownBy(() -> operator.apply("$.results", list, 5.0, true));
    }

    @Test
    @DisplayName("FAIL: multiple elements in array")
    void shouldFailWithMultipleElements() {
        List<String> list = Arrays.asList("SUCCESS", "PENDING");
        assertThatThrownBy(() -> operator.apply("$.results", list, "SUCCESS", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("Expected exactly one element");
    }

    @Test
    @DisplayName("FAIL: empty array")
    void shouldFailWithEmptyArray() {
        List<String> list = Collections.emptyList();
        assertThatThrownBy(() -> operator.apply("$.results", list, "SUCCESS", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("Expected exactly one element");
    }

    @Test
    @DisplayName("FAIL: single element but wrong value")
    void shouldFailWithWrongValue() {
        List<String> list = List.of("FAILED");
        assertThatThrownBy(() -> operator.apply("$.results", list, "SUCCESS", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_CONTAINS_ONLY_ONE_VALUE failed");
    }

    @Test
    @DisplayName("FAIL: actual is not a list")
    void shouldFailWhenActualIsNotList() {
        assertThatThrownBy(() -> operator.apply("$.results", "scalar", "SUCCESS", true))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expected array at path");
    }

    @Test
    @DisplayName("PASS: string-to-number coercion")
    void shouldPassWithStringToNumberCoercion() {
        List<Integer> list = List.of(100);
        assertThatNoException().isThrownBy(() -> operator.apply("$.results", list, "100", true));
    }
}
