package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;


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
            Object list = TestJsonHelper.parse("""
                [0, 0, 0 ]
                """);
            assertThatNoException().isThrownBy(() -> operator.apply("$.penalties", list, 0, true));
        }

        @Test
        @DisplayName("PASS: all elements equal to string")
        void shouldPassWhenAllSameString() {
            Object list = TestJsonHelper.parse("""
                ["Retail", "Retail", "Retail" ]
                """);
            assertThatNoException().isThrownBy(() -> operator.apply("$.segments", list, "Retail", true));
        }

        @Test
        @DisplayName("PASS: single element matches")
        void shouldPassWithSingleElement() {
            Object list = TestJsonHelper.parse("""
                ["Retail" ]
                """);
            assertThatNoException().isThrownBy(() -> operator.apply("$.segments", list, "Retail", true));
        }

        @Test
        @DisplayName("PASS: empty array — vacuously true")
        void shouldPassWithEmptyArray() {
            Object list = TestJsonHelper.parse("""
                []
                """);
            assertThatNoException().isThrownBy(() -> operator.apply("$.items", list, "anything", true));
        }

        @Test
        @DisplayName("PASS: numeric type coercion — int 0 equals double 0.0")
        void shouldPassWithNumericCoercion() {
            Object list = TestJsonHelper.parse("""
                [0, 0, 0 ]
                """);
            assertThatNoException().isThrownBy(() -> operator.apply("$.penalties", list, 0.0, true));
        }

        @Test
        @DisplayName("FAIL: one element differs")
        void shouldFailWhenOneElementDiffers() {
            Object list = TestJsonHelper.parse("""
                [0, 0, 1 ]
                """);
            assertThatThrownBy(() -> operator.apply("$.penalties", list, 0, true))
                    .isInstanceOf(HarnessAssertionException.class)
                    .hasMessageContaining("ALL_MATCH failed")
                    .hasMessageContaining("index 2");
        }

        @Test
        @DisplayName("FAIL: first element differs")
        void shouldFailWhenFirstElementDiffers() {
            Object list = TestJsonHelper.parse("""
                ["Corporate", "Retail", "Retail" ]
                """);
            assertThatThrownBy(() -> operator.apply("$.segments", list, "Retail", true))
                    .isInstanceOf(HarnessAssertionException.class)
                    .hasMessageContaining("index 0");
        }

        @Test
        @DisplayName("FAIL: all elements differ")
        void shouldFailWhenAllDiffer() {
            Object list = TestJsonHelper.parse("""
                [1, 2, 3 ]
                """);
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
            Object list = TestJsonHelper.parse("""
                [60, 70, 80 ]
                """);
            Object condition = TestJsonHelper.parse("""
                    {
                      "greaterThan": 50
                    }
                    """);
            assertThatNoException().isThrownBy(() -> operator.apply("$.scores", list, condition, true));
        }

        @Test
        @DisplayName("FAIL: one element equals threshold (not strictly greater)")
        void shouldFailWhenOneEqualsThreshold() {
            Object list = TestJsonHelper.parse("""
                [60, 50, 80 ]
                """);
            Object condition = TestJsonHelper.parse("""
                    {
                      "greaterThan": 50
                    }
                    """);
            assertThatThrownBy(() -> operator.apply("$.scores", list, condition, true))
                    .isInstanceOf(HarnessAssertionException.class)
                    .hasMessageContaining("index 1");
        }

        @Test
        @DisplayName("FAIL: one element below threshold")
        void shouldFailWhenOneBelowThreshold() {
            Object list = TestJsonHelper.parse("""
                [60, 40, 80 ]
                """);
            Object condition = TestJsonHelper.parse("""
                    {
                      "greaterThan": 50
                    }
                    """);
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
            Object list = TestJsonHelper.parse("""
                [10, 20, 30 ]
                """);
            Object condition = TestJsonHelper.parse("""
                    {
                      "lessThan": 50
                    }
                    """);
            assertThatNoException().isThrownBy(() -> operator.apply("$.scores", list, condition, true));
        }

        @Test
        @DisplayName("FAIL: one element equals threshold (not strictly less)")
        void shouldFailWhenOneEqualsThreshold() {
            Object list = TestJsonHelper.parse("""
                [10, 50, 30 ]
                """);
            Object condition = TestJsonHelper.parse("""
                    {
                      "lessThan": 50
                    }
                    """);
            assertThatThrownBy(() -> operator.apply("$.scores", list, condition, true))
                    .isInstanceOf(HarnessAssertionException.class)
                    .hasMessageContaining("index 1");
        }

        @Test
        @DisplayName("FAIL: one element above threshold")
        void shouldFailWhenOneAboveThreshold() {
            Object list = TestJsonHelper.parse("""
                [10, 60, 30 ]
                """);
            Object condition = TestJsonHelper.parse("""
                    {
                      "lessThan": 50
                    }
                    """);
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
            Object list = TestJsonHelper.parse("""
                [50, 75, 100 ]
                """);
            Object condition = TestJsonHelper.parse("""
                    {
                      "between": {
                        "min": 50,
                        "max": 100
                      }
                    }
                    """);
            assertThatNoException().isThrownBy(() -> operator.apply("$.scores", list, condition, true));
        }

        @Test
        @DisplayName("FAIL: one element outside range")
        void shouldFailWhenOneOutsideRange() {
            Object list = TestJsonHelper.parse("""
                [50, 110, 75 ]
                """);
            Object condition = TestJsonHelper.parse("""
                    {
                      "between": {
                        "min": 50,
                        "max": 100
                      }
                    }
                    """);
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
            Object list = TestJsonHelper.parse("""
                [1, 2, 3 ]
                """);
            Object condition = TestJsonHelper.parse("""
                    {
                      "unknownKey": 50
                    }
                    """);
            assertThatThrownBy(() -> operator.apply("$.scores", list, condition, true))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("greaterThan");
        }

        @Test
        @DisplayName("FAIL: non-numeric value for greaterThan")
        void shouldFailWithNonNumericForGreaterThan() {
            Object list = TestJsonHelper.parse("""
                ["a", "b" ]
                """);
            Object condition = TestJsonHelper.parse("""
                    {
                      "greaterThan": 50
                    }
                    """);
            assertThatThrownBy(() -> operator.apply("$.items", list, condition, true))
                    .isInstanceOf(NumberFormatException.class);
        }
    }
}
