package za.co.pathora.testharness.engine.operator;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FieldEqualsOtherFieldOperatorTest {

    private FieldEqualsOtherFieldOperator operator;

    @BeforeEach
    void setUp() {
        operator = new FieldEqualsOtherFieldOperator();
    }

    private DocumentContext parse(String json) {
        return JsonPath.parse(json);
    }

    @Test
    @DisplayName("PASS: two fields have the same value")
    void shouldPassWhenFieldsEqual() {
        DocumentContext ctx = parse("""
                {"outputData": {"amount": 100, "calculatedAmount": 100}}
                """);
        Map<String, Object> value = Map.of(
                "leftPath", "$.outputData.amount",
                "rightPath", "$.outputData.calculatedAmount");
        assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
    }

    @Test
    @DisplayName("PASS: string fields equal")
    void shouldPassWhenStringFieldsEqual() {
        DocumentContext ctx = parse("""
                {"outputData": {"status": "APPROVED", "finalStatus": "APPROVED"}}
                """);
        Map<String, Object> value = Map.of(
                "leftPath", "$.outputData.status",
                "rightPath", "$.outputData.finalStatus");
        assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
    }

    @Test
    @DisplayName("PASS: numeric type coercion — int vs double")
    void shouldPassWithTypeCoercion() {
        DocumentContext ctx = parse("""
                {"outputData": {"amount": 100, "calculatedAmount": 100.0}}
                """);
        Map<String, Object> value = Map.of(
                "leftPath", "$.outputData.amount",
                "rightPath", "$.outputData.calculatedAmount");
        assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
    }

    @Test
    @DisplayName("FAIL: two fields have different values")
    void shouldFailWhenFieldsDiffer() {
        DocumentContext ctx = parse("""
                {"outputData": {"amount": 100, "calculatedAmount": 200}}
                """);
        Map<String, Object> value = Map.of(
                "leftPath", "$.outputData.amount",
                "rightPath", "$.outputData.calculatedAmount");
        assertThatThrownBy(() -> operator.apply(ctx, value))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("FIELD_EQUALS_OTHER_FIELD failed")
                .hasMessageContaining("100")
                .hasMessageContaining("200");
    }

    @Test
    @DisplayName("FAIL: different string values")
    void shouldFailWhenStringFieldsDiffer() {
        DocumentContext ctx = parse("""
                {"outputData": {"status": "APPROVED", "finalStatus": "DECLINED"}}
                """);
        Map<String, Object> value = Map.of(
                "leftPath", "$.outputData.status",
                "rightPath", "$.outputData.finalStatus");
        assertThatThrownBy(() -> operator.apply(ctx, value))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("FIELD_EQUALS_OTHER_FIELD failed");
    }

    @Test
    @DisplayName("FAIL: expected is not a map")
    void shouldFailWhenExpectedIsNotMap() {
        DocumentContext ctx = parse("""
                {"outputData": {"amount": 100}}
                """);
        assertThatThrownBy(() -> operator.apply(ctx, "not-a-map"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
