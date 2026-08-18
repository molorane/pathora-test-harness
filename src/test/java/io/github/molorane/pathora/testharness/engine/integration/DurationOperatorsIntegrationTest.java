package io.github.molorane.pathora.testharness.engine.integration;

import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;

class DurationOperatorsIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("INTEGRATION: DURATION OPERATORS (DURATION_EQUALS, DURATION_GREATER_THAN, DURATION_LESS_THAN, DURATION_BETWEEN_DATES)")
    void shouldTestAllDurationOperatorsWithJsonFile() {
        String jsonPayload = loadJson("duration_operators.json");

        List<JsonAssertion> assertions = List.of(
                assertion(null, AssertionOperator.DURATION_EQUALS, Map.of(
                        "startPath", "$.startDate",
                        "endPath", "$.endDate",
                        "unit", "MINUTES",
                        "expected", 120
                )),
                assertion(null, AssertionOperator.DURATION_GREATER_THAN, Map.of(
                        "startPath", "$.startDate",
                        "endPath", "$.endDate",
                        "unit", "HOURS",
                        "value", 1
                )),
                assertion(null, AssertionOperator.DURATION_LESS_THAN, Map.of(
                        "startPath", "$.startDate",
                        "endPath", "$.endDate",
                        "unit", "HOURS",
                        "value", 5
                )),
                assertion(null, AssertionOperator.DURATION_BETWEEN, Map.of(
                        "startPath", "$.startDate",
                        "endPath", "$.endDate",
                        "unit", "HOURS",
                        "min", 1,
                        "max", 3
                ))
        );

        RuleTestCase ruleTestCase = testCase("Duration Operators Test", assertions);

        assertThatNoException().isThrownBy(() -> engine.assertResponse(jsonPayload, ruleTestCase));
    }
}
