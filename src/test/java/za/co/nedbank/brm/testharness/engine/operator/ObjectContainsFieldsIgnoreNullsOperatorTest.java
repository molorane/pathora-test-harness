package za.co.nedbank.brm.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.nedbank.brm.testharness.exception.HarnessAssertionException;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ObjectContainsFieldsIgnoreNullsOperatorTest {

    private ObjectContainsFieldsIgnoreNullsOperator operator;

    @BeforeEach
    void setUp() {
        operator = new ObjectContainsFieldsIgnoreNullsOperator();
    }

    @Test
    @DisplayName("PASS: null expected fields are ignored")
    void shouldPassIgnoringNullExpectedFields() {
        Map<String, Object> actual = Map.of("clientType", "1031");

        Map<String, Object> expected = new HashMap<>();
        expected.put("clientType", "1031");
        expected.put("middleName", null);

        assertThatNoException().isThrownBy(() -> operator.apply("$.client", actual, expected, true));
    }

    @Test
    @DisplayName("PASS: all non-null fields match")
    void shouldPassWhenNonNullFieldsMatch() {
        Map<String, Object> actual = Map.of(
                "clientType", "1031",
                "riskLevel", "HIGH",
                "segment", "Retail");

        Map<String, Object> expected = new HashMap<>();
        expected.put("clientType", "1031");
        expected.put("riskLevel", "HIGH");
        expected.put("optionalField", null);

        assertThatNoException().isThrownBy(() -> operator.apply("$.client", actual, expected, true));
    }

    @Test
    @DisplayName("PASS: all expected fields are null — always passes")
    void shouldPassWhenAllExpectedFieldsAreNull() {
        Map<String, Object> actual = Map.of("anything", "value");

        Map<String, Object> expected = new HashMap<>();
        expected.put("field1", null);
        expected.put("field2", null);

        assertThatNoException().isThrownBy(() -> operator.apply("$.data", actual, expected, true));
    }

    @Test
    @DisplayName("FAIL: non-null expected field value mismatch")
    void shouldFailWhenNonNullFieldMismatch() {
        Map<String, Object> actual = Map.of("clientType", "1032");

        Map<String, Object> expected = new HashMap<>();
        expected.put("clientType", "1031");
        expected.put("middleName", null);

        assertThatThrownBy(() -> operator.apply("$.client", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("OBJECT_CONTAINS_FIELDS_IGNORE_NULLS failed");
    }

    @Test
    @DisplayName("FAIL: non-null expected field missing in actual")
    void shouldFailWhenNonNullFieldMissing() {
        Map<String, Object> actual = Map.of("otherField", "value");

        Map<String, Object> expected = new HashMap<>();
        expected.put("clientType", "1031");
        expected.put("middleName", null);

        assertThatThrownBy(() -> operator.apply("$.client", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("OBJECT_CONTAINS_FIELDS_IGNORE_NULLS failed");
    }

    @Test
    @DisplayName("PASS: exact match with no null fields")
    void shouldPassWithExactMatchNoNulls() {
        Map<String, Object> actual = Map.of(
                "a", "1",
                "b", "2");

        Map<String, Object> expected = Map.of(
                "a", "1",
                "b", "2");

        assertThatNoException().isThrownBy(() -> operator.apply("$.data", actual, expected, true));
    }

    @Test
    @DisplayName("FAIL: actual is not a map")
    void shouldFailWhenActualIsNotMap() {
        Map<String, Object> expected = new HashMap<>();
        expected.put("field", "value");

        assertThatThrownBy(() -> operator.apply("$.data", "not-a-map", expected, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expected object but got");
    }
}
