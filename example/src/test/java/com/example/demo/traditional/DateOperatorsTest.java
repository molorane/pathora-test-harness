package com.example.demo.traditional;

import com.example.demo.dto.PolicyRequest;
import com.example.demo.dto.PolicyResponse;
import com.example.demo.service.ComplexPolicyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DateOperatorsTest {

    private static final Path REQUEST_PATH = Paths.get("templates/requests/complex-policy-request.json");

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ComplexPolicyService complexPolicyService;

    @Test
    @DisplayName("Date Component & Range Extraction Test")
    void dateComponentAndRangeExtractionTest() throws Exception {
        PolicyResponse response = evaluatePolicy();

        LocalDate effectiveDate = response.policyHeaderEffectiveDate();
        LocalDate expirationDate = response.expirationTimestamp().toLocalDate();
        LocalDate currentDate = response.currentDate();
        LocalDate today = LocalDate.now();

        assertAll(
                () -> assertEquals(2026, effectiveDate.getYear(), "Effective date year must be 2026"),
                () -> assertEquals(1, effectiveDate.getMonthValue(), "Effective date month must be January (1)"),
                () -> assertEquals(1, effectiveDate.getDayOfMonth(), "Effective date day must be 1"),
                () -> assertEquals(DayOfWeek.THURSDAY, effectiveDate.getDayOfWeek(), "Effective date day of week must be Thursday"),
                () -> assertEquals(LocalDate.of(2026, 1, 1), effectiveDate, "Effective date exactly equals 2026-01-01"),
                () -> assertTrue(
                        isBetweenInclusive(effectiveDate, LocalDate.of(2025, 1, 1), LocalDate.of(2027, 1, 1)),
                        "Effective date falls between 2025 and 2027"
                ),
                () -> assertTrue(expirationDate.isAfter(LocalDate.of(2026, 1, 1)), "Expiration date is after effective date"),
                () -> assertEquals(today, currentDate, "Current date equals system date today"),
                () -> assertTrue(
                        isWithinLastDays(currentDate, 1, today),
                        "Current date falls within last 1 day"
                )
        );
    }

    @Test
    @DisplayName("Time Component & Ordering Test")
    void timeComponentAndOrderingTest() throws Exception {
        PolicyResponse response = evaluatePolicy();

        LocalTime evaluationTime = response.evaluationTimestamp().toLocalTime();

        assertAll(
                () -> assertEquals(12, evaluationTime.getHour(), "Evaluation timestamp hour component equals 12"),
                () -> assertEquals(30, evaluationTime.getMinute(), "Evaluation timestamp minute component equals 30"),
                () -> assertEquals(45, evaluationTime.getSecond(), "Evaluation timestamp second component equals 45"),
                () -> assertEquals(LocalTime.of(12, 30, 45), evaluationTime, "Evaluation timestamp time component equals 12:30:45"),
                () -> assertTrue(evaluationTime.isAfter(LocalTime.of(8, 0)), "Evaluation timestamp is after 08:00 morning"),
                () -> assertTrue(evaluationTime.isBefore(LocalTime.of(18, 0)), "Evaluation timestamp is before 18:00:00 evening"),
                () -> assertTrue(
                        isBetweenInclusive(evaluationTime, LocalTime.of(8, 0), LocalTime.of(18, 0)),
                        "Evaluation timestamp is between 08:00:00 and 18:00:00"
                )
        );
    }

    @Test
    @DisplayName("DateTime Extended Operators Test")
    void dateTimeExtendedOperatorsTest() throws Exception {
        PolicyResponse response = evaluatePolicy();

        LocalDateTime evaluationDateTime = response.evaluationTimestamp();
        LocalDateTime currentDateTime = response.currentDateTime();
        LocalDateTime futureDateTime = response.futureDateTime();
        LocalDateTime now = LocalDateTime.now();

        assertAll(
                () -> assertEquals(
                        LocalDateTime.of(2026, 9, 15, 12, 30, 45),
                        evaluationDateTime,
                        "Evaluation timestamp matches expected datetime"
                ),
                () -> assertTrue(
                        evaluationDateTime.isBefore(LocalDateTime.of(2030, 1, 1, 0, 0)),
                        "Evaluation timestamp is before year 2030"
                ),
                () -> assertTrue(
                        isBetweenInclusive(
                                evaluationDateTime,
                                LocalDateTime.of(2026, 1, 1, 0, 0),
                                LocalDateTime.of(2027, 1, 1, 0, 0)
                        ),
                        "Evaluation timestamp falls between 2026 and 2027"
                ),
                () -> assertTrue(
                        Math.abs(ChronoUnit.SECONDS.between(LocalDateTime.of(2026, 9, 15, 12, 30, 40), evaluationDateTime)) <= 10,
                        "Evaluation timestamp matches within 10 seconds tolerance"
                ),
                () -> assertTrue(currentDateTime.isBefore(now), "Current datetime is in the past"),
                () -> assertTrue(
                        isWithinLastDays(currentDateTime, 1, now),
                        "Current datetime is within the last 1 day"
                ),
                () -> assertTrue(futureDateTime.isAfter(now), "Future datetime is in the future"),
                () -> assertTrue(
                        isWithinNextDays(futureDateTime, 60, now),
                        "Future datetime is within next 60 days"
                )
        );
    }

    private PolicyResponse evaluatePolicy() throws Exception {
        PolicyRequest request = objectMapper.readValue(Files.readString(REQUEST_PATH), PolicyRequest.class);
        return complexPolicyService.evaluatePolicy(request);
    }

    private static boolean isBetweenInclusive(LocalDate actual, LocalDate min, LocalDate max) {
        return !actual.isBefore(min) && !actual.isAfter(max);
    }

    private static boolean isBetweenInclusive(LocalTime actual, LocalTime min, LocalTime max) {
        return !actual.isBefore(min) && !actual.isAfter(max);
    }

    private static boolean isBetweenInclusive(LocalDateTime actual, LocalDateTime min, LocalDateTime max) {
        return !actual.isBefore(min) && !actual.isAfter(max);
    }

    private static boolean isWithinLastDays(LocalDate actual, long amount, LocalDate now) {
        return !actual.isBefore(now.minusDays(amount)) && !actual.isAfter(now);
    }

    private static boolean isWithinLastDays(LocalDateTime actual, long amount, LocalDateTime now) {
        return !actual.isBefore(now.minusDays(amount)) && !actual.isAfter(now);
    }

    private static boolean isWithinNextDays(LocalDateTime actual, long amount, LocalDateTime now) {
        return actual.isAfter(now) && !actual.isAfter(now.plusDays(amount));
    }
}

