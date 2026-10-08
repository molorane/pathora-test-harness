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
    @DisplayName("INTEGRATION: STRING OPERATORS")
    void shouldTestAllStringOperatorsWithJsonFile() {
        String jsonPayload = loadJson("string_operators.json");

        List<JsonAssertion> assertions = List.of(
            assertion("$.productCode", AssertionOperator.STARTS_WITH, "PRD-"),
            assertion("$.versionStr", AssertionOperator.ENDS_WITH, "-RELEASE"),
            assertion("$.email", AssertionOperator.REGEX_MATCH, "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"),
            assertion("$.productCode", AssertionOperator.STRING_CONTAINS, "2026"),
            assertion("$.productCode", AssertionOperator.STRING_CONTAINS_IGNORE_CASE, "prd"),
            assertion("$.status", AssertionOperator.STRING_EQUALS_IGNORE_CASE, "approved"),
            assertion("$.productCode", AssertionOperator.STRING_STARTS_WITH_IGNORE_CASE, "prd-"),
            assertion("$.productCode", AssertionOperator.STRING_STARTS_WITH_ANY, List.of("INV-", "PRD-", "ORD-")),
            assertion("$.versionStr", AssertionOperator.STRING_ENDS_WITH_ANY, List.of("-BETA", "-RELEASE", "-SNAPSHOT")),
            assertion("$.versionStr", AssertionOperator.STRING_ENDS_WITH_ANY_IGNORE_CASE, List.of("-beta", "-release")),
            assertion("$.sentence", AssertionOperator.STRING_CONTAINS_WHITESPACE, null),
            assertion("$.status", AssertionOperator.STRING_EQUALS_ANY, List.of("PENDING", "APPROVED", "REJECTED")),
            assertion("$.status", AssertionOperator.STRING_EQUALS_ANY_IGNORE_CASE, List.of("pending", "approved", "rejected")),
            assertion("$.mixedCaseText", AssertionOperator.IS_STRING_MIXED_CASE, null),
            assertion("$.alphanumericCode", AssertionOperator.IS_STRING_ALPHA_NUMERIC, null),
            assertion("$.alphaSpaceName", AssertionOperator.IS_STRING_ALPHA_SPACE, null),
            assertion("$.alphaOnly", AssertionOperator.IS_STRING_ALPHA, null),
            assertion("$.blankField", AssertionOperator.IS_STRING_BLANK, null),
            assertion("$.emptyField", AssertionOperator.IS_STRING_EMPTY, null),
            assertion("$.nonEmptyList", AssertionOperator.IS_STRING_NONE_EMPTY, null),
            assertion("$.listWithBlank", AssertionOperator.IS_ANY_STRING_BLANK, null)
        );

        RuleTestCase ruleTestCase = testCase("String Operators Test", assertions);

        assertThatNoException().isThrownBy(() -> engine.assertResponse(jsonPayload, ruleTestCase));
    }
}
