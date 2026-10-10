package io.github.molorane.pathora.testharness.engine.expression;

import io.github.molorane.pathora.testharness.util.PathoraClock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionResolverTest {

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
        assertEquals("2026-10-07", ExpressionResolver.resolve("{{$CURRENT_DATE}}"));
        assertEquals("2026-10-07", ExpressionResolver.resolve("{{$TODAY}}"));
        assertEquals("2026-10-07", ExpressionResolver.resolve("$CURRENT_DATE"));
        assertEquals("2026-10-07", ExpressionResolver.resolve("$TODAY"));
    }

    @Test
    void testCurrentDateTimeAndNow() {
        assertEquals("2026-10-07T12:00:00Z", ExpressionResolver.resolve("{{$CURRENT_DATETIME}}"));
        assertEquals("2026-10-07T12:00:00Z", ExpressionResolver.resolve("{{$NOW}}"));
        assertEquals("2026-10-07T12:00:00Z", ExpressionResolver.resolve("$CURRENT_DATETIME"));
        assertEquals("2026-10-07T12:00:00Z", ExpressionResolver.resolve("$NOW"));
    }

    @Test
    void testEpochMillisAndSeconds() {
        assertEquals(1791374400000L, ExpressionResolver.resolve("{{$EPOCH_MILLIS}}"));
        assertEquals(1791374400000L, ExpressionResolver.resolve("$EPOCH_MILLIS"));
        assertEquals(1791374400L, ExpressionResolver.resolve("{{$EPOCH_SECONDS}}"));
        assertEquals(1791374400L, ExpressionResolver.resolve("$EPOCH_SECONDS"));
    }

    @Test
    void testDateOffsets() {
        assertEquals("2026-11-06", ExpressionResolver.resolve("{{$CURRENT_DATE + 30d}}"));
        assertEquals("2026-09-27", ExpressionResolver.resolve("{{$CURRENT_DATE - 10d}}"));
        assertEquals("2026-10-21", ExpressionResolver.resolve("{{$CURRENT_DATE + 2w}}"));
        assertEquals("2027-01-07", ExpressionResolver.resolve("{{$CURRENT_DATE + 3m}}"));
        assertEquals("2001-10-07", ExpressionResolver.resolve("{{$CURRENT_DATE - 25y}}"));
        assertEquals("2027-10-07", ExpressionResolver.resolve("{{$TODAY + 1y}}"));
    }

    @Test
    void testDateTimeOffsets() {
        assertEquals("2026-10-07T14:00:00Z", ExpressionResolver.resolve("{{$CURRENT_DATETIME + 2h}}"));
        assertEquals("2026-10-07T11:30:00Z", ExpressionResolver.resolve("{{$NOW - 30min}}"));
        assertEquals("2026-10-07T12:00:45Z", ExpressionResolver.resolve("{{$NOW + 45s}}"));
    }

    @Test
    void testCustomFormatSpecifier() {
        assertEquals("07/10/2026", ExpressionResolver.resolve("{{$CURRENT_DATE:dd/MM/yyyy}}"));
        assertEquals("12-10-2026", ExpressionResolver.resolve("{{$CURRENT_DATE + 5d:dd-MM-yyyy}}"));
        assertEquals("2026/10/07 12:00", ExpressionResolver.resolve("{{$NOW:yyyy/MM/dd HH:mm}}"));
    }

    @Test
    void testEmbeddedExpressions() {
        assertEquals("REF-2026-10-07-END", ExpressionResolver.resolve("REF-{{$CURRENT_DATE}}-END"));
        assertEquals("TXN-1791374400000", ExpressionResolver.resolve("TXN-{{$EPOCH_MILLIS}}"));
    }

    @Test
    void testListAndMapResolution() {
        List<Object> list = List.of("{{$CURRENT_DATE}}", 123, "{{$CURRENT_DATE + 1d}}");
        List<?> resolvedList = (List<?>) ExpressionResolver.resolve(list);
        assertEquals("2026-10-07", resolvedList.get(0));
        assertEquals(123, resolvedList.get(1));
        assertEquals("2026-10-08", resolvedList.get(2));

        Map<String, Object> map = Map.of(
            "today", "{{$CURRENT_DATE}}",
            "nextWeek", "{{$CURRENT_DATE + 7d}}"
        );
        Map<?, ?> resolvedMap = (Map<?, ?>) ExpressionResolver.resolve(map);
        assertEquals("2026-10-07", resolvedMap.get("today"));
        assertEquals("2026-10-14", resolvedMap.get("nextWeek"));
    }

    @Test
    void testNonStringPassThrough() {
        assertNull(ExpressionResolver.resolve(null));
        assertEquals(42, ExpressionResolver.resolve(42));
        assertEquals(true, ExpressionResolver.resolve(true));
        assertEquals("regular-string", ExpressionResolver.resolve("regular-string"));
    }

    @Test
    void testUuidExpression() {
        Object uuidObj = ExpressionResolver.resolve("{{$UUID}}");
        assertNotNull(uuidObj);
        assertTrue(uuidObj.toString().matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"));

        Object standaloneUuid = ExpressionResolver.resolve("$RANDOM_UUID");
        assertNotNull(standaloneUuid);
        assertTrue(standaloneUuid.toString().matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"));
    }

    @Test
    void testRandomIntExpression() {
        Object randomIntObj = ExpressionResolver.resolve("{{$RANDOM_INT:1000:9999}}");
        assertNotNull(randomIntObj);
        assertTrue(randomIntObj instanceof Integer || randomIntObj instanceof Long);
        int val = ((Number) randomIntObj).intValue();
        assertTrue(val >= 1000 && val <= 9999);

        String embeddedStr = ExpressionResolver.resolveToString("ID-{{$RANDOM_INT:10:20}}");
        assertNotNull(embeddedStr);
        assertTrue(embeddedStr.startsWith("ID-"));
        int embeddedVal = Integer.parseInt(embeddedStr.substring(3));
        assertTrue(embeddedVal >= 10 && embeddedVal <= 20);
    }

    @Test
    void testRandomAlphanumericExpression() {
        String result = ExpressionResolver.resolveToString("REF-{{$RANDOM_ALPHANUMERIC:8}}");
        assertNotNull(result);
        assertTrue(result.startsWith("REF-"));
        assertEquals(12, result.length()); // "REF-" + 8 chars
    }

    @Test
    void testRandomEmailExpression() {
        String email = ExpressionResolver.resolveToString("{{$RANDOM_EMAIL}}");
        assertNotNull(email);
        assertTrue(email.startsWith("user_"));
        assertTrue(email.endsWith("@test.com"));
    }

    @Test
    void testRandomBooleanExpression() {
        Object standaloneBool = ExpressionResolver.resolve("{{$RANDOM_BOOLEAN}}");
        assertNotNull(standaloneBool);
        assertTrue(standaloneBool instanceof Boolean);

        String embeddedBool = ExpressionResolver.resolveToString("flag: {{$RANDOM_BOOLEAN}}");
        assertNotNull(embeddedBool);
        assertTrue(embeddedBool.equals("flag: true") || embeddedBool.equals("flag: false"));
    }

    @Test
    void testInvalidTokenThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            ExpressionResolver.resolve("{{$INVALID_TOKEN}}")
        );
    }

    @Test
    void testSystemPropertyTimezoneResolvesCorrectDate() {
        String originalProp = System.getProperty("pathora.timezone");
        try {
            System.setProperty("pathora.timezone", "Asia/Tokyo"); // UTC+9
            PathoraClock.reset();

            // When UTC is 2026-10-07 23:30, in Tokyo it is 2026-10-08 08:30
            PathoraClock.freeze(Instant.parse("2026-10-07T23:30:00Z"), PathoraClock.getZoneId());

            assertEquals("2026-10-08", ExpressionResolver.resolve("{{$CURRENT_DATE}}"));
            assertEquals("2026-10-09", ExpressionResolver.resolve("{{$CURRENT_DATE + 1d}}"));
        } finally {
            if (originalProp != null) {
                System.setProperty("pathora.timezone", originalProp);
            } else {
                System.clearProperty("pathora.timezone");
            }
            PathoraClock.reset();
        }
    }
}
