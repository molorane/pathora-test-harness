package io.github.molorane.pathora.testharness.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DateExpressionResolverTest {

    @BeforeEach
    void setUp() {
        // Freeze to 2026-10-07T12:00:00Z (Wednesday)
        PathoraClock.freeze(Instant.parse("2026-10-07T12:00:00Z"));
    }

    @AfterEach
    void tearDown() {
        PathoraClock.reset();
    }

    @Test
    void testCurrentDateAndToday() {
        assertEquals("2026-10-07", DateExpressionResolver.resolve("{{$CURRENT_DATE}}"));
        assertEquals("2026-10-07", DateExpressionResolver.resolve("{{$TODAY}}"));
        assertEquals("2026-10-07", DateExpressionResolver.resolve("$CURRENT_DATE"));
        assertEquals("2026-10-07", DateExpressionResolver.resolve("$TODAY"));
    }

    @Test
    void testCurrentDateTimeAndNow() {
        assertEquals("2026-10-07T12:00:00Z", DateExpressionResolver.resolve("{{$CURRENT_DATETIME}}"));
        assertEquals("2026-10-07T12:00:00Z", DateExpressionResolver.resolve("{{$NOW}}"));
        assertEquals("2026-10-07T12:00:00Z", DateExpressionResolver.resolve("$CURRENT_DATETIME"));
        assertEquals("2026-10-07T12:00:00Z", DateExpressionResolver.resolve("$NOW"));
    }

    @Test
    void testEpochMillisAndSeconds() {
        assertEquals(1791374400000L, DateExpressionResolver.resolve("{{$EPOCH_MILLIS}}"));
        assertEquals(1791374400000L, DateExpressionResolver.resolve("$EPOCH_MILLIS"));
        assertEquals(1791374400L, DateExpressionResolver.resolve("{{$EPOCH_SECONDS}}"));
        assertEquals(1791374400L, DateExpressionResolver.resolve("$EPOCH_SECONDS"));
    }

    @Test
    void testDateOffsets() {
        assertEquals("2026-11-06", DateExpressionResolver.resolve("{{$CURRENT_DATE + 30d}}"));
        assertEquals("2026-09-27", DateExpressionResolver.resolve("{{$CURRENT_DATE - 10d}}"));
        assertEquals("2026-10-21", DateExpressionResolver.resolve("{{$CURRENT_DATE + 2w}}"));
        assertEquals("2027-01-07", DateExpressionResolver.resolve("{{$CURRENT_DATE + 3m}}"));
        assertEquals("2001-10-07", DateExpressionResolver.resolve("{{$CURRENT_DATE - 25y}}"));
        assertEquals("2027-10-07", DateExpressionResolver.resolve("{{$TODAY + 1y}}"));
    }

    @Test
    void testDateTimeOffsets() {
        assertEquals("2026-10-07T14:00:00Z", DateExpressionResolver.resolve("{{$CURRENT_DATETIME + 2h}}"));
        assertEquals("2026-10-07T11:30:00Z", DateExpressionResolver.resolve("{{$NOW - 30min}}"));
        assertEquals("2026-10-07T12:00:45Z", DateExpressionResolver.resolve("{{$NOW + 45s}}"));
    }

    @Test
    void testCustomFormatSpecifier() {
        assertEquals("07/10/2026", DateExpressionResolver.resolve("{{$CURRENT_DATE:dd/MM/yyyy}}"));
        assertEquals("12-10-2026", DateExpressionResolver.resolve("{{$CURRENT_DATE + 5d:dd-MM-yyyy}}"));
        assertEquals("2026/10/07 12:00", DateExpressionResolver.resolve("{{$NOW:yyyy/MM/dd HH:mm}}"));
    }

    @Test
    void testEmbeddedExpressions() {
        assertEquals("REF-2026-10-07-END", DateExpressionResolver.resolve("REF-{{$CURRENT_DATE}}-END"));
        assertEquals("TXN-1791374400000", DateExpressionResolver.resolve("TXN-{{$EPOCH_MILLIS}}"));
    }

    @Test
    void testListAndMapResolution() {
        List<Object> list = List.of("{{$CURRENT_DATE}}", 123, "{{$CURRENT_DATE + 1d}}");
        List<?> resolvedList = (List<?>) DateExpressionResolver.resolve(list);
        assertEquals("2026-10-07", resolvedList.get(0));
        assertEquals(123, resolvedList.get(1));
        assertEquals("2026-10-08", resolvedList.get(2));

        Map<String, Object> map = Map.of(
                "today", "{{$CURRENT_DATE}}",
                "nextWeek", "{{$CURRENT_DATE + 7d}}"
        );
        Map<?, ?> resolvedMap = (Map<?, ?>) DateExpressionResolver.resolve(map);
        assertEquals("2026-10-07", resolvedMap.get("today"));
        assertEquals("2026-10-14", resolvedMap.get("nextWeek"));
    }

    @Test
    void testNonStringPassThrough() {
        assertNull(DateExpressionResolver.resolve(null));
        assertEquals(42, DateExpressionResolver.resolve(42));
        assertEquals(true, DateExpressionResolver.resolve(true));
        assertEquals("regular-string", DateExpressionResolver.resolve("regular-string"));
    }

    @Test
    void testInvalidTokenThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                DateExpressionResolver.resolve("{{$INVALID_TOKEN}}")
        );
    }
}

