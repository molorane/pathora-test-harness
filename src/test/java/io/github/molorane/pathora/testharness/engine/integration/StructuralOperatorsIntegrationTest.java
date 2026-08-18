package io.github.molorane.pathora.testharness.engine.integration;

import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;

class StructuralOperatorsIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("INTEGRATION: STRUCTURAL OPERATORS (EXISTS, PATH_EXISTS, PATH_NOT_EXISTS)")
    void shouldTestAllStructuralOperatorsWithJsonFile() {
        String jsonPayload = loadJson("structural_operators.json");

        List<JsonAssertion> assertions = List.of(
                assertion("$.existingKey", AssertionOperator.EXISTS, null),
                assertion("$.activeFlag", AssertionOperator.PATH_EXISTS, null),
                assertion("$.nonExistentPath", AssertionOperator.PATH_NOT_EXISTS, null)
        );

        RuleTestCase ruleTestCase = testCase("Structural Operators Test", assertions);

        assertThatNoException().isThrownBy(() -> engine.assertResponse(jsonPayload, ruleTestCase));
    }
}
