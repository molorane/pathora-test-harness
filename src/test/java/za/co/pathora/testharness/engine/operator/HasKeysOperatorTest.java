package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HasKeysOperatorTest {

    private HasKeysOperator operator;

    @BeforeEach
    void setUp() {
        operator = new HasKeysOperator();
    }

    @Test
    @DisplayName("PASS: object has all expected keys")
    void shouldPassWhenAllKeysPresent() {
        Map<String, Object> actual = Map.of("clientType", "1031", "riskLevel", "HIGH", "segment", "Retail");
        List<String> expected = Arrays.asList("clientType", "riskLevel");
        assertThatNoException().isThrownBy(() -> operator.apply("$.client", actual, expected, true));
    }

    @Test
    @DisplayName("PASS: exact keys — no extras")
    void shouldPassWithExactKeys() {
        Map<String, Object> actual = Map.of("a", 1, "b", 2);
        List<String> expected = Arrays.asList("a", "b");
        assertThatNoException().isThrownBy(() -> operator.apply("$.data", actual, expected, true));
    }

    @Test
    @DisplayName("PASS: single key")
    void shouldPassWithSingleKey() {
        Map<String, Object> actual = Map.of("status", "ACTIVE");
        List<String> expected = List.of("status");
        assertThatNoException().isThrownBy(() -> operator.apply("$.data", actual, expected, true));
    }

    @Test
    @DisplayName("FAIL: one key missing")
    void shouldFailWhenOneKeyMissing() {
        Map<String, Object> actual = Map.of("clientType", "1031");
        List<String> expected = Arrays.asList("clientType", "riskLevel");
        assertThatThrownBy(() -> operator.apply("$.client", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("HAS_KEYS failed")
                .hasMessageContaining("riskLevel");
    }

    @Test
    @DisplayName("FAIL: all keys missing")
    void shouldFailWhenAllKeysMissing() {
        Map<String, Object> actual = Map.of("other", "value");
        List<String> expected = Arrays.asList("clientType", "riskLevel");
        assertThatThrownBy(() -> operator.apply("$.client", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("HAS_KEYS failed");
    }

    @Test
    @DisplayName("FAIL: actual is not a map")
    void shouldFailWhenActualIsNotMap() {
        List<String> expected = Arrays.asList("clientType", "riskLevel");
        assertThatThrownBy(() -> operator.apply("$.client", "not-a-map", expected, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expected object but got");
    }
}
