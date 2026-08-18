package io.github.molorane.pathora.testharness.engine.integration;

import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;

class DateOperatorsIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("INTEGRATION: DATE OPERATORS (BEFORE, AFTER, BEFORE_NOW, AFTER_NOW, WITHIN_LAST, WITHIN_NEXT, BEFORE_DURATION, AFTER_DURATION, TIME_BEFORE, TIME_AFTER)")
    void shouldTestAllDateOperatorsWithJsonFile() {
        String jsonPayload = loadJson("date_operators.json");

        List<JsonAssertion> assertions = List.of(
                assertion("$.pastDate", AssertionOperator.DATE_BEFORE, "2025-01-01"),
                assertion("$.futureDate", AssertionOperator.DATE_AFTER, "2025-01-01"),
                assertion("$.pastDate", AssertionOperator.DATE_BEFORE_NOW, null),
                assertion("$.futureDate", AssertionOperator.DATE_AFTER_NOW, null),
                assertion("$.recentPastDateTime", AssertionOperator.DATE_WITHIN_LAST, Map.of("amount", 20, "unit", "YEARS")),
                assertion("$.distantFutureDateTime", AssertionOperator.DATE_WITHIN_NEXT, Map.of("amount", 100, "unit", "YEARS")),
                assertion(null, AssertionOperator.DATE_BEFORE_DURATION, Map.of(
                        "basePath", "$.baseDate",
                        "comparePath", "$.earlierDate",
                        "amount", 10,
                        "unit", "DAYS"
                )),
                assertion(null, AssertionOperator.DATE_AFTER_DURATION, Map.of(
                        "basePath", "$.baseDate",
                        "comparePath", "$.laterDate",
                        "amount", 10,
                        "unit", "DAYS"
                )),
                assertion("$.dtEarlier", AssertionOperator.DATETIME_BEFORE, "2026-08-18T12:00:00"),
                assertion("$.dtLater", AssertionOperator.DATETIME_AFTER, "2026-08-18T12:00:00")
        );

        RuleTestCase ruleTestCase = testCase("Date Operators Test", assertions);

        assertThatNoException().isThrownBy(() -> engine.assertResponse(jsonPayload, ruleTestCase));
    }
}
