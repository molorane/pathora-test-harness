package io.github.molorane.pathora.testharness.util;

import java.time.*;

/**
 * Centralized, thread-safe clock holder for the Pathora Test Harness.
 *
 * <p>Supports global and thread-scoped clocks, deterministic time-travel testing,
 * and custom timezone configurations.</p>
 */
public final class PathoraClock {

    private static final String TIMEZONE_PROPERTY = "pathora.timezone";
    private static volatile Clock globalClock = initDefaultClock();
    private static final ThreadLocal<Clock> THREAD_CLOCK = new ThreadLocal<>();

    private PathoraClock() {
    }

    private static Clock initDefaultClock() {
        String sysZone = System.getProperty(TIMEZONE_PROPERTY);
        if (sysZone == null || sysZone.isBlank()) {
            sysZone = System.getenv("PATHORA_TIMEZONE");
        }
        if (sysZone != null && !sysZone.isBlank()) {
            try {
                return Clock.system(ZoneId.of(sysZone.trim()));
            } catch (Exception ignored) {
                // Fall back to system default on invalid zone
            }
        }
        return Clock.systemDefaultZone();
    }

    /**
     * Gets the active clock (thread-scoped clock takes precedence over global clock).
     *
     * @return the active {@link Clock} instance
     */
    public static Clock getClock() {
        Clock tc = THREAD_CLOCK.get();
        return tc != null ? tc : globalClock;
    }

    /**
     * Gets the active {@link ZoneId} associated with the active clock.
     *
     * @return the active timezone ID
     */
    public static ZoneId getZoneId() {
        return getClock().getZone();
    }

    /**
     * Sets the global clock used by default across all threads.
     *
     * @param clock the clock to use, or {@code null} to restore default
     */
    public static void setClock(Clock clock) {
        globalClock = (clock != null) ? clock : initDefaultClock();
    }

    /**
     * Sets the global timezone for PathoraClock by timezone identifier string.
     *
     * @param timezone the timezone identifier (e.g. "Africa/Johannesburg", "Asia/Tokyo"), or {@code null} to reset
     */
    public static void setTimezone(String timezone) {
        if (timezone != null && !timezone.isBlank()) {
            setClock(Clock.system(ZoneId.of(timezone.trim())));
        } else {
            setClock(null);
        }
    }

    /**
     * Sets the global timezone for PathoraClock by {@link ZoneId}.
     *
     * @param zoneId the timezone ID, or {@code null} to reset
     */
    public static void setTimezone(ZoneId zoneId) {
        if (zoneId != null) {
            setClock(Clock.system(zoneId));
        } else {
            setClock(null);
        }
    }

    /**
     * Sets a thread-scoped clock, taking precedence on the calling thread.
     *
     * @param clock the clock to set for this thread
     */
    public static void setThreadClock(Clock clock) {
        if (clock != null) {
            THREAD_CLOCK.set(clock);
        } else {
            THREAD_CLOCK.remove();
        }
    }

    /**
     * Clears the thread-scoped clock for the calling thread.
     */
    public static void clearThreadClock() {
        THREAD_CLOCK.remove();
    }

    /**
     * Freezes time globally at the specified instant in UTC.
     *
     * @param instant the instant to freeze at
     */
    public static void freeze(Instant instant) {
        freeze(instant, ZoneOffset.UTC);
    }

    /**
     * Freezes time globally at the specified instant in the given timezone.
     *
     * @param instant the instant to freeze at
     * @param zoneId  the timezone to use
     */
    public static void freeze(Instant instant, ZoneId zoneId) {
        ZoneId zone = (zoneId != null) ? zoneId : ZoneOffset.UTC;
        setClock(Clock.fixed(instant, zone));
    }

    /**
     * Freezes time globally at the start of the specified local date in UTC.
     *
     * @param date the date to freeze at
     */
    public static void freeze(LocalDate date) {
        freeze(date, ZoneOffset.UTC);
    }

    /**
     * Freezes time globally at the start of the specified local date in the given timezone.
     *
     * @param date   the date to freeze at
     * @param zoneId the timezone to use
     */
    public static void freeze(LocalDate date, ZoneId zoneId) {
        ZoneId zone = (zoneId != null) ? zoneId : ZoneOffset.UTC;
        freeze(date.atStartOfDay(zone).toInstant(), zone);
    }

    /**
     * Freezes time for the calling thread at the specified instant in UTC.
     *
     * @param instant the instant to freeze at
     */
    public static void freezeThread(Instant instant) {
        freezeThread(instant, ZoneOffset.UTC);
    }

    /**
     * Freezes time for the calling thread at the specified instant in the given timezone.
     *
     * @param instant the instant to freeze at
     * @param zoneId  the timezone to use
     */
    public static void freezeThread(Instant instant, ZoneId zoneId) {
        ZoneId zone = (zoneId != null) ? zoneId : ZoneOffset.UTC;
        setThreadClock(Clock.fixed(instant, zone));
    }

    /**
     * Freezes time for the calling thread at the start of the specified date.
     *
     * @param date   the date to freeze at
     * @param zoneId the timezone to use
     */
    public static void freezeThread(LocalDate date, ZoneId zoneId) {
        ZoneId zone = (zoneId != null) ? zoneId : ZoneOffset.UTC;
        freezeThread(date.atStartOfDay(zone).toInstant(), zone);
    }

    /**
     * Obtains the current instant from the active clock.
     *
     * @return current {@link Instant}
     */
    public static Instant instant() {
        return getClock().instant();
    }

    /**
     * Obtains the current local date from the active clock.
     *
     * @return current {@link LocalDate}
     */
    public static LocalDate today() {
        return LocalDate.now(getClock());
    }

    /**
     * Obtains the current local datetime from the active clock.
     *
     * @return current {@link LocalDateTime}
     */
    public static LocalDateTime now() {
        return LocalDateTime.now(getClock());
    }

    /**
     * Obtains the current zoned datetime from the active clock.
     *
     * @return current {@link ZonedDateTime}
     */
    public static ZonedDateTime nowZoned() {
        return ZonedDateTime.now(getClock());
    }

    /**
     * Resets the clock to the initial default state and clears any thread-local clocks.
     */
    public static void reset() {
        globalClock = initDefaultClock();
        THREAD_CLOCK.remove();
    }
}

