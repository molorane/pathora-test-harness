package io.github.molorane.pathora.testharness.engine.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.engine.AssertionEngine;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import io.github.molorane.pathora.testharness.util.PathoraClock;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DynamicDateAssertionIntegrationTest {

    private AssertionEngine assertionEngine;

    @BeforeEach
    void setUp() {
        assertionEngine = new AssertionEngine();
        PathoraClock.freeze(Instant.parse("2026-10-07T12:00:00Z"));
    }

    @AfterEach
    void tearDown() {
        PathoraClock.reset();
    }

    @Test
    void testDateEqualsWithDynamicCurrentDate() {
        String response = """
                {
                    "decisionDate": "2026-10-07",
                    "expiryDate": "2026-11-06"
                }
                """;

        List<JsonAssertion> assertions = List.of(
                new JsonAssertion("$.decisionDate", AssertionOperator.DATE_EQUALS, "{{$CURRENT_DATE}}", "Matches today", null),
                new JsonAssertion("$.expiryDate", AssertionOperator.DATE_EQUALS, "{{$CURRENT_DATE + 30d}}", "Matches +30d", null),
                new JsonAssertion("$.decisionDate", AssertionOperator.IS_TODAY, null, "Is today", null)
        );

        RuleTestCase testCase = new RuleTestCase("test-date", "desc", "op", List.of(), assertions);

        assertDoesNotThrow(() -> assertionEngine.assertResponse(response, testCase));
    }

    @Test
    void testDateBetweenWithDynamicTokens() {
        String response = """
                {
                    "effectiveDate": "2026-10-10"
                }
                """;

        List<JsonAssertion> assertions = List.of(
                new JsonAssertion(
                        "$.effectiveDate",
                        AssertionOperator.DATE_BETWEEN,
                        Map.of(
                                "min", "{{$CURRENT_DATE - 5d}}",
                                "max", "{{$CURRENT_DATE + 5d}}"
                        ),
                        "Date in window",
                        null
                )
        );

        RuleTestCase testCase = new RuleTestCase("test-between", "desc", "op", List.of(), assertions);

        assertDoesNotThrow(() -> assertionEngine.assertResponse(response, testCase));
    }

    @Test
    void testDateEqualsMismatchThrowsHarnessAssertionException() {
        String response = """
                {
                    "decisionDate": "2026-10-01"
                }
                """;

        List<JsonAssertion> assertions = List.of(
                new JsonAssertion("$.decisionDate", AssertionOperator.DATE_EQUALS, "{{$CURRENT_DATE}}", "Matches today", null)
        );

        RuleTestCase testCase = new RuleTestCase("test-fail", "desc", "op", List.of(), assertions);

        assertThrows(HarnessAssertionException.class, () -> assertionEngine.assertResponse(response, testCase));
    }
}

