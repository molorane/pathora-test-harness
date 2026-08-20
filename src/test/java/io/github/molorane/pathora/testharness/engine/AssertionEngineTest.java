package io.github.molorane.pathora.testharness.engine;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AssertionEngineTest {

    private AssertionEngine engine;

    @BeforeEach
    void setUp() {
        engine = new AssertionEngine();
    }

    private JsonAssertion assertion(String jsonPath, AssertionOperator operator, Object value) {
        return new JsonAssertion(jsonPath, operator, value, null, null);
    }

    private JsonAssertion composite(AssertionOperator operator, List<JsonAssertion> assertions) {
        return new JsonAssertion(null, operator, null, null, assertions);
    }

    private RuleTestCase testCase(List<JsonAssertion> assertions) {
        return new RuleTestCase("Test 1", "Description", "EntryPoint1", List.of(), assertions);
    }

    @Test
    @DisplayName("PASS: assertResponse evaluates path assertions successfully")
    void shouldAssertResponseSuccessfully() {
        String json = "{\"status\": \"ACTIVE\", \"count\": 10}";
        List<JsonAssertion> assertions = List.of(
                assertion("$.status", AssertionOperator.EQUALS, "ACTIVE"),
                assertion("$.count", AssertionOperator.GREATER_THAN, 5)
        );

        assertThatNoException().isThrownBy(() -> engine.assertResponse(json, testCase(assertions)));
    }

    @Test
    @DisplayName("PASS: assertResponse evaluates DocumentContextAwareEvaluator successfully")
    void shouldEvaluateDocumentContextAwareEvaluator() {
        String json = "{\"actualVal\": 100, \"expectedVal\": 100}";
        List<JsonAssertion> assertions = List.of(
                assertion(null, AssertionOperator.FIELD_EQUALS_OTHER_FIELD, Map.of(
                        "leftPath", "$.actualVal",
                        "rightPath", "$.expectedVal"
                ))
        );

        assertThatNoException().isThrownBy(() -> engine.assertResponse(json, testCase(assertions)));
    }

    @Test
    @DisplayName("PASS: PATH_EXISTS, PATH_NOT_EXISTS, and EXISTS allow non-existent paths to pass to evaluator")
    void shouldHandleNonExistentPathForExistenceOperators() {
        String json = "{\"presentKey\": \"val\"}";
        List<JsonAssertion> assertions = List.of(
                assertion("$.absentKey", AssertionOperator.PATH_NOT_EXISTS, null)
        );

        assertThatNoException().isThrownBy(() -> engine.assertResponse(json, testCase(assertions)));
    }

    @Test
    @DisplayName("PASS: evaluate AND logical assertion when all pass")
    void shouldEvaluateAndAssertionSuccessfully() {
        String json = "{\"user\": {\"age\": 25, \"active\": true}}";
        JsonAssertion andAssertion = composite(AssertionOperator.AND, List.of(
                assertion("$.user.age", AssertionOperator.GREATER_THAN, 18),
                assertion("$.user.active", AssertionOperator.EQUALS, true)
        ));

        assertThatNoException().isThrownBy(() -> engine.assertResponse(json, testCase(List.of(andAssertion))));
    }

    @Test
    @DisplayName("FAIL: evaluate AND logical assertion throws exception when sub-assertion fails")
    void shouldFailAndAssertionWhenSubAssertionFails() {
        String json = "{\"user\": {\"age\": 15, \"active\": true}}";
        JsonAssertion andAssertion = composite(AssertionOperator.AND, List.of(
                assertion("$.user.age", AssertionOperator.GREATER_THAN, 18),
                assertion("$.user.active", AssertionOperator.EQUALS, true)
        ));

        assertThatThrownBy(() -> engine.assertResponse(json, testCase(List.of(andAssertion))))
                .isInstanceOf(HarnessAssertionException.class);
    }

    @Test
    @DisplayName("FAIL: evaluate AND throws IllegalArgumentException when assertions list is empty")
    void shouldThrowExceptionWhenAndHasNoAssertions() {
        String json = "{\"status\": \"OK\"}";
        JsonAssertion invalidAnd = composite(AssertionOperator.AND, List.of());

        assertThatThrownBy(() -> engine.assertResponse(json, testCase(List.of(invalidAnd))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("AND operator requires 'Assertions' list");
    }

    @Test
    @DisplayName("PASS: evaluate OR logical assertion when at least one assertion passes")
    void shouldEvaluateOrAssertionSuccessfully() {
        String json = "{\"status\": \"PENDING\"}";
        JsonAssertion orAssertion = composite(AssertionOperator.OR, List.of(
                assertion("$.status", AssertionOperator.EQUALS, "ACTIVE"),
                assertion("$.status", AssertionOperator.EQUALS, "PENDING")
        ));

        assertThatNoException().isThrownBy(() -> engine.assertResponse(json, testCase(List.of(orAssertion))));
    }

    @Test
    @DisplayName("FAIL: evaluate OR throws AssertionError when all nested assertions fail")
    void shouldFailOrAssertionWhenAllSubAssertionsFail() {
        String json = "{\"status\": \"REJECTED\"}";
        JsonAssertion orAssertion = composite(AssertionOperator.OR, List.of(
                assertion("$.status", AssertionOperator.EQUALS, "ACTIVE"),
                assertion("$.status", AssertionOperator.EQUALS, "PENDING")
        ));

        assertThatThrownBy(() -> engine.assertResponse(json, testCase(List.of(orAssertion))))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("LOGICAL_OR_FAILED");
    }

    @Test
    @DisplayName("FAIL: evaluate OR throws IllegalArgumentException when assertions list is null")
    void shouldThrowExceptionWhenOrHasNoAssertions() {
        String json = "{\"status\": \"OK\"}";
        JsonAssertion invalidOr = composite(AssertionOperator.OR, null);

        assertThatThrownBy(() -> engine.assertResponse(json, testCase(List.of(invalidOr))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("OR operator requires 'Assertions' list");
    }

    @Test
    @DisplayName("PASS: evaluate NOT logical assertion when nested assertion fails")
    void shouldEvaluateNotAssertionSuccessfully() {
        String json = "{\"status\": \"ACTIVE\"}";
        JsonAssertion notAssertion = composite(AssertionOperator.NOT, List.of(
                assertion("$.status", AssertionOperator.EQUALS, "INACTIVE")
        ));

        assertThatNoException().isThrownBy(() -> engine.assertResponse(json, testCase(List.of(notAssertion))));
    }

    @Test
    @DisplayName("FAIL: evaluate NOT throws AssertionError when nested assertion passes")
    void shouldFailNotAssertionWhenSubAssertionPasses() {
        String json = "{\"status\": \"ACTIVE\"}";
        JsonAssertion notAssertion = composite(AssertionOperator.NOT, List.of(
                assertion("$.status", AssertionOperator.EQUALS, "ACTIVE")
        ));

        assertThatThrownBy(() -> engine.assertResponse(json, testCase(List.of(notAssertion))))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("LOGICAL_NOT_FAILED");
    }

    @Test
    @DisplayName("FAIL: evaluate NOT throws IllegalArgumentException when assertions list size is not 1")
    void shouldThrowExceptionWhenNotHasInvalidAssertionsSize() {
        String json = "{\"status\": \"OK\"}";
        JsonAssertion invalidNot = composite(AssertionOperator.NOT, List.of(
                assertion("$.status", AssertionOperator.EQUALS, "OK"),
                assertion("$.status", AssertionOperator.EQUALS, "ACTIVE")
        ));

        assertThatThrownBy(() -> engine.assertResponse(json, testCase(List.of(invalidNot))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("NOT operator requires exactly one 'Assertions'");
    }

    @Test
    @DisplayName("FAIL: throws AssertionError on non-existent JSON path for standard operator")
    void shouldThrowAssertionErrorWhenPathNotFound() {
        String json = "{\"status\": \"OK\"}";
        List<JsonAssertion> assertions = List.of(
                assertion("$.nonExistentPath", AssertionOperator.EQUALS, "OK")
        );

        assertThatThrownBy(() -> engine.assertResponse(json, testCase(assertions), "mutatedRequestPayload"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("JSON_PATH_EVALUATION_FAILED")
                .hasMessageContaining("mutatedRequestPayload");
    }

    @Test
    @DisplayName("FAIL: throws AssertionError on invalid JsonPath syntax (handlePathException)")
    void shouldThrowAssertionErrorWhenJsonPathIsInvalidSyntax() {
        String json = "{\"status\": \"OK\"}";
        List<JsonAssertion> assertions = List.of(
                assertion("$..[?", AssertionOperator.EQUALS, "OK")
        );

        assertThatThrownBy(() -> engine.assertResponse(json, testCase(assertions), "mutatedRequestPayload"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("JSON_PATH_RUNTIME_ERROR");
    }
}
