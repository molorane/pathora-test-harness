package io.github.molorane.pathora.testharness.engine.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;

class DateOperatorsIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("INTEGRATION: DATE OPERATORS (BEFORE, AFTER, IS_PAST_DATE, IS_FUTURE_DATE, WITHIN_LAST, WITHIN_NEXT, BEFORE_DURATION, AFTER_DURATION, TIME_BEFORE, TIME_AFTER)")
    void shouldTestAllDateOperatorsWithJsonFile() {
        String jsonPayload = loadJson("date_operators.json");

        List<JsonAssertion> assertions = List.of(
                assertion("$.pastDate", AssertionOperator.DATE_BEFORE, "2025-01-01"),
                assertion("$.futureDate", AssertionOperator.DATE_AFTER, "2025-01-01"),
                assertion("$.pastDate", AssertionOperator.IS_PAST_DATE, null),
                assertion("$.futureDate", AssertionOperator.IS_FUTURE_DATE, null),
                assertion("$.pastDate", AssertionOperator.DATE_WITHIN_LAST, Map.of("amount", 30, "unit", "YEARS")),
                assertion("$.futureDate", AssertionOperator.DATE_WITHIN_NEXT, Map.of("amount", 100, "unit", "YEARS")),
                assertion("$.recentPastDateTime", AssertionOperator.DATETIME_WITHIN_LAST, Map.of("amount", 20, "unit", "YEARS")),
                assertion("$.distantFutureDateTime", AssertionOperator.DATETIME_WITHIN_NEXT, Map.of("amount", 100, "unit", "YEARS")),
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
                assertion("$.dtLater", AssertionOperator.DATETIME_AFTER, "2026-08-18T12:00:00"),
                assertion("$.pastDate", AssertionOperator.DATE_YEAR_EQUALS, 2000),
                assertion("$.recentPastDateTime", AssertionOperator.DATE_YEAR_EQUALS, "2020"),
                assertion("$.pastDate", AssertionOperator.DATE_MONTH_EQUALS, "JANUARY"),
                assertion("$.dtEarlier", AssertionOperator.DATE_MONTH_EQUALS, "August"),
                assertion("$.dtEarlier", AssertionOperator.DATE_MONTH_EQUALS, 8),
                assertion("$.dtEarlier", AssertionOperator.DATE_DAY_EQUALS, 18),
                assertion("$.pastDate", AssertionOperator.DATE_DAY_EQUALS, 1),
                assertion("$.dtEarlier", AssertionOperator.DATE_DAY_OF_WEEK_EQUALS, "TUESDAY"),
                assertion("$.dtEarlier", AssertionOperator.DATE_DAY_OF_WEEK_EQUALS, "Tuesday"),
                assertion("$.dtEarlier", AssertionOperator.DATE_DAY_OF_WEEK_EQUALS, 2),
                assertion("$.dtEarlier", AssertionOperator.DATE_EQUALS, "2026-08-18"),
                assertion("$.dtEarlier", AssertionOperator.TIME_EQUALS, "08:00:00"),
                assertion("$.dtEarlier", AssertionOperator.TIME_EQUALS, "08:00"),
                assertion("$.dtEarlier", AssertionOperator.TIME_BEFORE, "09:00:00"),
                assertion("$.dtEarlier", AssertionOperator.TIME_AFTER, "07:00:00"),
                assertion("$.dtEarlier", AssertionOperator.TIME_BETWEEN, Map.of("min", "07:00:00", "max", "09:00:00")),
                assertion("$.dtEarlier", AssertionOperator.DATE_HOUR_EQUALS, 8),
                assertion("$.dtEarlier", AssertionOperator.DATE_MINUTE_EQUALS, 0),
                assertion("$.dtEarlier", AssertionOperator.DATE_SECOND_EQUALS, 0)
        );

        RuleTestCase ruleTestCase = testCase("Date Operators Test", assertions);

        assertThatNoException().isThrownBy(() -> engine.assertResponse(jsonPayload, ruleTestCase));
    }
}
