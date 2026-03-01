package za.co.nedbank.brm.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.nedbank.brm.testharness.exception.HarnessAssertionException;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ObjectContainsFieldsOperatorTest {

    private ObjectContainsFieldsOperator operator;

    @BeforeEach
    void setUp() {
        operator = new ObjectContainsFieldsOperator();
    }

    @Test
    @DisplayName("PASS: actual contains all expected fields")
    void shouldPassWhenAllFieldsMatch() {
        Map<String, Object> actual = Map.of(
                "clientType", "1031",
                "riskLevel", "HIGH",
                "segment", "Retail");

        Map<String, Object> expected = Map.of(
                "clientType", "1031",
                "riskLevel", "HIGH");

        assertThatNoException().isThrownBy(() -> operator.apply("$.client", actual, expected, true));
    }

    @Test
    @DisplayName("PASS: exact match — no extra fields")
    void shouldPassWithExactMatch() {
        Map<String, Object> actual = Map.of("status", "APPROVED");
        Map<String, Object> expected = Map.of("status", "APPROVED");

        assertThatNoException().isThrownBy(() -> operator.apply("$.result", actual, expected, true));
    }

    @Test
    @DisplayName("PASS: nested map matches")
    void shouldPassWithNestedMap() {
        Map<String, Object> nestedActual = Map.of("code", "X");
        Map<String, Object> actual = Map.of("nested", nestedActual);

        Map<String, Object> nestedExpected = Map.of("code", "X");
        Map<String, Object> expected = Map.of("nested", nestedExpected);

        assertThatNoException().isThrownBy(() -> operator.apply("$.data", actual, expected, true));
    }

    @Test
    @DisplayName("FAIL: field value mismatch")
    void shouldFailWhenFieldValueDiffers() {
        Map<String, Object> actual = Map.of(
                "clientType", "1031",
                "riskLevel", "LOW");

        Map<String, Object> expected = Map.of(
                "clientType", "1031",
                "riskLevel", "HIGH");

        assertThatThrownBy(() -> operator.apply("$.client", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("OBJECT_CONTAINS_FIELDS failed");
    }

    @Test
    @DisplayName("FAIL: expected field missing in actual")
    void shouldFailWhenFieldMissing() {
        Map<String, Object> actual = Map.of("clientType", "1031");

        Map<String, Object> expected = Map.of(
                "clientType", "1031",
                "riskLevel", "HIGH");

        assertThatThrownBy(() -> operator.apply("$.client", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("OBJECT_CONTAINS_FIELDS failed");
    }

    @Test
    @DisplayName("FAIL: null expected field does NOT match missing")
    void shouldFailWhenExpectedNullButFieldMissing() {
        Map<String, Object> actual = Map.of("clientType", "1031");

        Map<String, Object> expected = new HashMap<>();
        expected.put("clientType", "1031");
        expected.put("middleName", null);

        assertThatThrownBy(() -> operator.apply("$.client", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("OBJECT_CONTAINS_FIELDS failed");
    }

    @Test
    @DisplayName("FAIL: actual is not a map")
    void shouldFailWhenActualIsNotMap() {
        Map<String, Object> expected = Map.of("field", "value");

        assertThatThrownBy(() -> operator.apply("$.client", "not-a-map", expected, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expected object but got");
    }
}
