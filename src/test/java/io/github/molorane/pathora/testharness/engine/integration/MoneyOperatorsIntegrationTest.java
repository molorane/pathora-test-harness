package io.github.molorane.pathora.testharness.engine.integration;

import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;

class MoneyOperatorsIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("INTEGRATION: MONEY OPERATORS (MONEY_EQUALS, MONEY_GREATER_THAN, MONEY_GREATER_THAN_OR_EQUALS, MONEY_LESS_THAN, MONEY_LESS_THAN_OR_EQUALS, MONEY_BETWEEN)")
    void shouldTestAllMoneyOperatorsWithJsonFile() {
        String jsonPayload = loadJson("money_operators.json");

        List<JsonAssertion> assertions = List.of(
                assertion("$.exactAmount", AssertionOperator.MONEY_EQUALS, "100.5"),
                assertion("$.currencyAmount", AssertionOperator.MONEY_EQUALS, Map.of("amount", 250.75, "currency", "USD")),
                assertion("$.largeBalance", AssertionOperator.MONEY_GREATER_THAN, 10000.00),
                assertion("$.thresholdAmount", AssertionOperator.MONEY_GREATER_THAN_OR_EQUALS, 1000.00),
                assertion("$.smallFee", AssertionOperator.MONEY_LESS_THAN, 10.00),
                assertion("$.exactAmount", AssertionOperator.MONEY_LESS_THAN_OR_EQUALS, 100.50),
                assertion("$.currencyAmount", AssertionOperator.MONEY_BETWEEN, Map.of("min", 200.00, "max", 300.00)),
                assertion("$.interestAccrual", AssertionOperator.MONEY_EQUALS_WITH_TOLERANCE, Map.of("expected", 100.00, "tolerance", 0.01))
        );

        RuleTestCase ruleTestCase = testCase("Money Operators Test", assertions);

        assertThatNoException().isThrownBy(() -> engine.assertResponse(jsonPayload, ruleTestCase));
    }
}
