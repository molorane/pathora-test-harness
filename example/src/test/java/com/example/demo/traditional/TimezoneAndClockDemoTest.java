package com.example.demo.traditional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import io.github.molorane.pathora.testharness.engine.AssertionEngine;
import io.github.molorane.pathora.testharness.engine.JsonMutationEngine;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.JsonMutation;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import io.github.molorane.pathora.testharness.model.TestSuite;
import io.github.molorane.pathora.testharness.util.DateExpressionResolver;
import io.github.molorane.pathora.testharness.util.PathoraClock;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Demonstrates the different ways a user can configure timezones and clocks
 * in the Pathora Test Harness engine:
 *
 * <ol>
 *   <li><b>Way 1: Test Suite JSON Level Timezone</b> — Configured on the root {@link TestSuite} record.</li>
 *   <li><b>Way 2: Test Case JSON Level Timezone Override</b> — Configured on individual {@link RuleTestCase} records.</li>
 *   <li><b>Way 3: Programmatic Global Clock Freezing with ZoneId</b> — Using {@code PathoraClock.freeze(Instant/LocalDate, ZoneId)}.</li>
 *   <li><b>Way 4: Programmatic Thread-Scoped Clock Freezing</b> — Using {@code PathoraClock.freezeThread(Instant/LocalDate, ZoneId)}.</li>
 *   <li><b>Way 5: Programmatic Custom Clock Injection</b> — Using {@code PathoraClock.setClock(Clock)} or {@code PathoraClock.setThreadClock(Clock)}.</li>
 *   <li><b>Way 6: JVM System Property</b> — Using {@code -Dpathora.timezone=...} or {@code System.setProperty("pathora.timezone", ...)}.</li>
 *   <li><b>Way 7: Spring Environment / application.yml</b> — Configured via {@code pathora.timezone} property in {@code application.yml}.</li>
 * </ol>
 */
@SpringBootTest
class TimezoneAndClockDemoTest {

    @Autowired
    private AssertionEngine assertionEngine;

    @Autowired
    private JsonMutationEngine mutationEngine;

    @BeforeEach
    @AfterEach
    void resetClock() {
        PathoraClock.reset();
        System.clearProperty("pathora.timezone");
    }

    @Test
    @DisplayName("Way 1 & 2: Test Suite Level Timezone with Test Case Level Timezone Override")
    void demonstrateSuiteAndTestCaseLevelTimezone() {
        // Suite configured with Africa/Johannesburg (UTC+2)
        TestSuite suite = new TestSuite(
                "templates/requests/complex-policy-request.json",
                null,
                List.of(),
                "Africa/Johannesburg"
        );
        assertEquals("Africa/Johannesburg", suite.timezone());

        // Test Case inherits suite timezone or overrides with its own (e.g. Asia/Tokyo UTC+9)
        RuleTestCase testCaseWithTokyoZone = new RuleTestCase(
                "Tokyo Evaluation Test",
                "Evaluates assertions in Tokyo timezone",
                "policy-evaluation-service",
                List.of(new JsonMutation("$.effectiveDate", "{{$CURRENT_DATE}}")),
                List.of(new JsonAssertion("$.effectiveDate", AssertionOperator.DATE_EQUALS, "{{$CURRENT_DATE}}", "Matches today in Tokyo", null)),
                "Asia/Tokyo"
        );

        String responsePayload = """
                {
                    "effectiveDate": "%s"
                }
                """.formatted(LocalDate.now(ZoneId.of("Asia/Tokyo")));

        // AssertionEngine automatically activates the test case timezone on the execution thread
        assertDoesNotThrow(() -> assertionEngine.assertResponse(responsePayload, testCaseWithTokyoZone));
    }

    @Test
    @DisplayName("Way 3: Programmatic Global Clock Freezing with ZoneId (Time-Travel Testing)")
    void demonstrateGlobalClockFreezingWithZone() {
        // Freeze time deterministically globally at 2028 Leap Day in UTC+2 (Johannesburg)
        LocalDate leapDay = LocalDate.of(2028, 2, 29);
        ZoneId joburg = ZoneId.of("Africa/Johannesburg");
        PathoraClock.freeze(leapDay, joburg);

        assertAll(
                () -> assertEquals(joburg, PathoraClock.getZoneId()),
                () -> assertEquals(LocalDate.of(2028, 2, 29), PathoraClock.today()),
                () -> assertEquals("2028-02-29", DateExpressionResolver.resolveToString("{{$CURRENT_DATE}}")),
                () -> assertEquals("2028-03-30", DateExpressionResolver.resolveToString("{{$CURRENT_DATE + 30d}}")),
                () -> assertEquals("2003-02-28", DateExpressionResolver.resolveToString("{{$CURRENT_DATE - 25y}}"))
        );

        String sampleResponse = """
                {
                    "effectiveDate": "2028-02-29",
                    "expiryDate": "2028-03-30"
                }
                """;

        RuleTestCase testCase = new RuleTestCase(
                "Leap Day Frozen Time Assertion",
                "Verifies date resolution against frozen clock",
                "policy-evaluation-service",
                List.of(),
                List.of(
                        new JsonAssertion("$.effectiveDate", AssertionOperator.DATE_EQUALS, "{{$CURRENT_DATE}}", "Equals frozen date", null),
                        new JsonAssertion("$.effectiveDate", AssertionOperator.IS_TODAY, null, "Is today", null),
                        new JsonAssertion("$.expiryDate", AssertionOperator.DATE_EQUALS, "{{$CURRENT_DATE + 30d}}", "Equals +30d", null)
                )
        );

        assertDoesNotThrow(() -> assertionEngine.assertResponse(sampleResponse, testCase));
    }

    @Test
    @DisplayName("Way 4: Programmatic Thread-Scoped Clock Freezing for Parallel Isolation")
    void demonstrateThreadScopedClockFreezing() throws InterruptedException {
        // Set global time to 2026
        Instant globalInstant = Instant.parse("2026-01-01T00:00:00Z");
        PathoraClock.freeze(globalInstant, ZoneOffset.UTC);

        // Freeze current thread to 2030 in America/New_York (UTC-5)
        Instant threadInstant = Instant.parse("2030-07-04T16:00:00Z");
        ZoneId newYork = ZoneId.of("America/New_York");
        PathoraClock.freezeThread(threadInstant, newYork);

        // Current thread sees the thread-scoped clock and timezone
        assertEquals(newYork, PathoraClock.getZoneId());
        assertEquals(LocalDate.of(2030, 7, 4), PathoraClock.today());
        assertEquals("2030-07-04", DateExpressionResolver.resolveToString("{{$CURRENT_DATE}}"));

        // Another concurrent thread still sees the global clock in UTC
        Thread worker = new Thread(() -> {
            assertEquals(ZoneOffset.UTC, PathoraClock.getZoneId());
            assertEquals(LocalDate.of(2026, 1, 1), PathoraClock.today());
        });
        worker.start();
        worker.join();

        PathoraClock.clearThreadClock();
        assertEquals(ZoneOffset.UTC, PathoraClock.getZoneId());
        assertEquals(LocalDate.of(2026, 1, 1), PathoraClock.today());
    }

    @Test
    @DisplayName("Way 5: Programmatic Custom Clock Injection (Clock.system / Clock.offset)")
    void demonstrateCustomClockInjection() {
        // Set an active Clock using any java.time.Clock instance
        Clock londonClock = Clock.system(ZoneId.of("Europe/London"));
        PathoraClock.setClock(londonClock);

        assertEquals(ZoneId.of("Europe/London"), PathoraClock.getZoneId());
        assertNotNull(PathoraClock.today());

        // Dynamic tokens resolve using Europe/London
        String dateString = DateExpressionResolver.resolveToString("{{$CURRENT_DATE}}");
        assertEquals(LocalDate.now(londonClock).toString(), dateString);
    }

    @Test
    @DisplayName("Way 6: JVM System Property Configuration (pathora.timezone)")
    void demonstrateSystemPropertyTimezone() {
        System.setProperty("pathora.timezone", "Australia/Sydney");
        PathoraClock.reset(); // Re-initializes clock from system properties

        assertEquals(ZoneId.of("Australia/Sydney"), PathoraClock.getZoneId());
        assertEquals(LocalDate.now(ZoneId.of("Australia/Sydney")), PathoraClock.today());
    }

    @Test
    @DisplayName("Way 7: Spring Environment / application.yml Configuration (pathora.timezone)")
    void demonstrateSpringApplicationYmlTimezone() {
        // PathoraClock.setTimezone can be set directly from @Value("${pathora.timezone}") in Spring configuration
        PathoraClock.setTimezone("Africa/Johannesburg");

        assertEquals(ZoneId.of("Africa/Johannesburg"), PathoraClock.getZoneId());
        assertEquals(LocalDate.now(ZoneId.of("Africa/Johannesburg")), PathoraClock.today());
    }

    @Test
    @DisplayName("Dynamic Date Expression Mutations & Assertions Demo")
    void demonstrateDynamicDateMutationsAndAssertions() {
        PathoraClock.freeze(Instant.parse("2026-10-07T10:00:00Z"), ZoneOffset.UTC);

        String basePayload = """
                {
                    "policyNumber": "POL-100",
                    "effectiveDate": "2020-01-01",
                    "expirationDate": "2021-01-01"
                }
                """;

        List<JsonMutation> mutations = List.of(
                new JsonMutation("$.effectiveDate", "{{$CURRENT_DATE}}"),
                new JsonMutation("$.expirationDate", "{{$CURRENT_DATE + 365d}}")
        );

        String mutated = mutationEngine.apply(basePayload, mutations, "policy-demo.json", "evaluatePolicy");

        RuleTestCase testCase = new RuleTestCase(
                "Dynamic Mutation Validation",
                "Verifies dynamic token mutations and assertions",
                "policy-evaluation-service",
                mutations,
                List.of(
                        new JsonAssertion("$.effectiveDate", AssertionOperator.DATE_EQUALS, "{{$CURRENT_DATE}}", "Effective date matches today", null),
                        new JsonAssertion("$.expirationDate", AssertionOperator.DATE_EQUALS, "{{$CURRENT_DATE + 365d}}", "Expiration date matches +365d", null),
                        new JsonAssertion(
                                "$.effectiveDate",
                                AssertionOperator.DATE_BETWEEN,
                                Map.of(
                                        "min", "{{$CURRENT_DATE - 1d}}",
                                        "max", "{{$CURRENT_DATE + 1d}}"
                                ),
                                "Effective date is in +/- 1d window",
                                null
                        )
                )
        );

        assertDoesNotThrow(() -> assertionEngine.assertResponse(mutated, testCase));
    }
}

