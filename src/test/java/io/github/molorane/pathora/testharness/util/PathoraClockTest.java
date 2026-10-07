package io.github.molorane.pathora.testharness.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PathoraClockTest {

    @BeforeEach
    @AfterEach
    void resetClock() {
        PathoraClock.reset();
    }

    @Test
    void testDefaultClockIsNotNull() {
        assertNotNull(PathoraClock.getClock());
        assertNotNull(PathoraClock.getZoneId());
    }

    @Test
    void testFreezeInstant() {
        Instant fixed = Instant.parse("2026-05-15T10:30:00Z");
        PathoraClock.freeze(fixed, ZoneOffset.UTC);

        assertEquals(fixed, PathoraClock.instant());
        assertEquals(LocalDate.of(2026, 5, 15), PathoraClock.today());
        assertEquals(LocalDateTime.of(2026, 5, 15, 10, 30, 0), PathoraClock.now());
    }

    @Test
    void testFreezeLocalDate() {
        LocalDate date = LocalDate.of(2028, 2, 29); // leap day
        PathoraClock.freeze(date, ZoneOffset.UTC);

        assertEquals(date, PathoraClock.today());
        assertEquals(LocalDateTime.of(2028, 2, 29, 0, 0, 0), PathoraClock.now());
    }

    @Test
    void testFreezeWithZone() {
        Instant fixed = Instant.parse("2026-10-07T23:30:00Z");
        ZoneId tokyo = ZoneId.of("Asia/Tokyo"); // UTC+9 -> 2026-10-08 08:30:00
        PathoraClock.freeze(fixed, tokyo);

        assertEquals(tokyo, PathoraClock.getZoneId());
        assertEquals(LocalDate.of(2026, 10, 8), PathoraClock.today());
    }

    @Test
    void testThreadLocalClockOverridesGlobal() throws InterruptedException {
        Instant globalInstant = Instant.parse("2025-01-01T00:00:00Z");
        Instant threadInstant = Instant.parse("2026-06-01T12:00:00Z");

        PathoraClock.freeze(globalInstant, ZoneOffset.UTC);
        PathoraClock.freezeThread(threadInstant, ZoneOffset.UTC);

        assertEquals(threadInstant, PathoraClock.instant());

        // Verify other thread still sees global clock
        Thread otherThread = new Thread(() -> {
            assertEquals(globalInstant, PathoraClock.instant());
        });
        otherThread.start();
        otherThread.join();

        PathoraClock.clearThreadClock();
        assertEquals(globalInstant, PathoraClock.instant());
    }

    @Test
    void testSetClockNullRestoresDefault() {
        ZoneId originalZone = PathoraClock.getZoneId();
        PathoraClock.setClock(Clock.system(ZoneId.of("Pacific/Auckland")));
        assertEquals(ZoneId.of("Pacific/Auckland"), PathoraClock.getZoneId());

        PathoraClock.setClock(null);
        assertEquals(originalZone, PathoraClock.getZoneId());
    }
}

