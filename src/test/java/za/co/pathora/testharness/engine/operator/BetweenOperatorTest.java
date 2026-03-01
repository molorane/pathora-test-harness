package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BetweenOperatorTest {

    private BetweenOperator operator;

    @BeforeEach
    void setUp() {
        operator = new BetweenOperator();
    }

    // ── PASS cases ──

    @Test
    @DisplayName("PASS: value within range")
    void shouldPassWhenValueWithinRange() {
        Map<String, Object> range = Map.of("min", 50, "max", 100);
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 75, range, true));
    }

    @Test
    @DisplayName("PASS: value equals min boundary (inclusive)")
    void shouldPassWhenValueEqualsMin() {
        Map<String, Object> range = Map.of("min", 50, "max", 100);
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 50, range, true));
    }

    @Test
    @DisplayName("PASS: value equals max boundary (inclusive)")
    void shouldPassWhenValueEqualsMax() {
        Map<String, Object> range = Map.of("min", 50, "max", 100);
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 100, range, true));
    }

    @Test
    @DisplayName("PASS: double value within range")
    void shouldPassWithDoubleValue() {
        Map<String, Object> range = Map.of("min", 0.0, "max", 1.0);
        assertThatNoException().isThrownBy(() -> operator.apply("$.probability", 0.5, range, true));
    }

    @Test
    @DisplayName("PASS: string-to-number coercion on actual")
    void shouldPassWithStringActualCoercion() {
        Map<String, Object> range = Map.of("min", 10, "max", 20);
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", "15", range, true));
    }

    @Test
    @DisplayName("PASS: negative range")
    void shouldPassWithNegativeRange() {
        Map<String, Object> range = Map.of("min", -100, "max", -10);
        assertThatNoException().isThrownBy(() -> operator.apply("$.temp", -50, range, true));
    }

    // ── FAIL cases ──

    @Test
    @DisplayName("FAIL: value below min")
    void shouldFailWhenValueBelowMin() {
        Map<String, Object> range = Map.of("min", 50, "max", 100);
        assertThatThrownBy(() -> operator.apply("$.score", 30, range, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("BETWEEN failed");
    }

    @Test
    @DisplayName("FAIL: value above max")
    void shouldFailWhenValueAboveMax() {
        Map<String, Object> range = Map.of("min", 50, "max", 100);
        assertThatThrownBy(() -> operator.apply("$.score", 150, range, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("BETWEEN failed");
    }

    @Test
    @DisplayName("FAIL: value just below min")
    void shouldFailWhenValueJustBelowMin() {
        Map<String, Object> range = Map.of("min", 50.0, "max", 100.0);
        assertThatThrownBy(() -> operator.apply("$.score", 49.99, range, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("BETWEEN failed");
    }

    @Test
    @DisplayName("FAIL: value just above max")
    void shouldFailWhenValueJustAboveMax() {
        Map<String, Object> range = Map.of("min", 50.0, "max", 100.0);
        assertThatThrownBy(() -> operator.apply("$.score", 100.01, range, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("BETWEEN failed");
    }

    // ── Edge / error cases ──

    @Test
    @DisplayName("FAIL: missing 'min' key in value")
    void shouldFailWhenMinKeyMissing() {
        Map<String, Object> range = Map.of("max", 100);
        assertThatThrownBy(() -> operator.apply("$.score", 75, range, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("'min' and 'max'");
    }

    @Test
    @DisplayName("FAIL: missing 'max' key in value")
    void shouldFailWhenMaxKeyMissing() {
        Map<String, Object> range = Map.of("min", 50);
        assertThatThrownBy(() -> operator.apply("$.score", 75, range, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("'min' and 'max'");
    }

    @Test
    @DisplayName("FAIL: expected is not a map")
    void shouldFailWhenExpectedIsNotMap() {
        assertThatThrownBy(() -> operator.apply("$.score", 75, "not-a-map", true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expected object but got");
    }

    @Test
    @DisplayName("PASS: min equals max — exact match")
    void shouldPassWhenMinEqualsMaxAndValueMatches() {
        Map<String, Object> range = Map.of("min", 42, "max", 42);
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 42, range, true));
    }

    @Test
    @DisplayName("FAIL: min equals max — value differs")
    void shouldFailWhenMinEqualsMaxAndValueDiffers() {
        Map<String, Object> range = Map.of("min", 42, "max", 42);
        assertThatThrownBy(() -> operator.apply("$.score", 43, range, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("BETWEEN failed");
    }
}
