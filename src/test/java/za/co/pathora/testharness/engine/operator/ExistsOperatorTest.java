package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExistsOperatorTest {

    private ExistsOperator operator;

    @BeforeEach
    void setUp() {
        operator = new ExistsOperator();
    }

    /**
     * ```json
     * { "JsonPath": "$.referenceId", "Operator": "EXISTS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: path exists with string value")
    void shouldPassWhenPathExistsWithString() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.referenceId", "ABC123", null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.count", "Operator": "EXISTS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: path exists with numeric value")
    void shouldPassWhenPathExistsWithNumber() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.count", 42, null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.field", "Operator": "EXISTS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: path exists with null value")
    void shouldPassWhenPathExistsWithNull() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", null, null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.active", "Operator": "EXISTS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: path exists with boolean value")
    void shouldPassWhenPathExistsWithBoolean() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.active", true, null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.missing", "Operator": "EXISTS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("FAIL: path does not exist")
    void shouldFailWhenPathDoesNotExist() {
        assertThatThrownBy(() -> operator.apply("$.missing", null, null, false))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expected path to exist: $.missing");
    }

    /**
     * ```json
     * { "JsonPath": "$.outputData.nested.field", "Operator": "EXISTS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("FAIL: path does not exist — nested path")
    void shouldFailForNestedMissingPath() {
        assertThatThrownBy(() -> operator.apply("$.outputData.nested.field", null, null, false))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expected path to exist");
    }
}
