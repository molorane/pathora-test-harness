package io.github.molorane.pathora.testharness.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.engine.AssertionEngine;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AssertionEngineErrorMessageTest {

    private AssertionEngine engine;

    @BeforeEach
    void setUp() {
        engine = new AssertionEngine();
    }

    @Test
    @DisplayName("JSON path failure includes expected value")
    void missingJsonPathIncludesExpectedValue() {
        JsonAssertion assertion = new JsonAssertion(
                "$.outputData.partyInContextResult.actions[0].type",
                AssertionOperator.EQUALS,
                "ASSESS_COMPLIANCE",
                null,
                null);

        RuleTestCase testCase = new RuleTestCase(
                "test",
                "req",
                "AssessComplianceEntryPoint",
                null,
                Collections.singletonList(assertion));

        String response = "{\"outputData\":{}}";

        assertThatThrownBy(() -> engine.assertResponse(response, testCase))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("JSON_PATH_EVALUATION_FAILED")
                .hasMessageContaining("Expected Value: ASSESS_COMPLIANCE")
                .hasMessageContaining("Mutated Request:");
    }

    @Test
    @DisplayName("JSON path failure includes request and mutated request")
    void missingJsonPathIncludesRequestContext() {
        JsonAssertion assertion = new JsonAssertion(
                "$.outputData.partyInContextResult.actions[0].type",
                AssertionOperator.EQUALS,
                "ASSESS_COMPLIANCE",
                null,
                null);

        RuleTestCase testCase = new RuleTestCase(
                "test",
                "req",
                "AssessComplianceEntryPoint",
                null,
                Collections.singletonList(assertion));

        String response = "{\"outputData\":{}}";
        String mutatedRequest = "{\"caseId\":\"A1\",\"flag\":true}";

        assertThatThrownBy(() -> engine.assertResponse(response, testCase, mutatedRequest))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("JSON_PATH_EVALUATION_FAILED")
                .hasMessageContaining("Expected Value: ASSESS_COMPLIANCE")
                .hasMessageContaining("Mutated Request:")
                .hasMessageContaining(mutatedRequest);
    }
}

