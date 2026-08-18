package io.github.molorane.pathora.testharness.engine.integration;

import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;

class StringOperatorsIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("INTEGRATION: STRING OPERATORS (STARTS_WITH, ENDS_WITH, REGEX_MATCH)")
    void shouldTestAllStringOperatorsWithJsonFile() {
        String jsonPayload = loadJson("string_operators.json");

        List<JsonAssertion> assertions = List.of(
                assertion("$.productCode", AssertionOperator.STARTS_WITH, "PRD-"),
                assertion("$.versionStr", AssertionOperator.ENDS_WITH, "-RELEASE"),
                assertion("$.email", AssertionOperator.REGEX_MATCH, "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        );

        RuleTestCase ruleTestCase = testCase("String Operators Test", assertions);

        assertThatNoException().isThrownBy(() -> engine.assertResponse(jsonPayload, ruleTestCase));
    }
}
