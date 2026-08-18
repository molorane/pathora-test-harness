package io.github.molorane.pathora.testharness.engine.integration;

import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;

class ScalarOperatorsIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("INTEGRATION: SCALAR OPERATORS (EQUALS, NOT_EQUALS, GT, GTE, LT, LTE, BETWEEN)")
    void shouldTestAllScalarOperatorsWithJsonFile() {
        String jsonPayload = loadJson("scalar_operators.json");

        List<JsonAssertion> assertions = List.of(
                assertion("$.score", AssertionOperator.EQUALS, 85),
                assertion("$.status", AssertionOperator.NOT_EQUALS, "REJECTED"),
                assertion("$.score", AssertionOperator.GREATER_THAN, 80),
                assertion("$.score", AssertionOperator.GREATER_THAN_OR_EQUALS, 85),
                assertion("$.negativeScore", AssertionOperator.LESS_THAN, 0),
                assertion("$.temperature", AssertionOperator.LESS_THAN_OR_EQUALS, 36.6),
                assertion("$.rating", AssertionOperator.BETWEEN, Map.of("min", 4.0, "max", 5.0))
        );

        RuleTestCase ruleTestCase = testCase("Scalar Operators Test", assertions);

        assertThatNoException().isThrownBy(() -> engine.assertResponse(jsonPayload, ruleTestCase));
    }
}
