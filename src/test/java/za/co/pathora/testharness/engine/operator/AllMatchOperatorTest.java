package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AllMatchOperatorTest {

    private AllMatchOperator operator;

    @BeforeEach
    void setUp() {
        operator = new AllMatchOperator();
    }

    // ═══════════════════════════════════════
    // EQUALS MODE (scalar Value)
    // ═══════════════════════════════════════

    @Nested
    @DisplayName("Equals mode (scalar Value)")
    class EqualsMode {

        @Test
        @DisplayName("PASS: all elements equal to 0")
        void shouldPassWhenAllZeros() {
            List<Integer> list = Arrays.asList(0, 0, 0);
            assertThatNoException().isThrownBy(() -> operator.apply("$.penalties", list, 0, true));
        }

        @Test
        @DisplayName("PASS: all elements equal to string")
        void shouldPassWhenAllSameString() {
            List<String> list = Arrays.asList("Retail", "Retail", "Retail");
            assertThatNoException().isThrownBy(() -> operator.apply("$.segments", list, "Retail", true));
        }

        @Test
        @DisplayName("PASS: single element matches")
        void shouldPassWithSingleElement() {
            List<String> list = List.of("Retail");
            assertThatNoException().isThrownBy(() -> operator.apply("$.segments", list, "Retail", true));
        }

        @Test
        @DisplayName("PASS: empty array — vacuously true")
        void shouldPassWithEmptyArray() {
            List<Object> list = Collections.emptyList();
            assertThatNoException().isThrownBy(() -> operator.apply("$.items", list, "anything", true));
        }

        @Test
        @DisplayName("PASS: numeric type coercion — int 0 equals double 0.0")
        void shouldPassWithNumericCoercion() {
            List<Integer> list = Arrays.asList(0, 0, 0);
            assertThatNoException().isThrownBy(() -> operator.apply("$.penalties", list, 0.0, true));
        }

        @Test
        @DisplayName("FAIL: one element differs")
        void shouldFailWhenOneElementDiffers() {
            List<Integer> list = Arrays.asList(0, 0, 1);
            assertThatThrownBy(() -> operator.apply("$.penalties", list, 0, true))
                    .isInstanceOf(HarnessAssertionException.class)
                    .hasMessageContaining("ALL_MATCH failed")
                    .hasMessageContaining("index 2");
        }

        @Test
        @DisplayName("FAIL: first element differs")
        void shouldFailWhenFirstElementDiffers() {
            List<String> list = Arrays.asList("Corporate", "Retail", "Retail");
            assertThatThrownBy(() -> operator.apply("$.segments", list, "Retail", true))
                    .isInstanceOf(HarnessAssertionException.class)
                    .hasMessageContaining("index 0");
        }

        @Test
        @DisplayName("FAIL: all elements differ")
        void shouldFailWhenAllDiffer() {
            List<Integer> list = Arrays.asList(1, 2, 3);
            assertThatThrownBy(() -> operator.apply("$.penalties", list, 0, true))
                    .isInstanceOf(HarnessAssertionException.class)
                    .hasMessageContaining("ALL_MATCH failed");
        }
    }

    // ═══════════════════════════════════════
    // GREATER_THAN MODE
    // ═══════════════════════════════════════

    @Nested
    @DisplayName("greaterThan mode")
    class GreaterThanMode {

        @Test
        @DisplayName("PASS: all elements greater than threshold")
        void shouldPassWhenAllGreater() {
            List<Integer> list = Arrays.asList(60, 70, 80);
            Map<String, Object> condition = Map.of("greaterThan", 50);
            assertThatNoException().isThrownBy(() -> operator.apply("$.scores", list, condition, true));
        }

        @Test
        @DisplayName("FAIL: one element equals threshold (not strictly greater)")
        void shouldFailWhenOneEqualsThreshold() {
            List<Integer> list = Arrays.asList(60, 50, 80);
            Map<String, Object> condition = Map.of("greaterThan", 50);
            assertThatThrownBy(() -> operator.apply("$.scores", list, condition, true))
                    .isInstanceOf(HarnessAssertionException.class)
                    .hasMessageContaining("index 1");
        }

        @Test
        @DisplayName("FAIL: one element below threshold")
        void shouldFailWhenOneBelowThreshold() {
            List<Integer> list = Arrays.asList(60, 40, 80);
            Map<String, Object> condition = Map.of("greaterThan", 50);
            assertThatThrownBy(() -> operator.apply("$.scores", list, condition, true))
                    .isInstanceOf(HarnessAssertionException.class)
                    .hasMessageContaining("ALL_MATCH failed");
        }
    }

    // ═══════════════════════════════════════
    // LESS_THAN MODE
    // ═══════════════════════════════════════

    @Nested
    @DisplayName("lessThan mode")
    class LessThanMode {

        @Test
        @DisplayName("PASS: all elements less than threshold")
        void shouldPassWhenAllLess() {
            List<Integer> list = Arrays.asList(10, 20, 30);
            Map<String, Object> condition = Map.of("lessThan", 50);
            assertThatNoException().isThrownBy(() -> operator.apply("$.scores", list, condition, true));
        }

        @Test
        @DisplayName("FAIL: one element equals threshold (not strictly less)")
        void shouldFailWhenOneEqualsThreshold() {
            List<Integer> list = Arrays.asList(10, 50, 30);
            Map<String, Object> condition = Map.of("lessThan", 50);
            assertThatThrownBy(() -> operator.apply("$.scores", list, condition, true))
                    .isInstanceOf(HarnessAssertionException.class)
                    .hasMessageContaining("index 1");
        }

        @Test
        @DisplayName("FAIL: one element above threshold")
        void shouldFailWhenOneAboveThreshold() {
            List<Integer> list = Arrays.asList(10, 60, 30);
            Map<String, Object> condition = Map.of("lessThan", 50);
            assertThatThrownBy(() -> operator.apply("$.scores", list, condition, true))
                    .isInstanceOf(HarnessAssertionException.class)
                    .hasMessageContaining("ALL_MATCH failed");
        }
    }

    // ═══════════════════════════════════════
    // BETWEEN MODE
    // ═══════════════════════════════════════

    @Nested
    @DisplayName("between mode")
    class BetweenMode {

        @Test
        @DisplayName("PASS: all elements within range")
        void shouldPassWhenAllWithinRange() {
            List<Integer> list = Arrays.asList(50, 75, 100);
            Map<String, Object> condition = Map.of(
                    "between", Map.of("min", 50, "max", 100));
            assertThatNoException().isThrownBy(() -> operator.apply("$.scores", list, condition, true));
        }

        @Test
        @DisplayName("FAIL: one element outside range")
        void shouldFailWhenOneOutsideRange() {
            List<Integer> list = Arrays.asList(50, 110, 75);
            Map<String, Object> condition = Map.of(
                    "between", Map.of("min", 50, "max", 100));
            assertThatThrownBy(() -> operator.apply("$.scores", list, condition, true))
                    .isInstanceOf(HarnessAssertionException.class)
                    .hasMessageContaining("index 1");
        }
    }

    // ═══════════════════════════════════════
    // EDGE / ERROR CASES
    // ═══════════════════════════════════════

    @Nested
    @DisplayName("Edge cases")
    class EdgeCases {

        @Test
        @DisplayName("FAIL: actual is not a list")
        void shouldFailWhenNotList() {
            assertThatThrownBy(() -> operator.apply("$.items", "scalar", 0, true))
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContaining("Expected array at path");
        }

        @Test
        @DisplayName("FAIL: unknown condition key")
        void shouldFailWithUnknownCondition() {
            List<Integer> list = Arrays.asList(1, 2, 3);
            Map<String, Object> condition = Map.of("unknownKey", 50);
            assertThatThrownBy(() -> operator.apply("$.scores", list, condition, true))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("greaterThan");
        }

        @Test
        @DisplayName("FAIL: non-numeric value for greaterThan")
        void shouldFailWithNonNumericForGreaterThan() {
            List<String> list = Arrays.asList("a", "b");
            Map<String, Object> condition = Map.of("greaterThan", 50);
            assertThatThrownBy(() -> operator.apply("$.items", list, condition, true))
                    .isInstanceOf(NumberFormatException.class);
        }
    }
}
