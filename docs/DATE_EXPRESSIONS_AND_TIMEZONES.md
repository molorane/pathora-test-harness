# Pathora Test Harness: Date Expressions, Timezones & Temporal Testing Guide

This guide provides a comprehensive reference on dynamic date/datetime expression language, timezone management, deterministic time-travel testing, and clock configuration in **Pathora Test Harness**.

---

## Table of Contents
1. [Overview & Motivation](#1-overview--motivation)
2. [Expression Language Reference](#2-expression-language-reference)
   - [Supported Base Tokens](#supported-base-tokens)
   - [Relative Offset Syntax](#relative-offset-syntax)
   - [Custom Date & DateTime Formatting](#custom-date--datetime-formatting)
   - [Combined Offset & Format Examples](#combined-offset--format-examples)
3. [Using Expressions in Request Mutations](#3-using-expressions-in-request-mutations)
4. [Using Expressions in Response Assertions](#4-using-expressions-in-response-assertions)
   - [Date Comparisons](#date-comparisons)
   - [Bounded Date Ranges (`DATE_BETWEEN`)](#bounded-date-ranges-date_between)
   - [DateTime Assertions with Tolerances](#datetime-assertions-with-tolerances)
   - [Assertion Best Practices (Date vs DateTime)](#assertion-best-practices-date-vs-datetime)
5. [Timezone Architecture & Precedence Hierarchy](#5-timezone-architecture--precedence-hierarchy)
   - [7 Ways to Configure Timezones & Clocks](#7-ways-to-configure-timezones--clocks)
   - [How Timezone Override Works Internally](#how-timezone-override-works-internally)
6. [Deterministic Time-Travel Testing with `PathoraClock`](#6-deterministic-time-travel-testing-with-pathoraclock)
   - [Global Freezing (`freeze`)](#global-freezing-freeze)
   - [Thread-Isolated Freezing (`freezeThread`)](#thread-isolated-freezing-freezethread)
   - [Lifecycle Best Practices](#lifecycle-best-practices)
7. [End-to-End JSON Test Suite Examples](#7-end-to-end-json-test-suite-examples)

---

## 1. Overview & Motivation

Enterprise systems frequently rely on temporal business rules:
- Policies that become active today or expire after 30 days.
- Underwriting rules that require applicants to be older than 18 or 25 years.
- Token and session expirations that occur in the next 15 minutes.
- Cross-region applications operating across multiple timezones (e.g. `Africa/Johannesburg`, `America/New_York`, `Asia/Tokyo`).

Hardcoded static timestamps in test payloads quickly become stale and cause test suites to fail over time. Pathora Test Harness solves this through:
- **Declarative Dynamic Expressions**: Evaluated at test runtime (`{{$CURRENT_DATE + 30d}}`, `{{$CURRENT_DATE - 25y}}`).
- **Thread-Safe Timezone Isolation**: Set timezones per test suite or per test case without multi-threading test pollution.
- **Deterministic Time-Travel**: Freeze the library clock to any date or epoch instant for reproducible assertions.

---

## 2. Expression Language Reference

Dynamic expressions are wrapped in double curly braces `{{...}}` or written as standalone tokens (e.g. `{{$CURRENT_DATE}}` or `$CURRENT_DATE`).

### Supported Base Tokens

| Token | Alias | Output Type | Default Format / Example |
| :--- | :--- | :--- | :--- |
| `{{$CURRENT_DATE}}` | `{{$TODAY}}` | String (ISO Date) | `yyyy-MM-dd` (e.g., `2026-10-07`) |
| `{{$CURRENT_DATETIME}}` | `{{$NOW}}` | String (ISO DateTime) | `yyyy-MM-dd'T'HH:mm:ss` (e.g., `2026-10-07T14:30:00`) |
| `{{$CURRENT_TIME}}` | | String (ISO Time) | `HH:mm:ss` (e.g., `14:30:00`) |
| `{{$EPOCH_MILLIS}}` | `{{$TIMESTAMP}}` | Long (Integer) | Milliseconds since epoch (e.g., `1791374400000`) |
| `{{$EPOCH_SECONDS}}` | | Long (Integer) | Seconds since epoch (e.g., `1791374400`) |

---

### Relative Offset Syntax

Offsets can be added (`+`) or subtracted (`-`) from base tokens.

#### Supported Offset Units:

| Unit Token | Temporal Unit | Example Expression | Description |
| :--- | :--- | :--- | :--- |
| `d` | Days | `{{$CURRENT_DATE + 30d}}` | 30 days in the future |
| `w` | Weeks | `{{$CURRENT_DATE + 2w}}` | 2 weeks in the future |
| `m` | Months | `{{$CURRENT_DATE - 6m}}` | 6 months in the past |
| `y` | Years | `{{$CURRENT_DATE - 25y}}` | 25 years in the past (e.g. Date of Birth) |
| `h` | Hours | `{{$CURRENT_DATETIME + 2h}}` | 2 hours from current time |
| `min` | Minutes | `{{$CURRENT_DATETIME + 15min}}` | 15 minutes from current time |
| `s` | Seconds | `{{$CURRENT_DATETIME - 30s}}` | 30 seconds before current time |
| `ms` | Milliseconds | `{{$EPOCH_MILLIS + 5000ms}}` | 5,000 milliseconds added to epoch |

---

### Custom Date & DateTime Formatting

Append a colon `:` followed by a `java.time.format.DateTimeFormatter` pattern:

| Pattern Syntax | Output Example |
| :--- | :--- |
| `{{$CURRENT_DATE:yyyy/MM/dd}}` | `2026/10/07` |
| `{{$CURRENT_DATE:dd-MM-yyyy}}` | `07-10-2026` |
| `{{$CURRENT_DATE:MMMM d, yyyy}}` | `October 7, 2026` |
| `{{$CURRENT_DATETIME:yyyy-MM-dd HH:mm:ss}}` | `2026-10-07 14:30:00` |
| `{{$CURRENT_DATETIME:yyyyMMdd_HHmmss}}` | `20261007_143000` |

---

### Combined Offset & Format Examples

Offsets and formatting patterns can be seamlessly combined:

```text
{{$CURRENT_DATE + 30d:yyyy/MM/dd}}          -> 2026/11/06
{{$CURRENT_DATE - 1y:dd-MM-yyyy}}           -> 07-10-2025
{{$CURRENT_DATETIME + 2h:yyyy-MM-dd HH:mm}} -> 2026-10-07 16:30
```

---

## 3. Using Expressions in Request Mutations

The `JsonMutationEngine` automatically resolves date and datetime expressions before mutating request payloads.

```json
{
  "requestPath": "../requests/policy-request.json",
  "tests": [
    {
      "name": "Dynamic Effective Date Mutation",
      "operation": "policy-evaluation-service",
      "mutations": [
        {
          "path": "$.policyHeader.effectiveDate",
          "value": "{{$CURRENT_DATE + 15d}}T00:00:00"
        },
        {
          "path": "$.insuredParty.dateOfBirth",
          "value": "{{$CURRENT_DATE - 30y}}"
        },
        {
          "path": "$.policyHeader.submissionTimestamp",
          "value": "{{$EPOCH_MILLIS}}"
        }
      ]
    }
  ]
}
```

---

## 4. Using Expressions in Response Assertions

The `AssertionEngine` evaluates dynamic expressions inside assertion `value` definitions before passing them to specific evaluators.

### Date Comparisons

```json
{
  "assertions": [
    {
      "path": "$.policyHeaderEffectiveDate",
      "operator": "DATE_EQUALS",
      "value": "{{$CURRENT_DATE + 15d}}",
      "description": "Effective date must equal current date + 15 days"
    },
    {
      "path": "$.policyHeaderEffectiveDate",
      "operator": "DATE_AFTER",
      "value": "{{$CURRENT_DATE}}",
      "description": "Effective date must be after today"
    },
    {
      "path": "$.insuredParty.dateOfBirth",
      "operator": "DATE_BEFORE",
      "value": "{{$CURRENT_DATE - 18y}}",
      "description": "Applicant must be at least 18 years old"
    },
    {
      "path": "$.currentDate",
      "operator": "IS_TODAY",
      "description": "Current date must match system today"
    }
  ]
}
```

---

### Bounded Date Ranges (`DATE_BETWEEN`)

Dynamic expressions can be placed inside composite map arguments:

```json
{
  "path": "$.policyHeaderEffectiveDate",
  "operator": "DATE_BETWEEN",
  "value": {
    "min": "{{$CURRENT_DATE}}",
    "max": "{{$CURRENT_DATE + 30d}}"
  },
  "description": "Effective date falls within dynamic 30-day window"
}
```

---

### DateTime Assertions with Tolerances

Because live services execute with clock skew, strict equality on seconds/milliseconds is fragile. Use **`DATETIME_EQUALS_WITH_TOLERANCE`** with dynamic expressions:

```json
{
  "path": "$.tokenExpiresAt",
  "operator": "DATETIME_EQUALS_WITH_TOLERANCE",
  "value": {
    "expected": "{{$CURRENT_DATETIME + 1h}}",
    "tolerance": 5,
    "unit": "MINUTES"
  },
  "description": "Token expires approximately 1 hour from now (within 5 minutes)"
}
```

---

### Assertion Best Practices (Date vs DateTime)

| Scenario | Recommended Approach | Reason |
| :--- | :--- | :--- |
| **Business Calendar Date** (e.g., Effective Date, Due Date, DoB) | `DATE_EQUALS`, `DATE_BEFORE`, `DATE_AFTER`, `DATE_BETWEEN` with `{{$CURRENT_DATE}}` | Exact day matching is deterministic and not subject to millisecond skew. |
| **Past Event / Audit Log** (e.g., Created At, Evaluated At) | `IS_PAST_DATETIME` or `DATETIME_WITHIN_LAST` (`{"amount": 10, "unit": "MINUTES"}`) | Avoids millisecond mismatch between client and server. |
| **Future Event / Expiry** (e.g., Token Expiration, Timeout) | `DATETIME_EQUALS_WITH_TOLERANCE` or `DATETIME_WITHIN_NEXT` | Accommodates execution delays and network latency. |
| **Deterministic Time Testing** | Freeze `PathoraClock` at test startup | Freezes the clock so date and datetime checks are 100% predictable. |

---

## 5. Timezone Architecture & Precedence Hierarchy

Pathora Test Harness provides a 7-tier hierarchy to determine the active timezone:

```
┌────────────────────────────────────────────────────────┐
│ 1. Test Case Level JSON ("timezone": "Asia/Tokyo")     │  <-- Highest Priority
├────────────────────────────────────────────────────────┤
│ 2. Test Suite Level JSON ("timezone": "Europe/London") │  <-- Suite default
├────────────────────────────────────────────────────────┤
│ 3. Programmatic Thread Clock (PathoraClock)            │  <-- ThreadLocal isolation
├────────────────────────────────────────────────────────┤
│ 4. Programmatic Global Clock (PathoraClock)            │  <-- Suite setup
├────────────────────────────────────────────────────────┤
│ 5. Spring Environment / application.yml                │  <-- pathora.timezone property
├────────────────────────────────────────────────────────┤
│ 6. JVM System Property (-Dpathora.timezone=...)        │  <-- CI/CD parameter
├────────────────────────────────────────────────────────┤
│ 7. System Default Timezone (Clock.systemDefaultZone()) │  <-- Fallback
└────────────────────────────────────────────────────────┘
```

---

### 7 Ways to Configure Timezones & Clocks

#### Way 1: Test Suite Level JSON
Configured at the root of the test suite file. All test cases in the file inherit this zone:
```json
{
  "requestPath": "../requests/policy-request.json",
  "timezone": "Africa/Johannesburg",
  "tests": [ ... ]
}
```

#### Way 2: Test Case Level JSON Override
Overrides the suite-level timezone for an individual test case:
```json
{
  "name": "Tokyo Regional Policy Evaluation",
  "timezone": "Asia/Tokyo",
  "mutations": [
    { "path": "$.policyHeader.effectiveDate", "value": "{{$CURRENT_DATE}}" }
  ],
  "assertions": [
    { "path": "$.policyHeaderEffectiveDate", "operator": "DATE_EQUALS", "value": "{{$CURRENT_DATE}}" }
  ]
}
```

#### Way 3: Programmatic Global Freezing with ZoneId (Time-Travel Testing)
```java
LocalDate testDate = LocalDate.of(2028, 2, 29);
PathoraClock.freeze(testDate, ZoneId.of("Africa/Johannesburg"));

// Dynamic tokens resolve to 2028-02-29
DateExpressionResolver.resolveToString("{{$CURRENT_DATE}}"); // "2028-02-29"
DateExpressionResolver.resolveToString("{{$CURRENT_DATE + 30d}}"); // "2028-03-30"
```

#### Way 4: Programmatic Thread-Scoped Freezing (Parallel Isolation)
```java
Instant threadInstant = Instant.parse("2030-07-04T12:00:00Z");
PathoraClock.freezeThread(threadInstant, ZoneId.of("America/New_York"));
try {
    // Current thread sees America/New_York (2030-07-04)
    // Other concurrent test threads see global clock
} finally {
    PathoraClock.clearThreadClock();
}
```

#### Way 5: Programmatic Custom Clock Injection
```java
Clock customClock = Clock.system(ZoneId.of("Europe/London"));
PathoraClock.setClock(customClock);

// Or by timezone ID / ZoneId directly:
PathoraClock.setTimezone("Europe/London");
```

#### Way 6: JVM System Property Configuration
Pass on the command line:
```bash
mvn test -Dpathora.timezone=Australia/Sydney
```
Or in Java:
```java
System.setProperty("pathora.timezone", "Australia/Sydney");
PathoraClock.reset();
```

#### Way 7: Spring Environment / `application.yml` Configuration
Configure the global default timezone declaratively in Spring Boot `application.yml` or `application.properties`:

```yaml
# application.yml
pathora:
  timezone: "Africa/Johannesburg"
```

In your Spring configuration class:
```java
@Configuration
public class TestHarnessConfig {

    @Value("${pathora.timezone:}")
    private String configuredTimezone;

    @PostConstruct
    public void initTimezone() {
        if (configuredTimezone != null && !configuredTimezone.isBlank()) {
            PathoraClock.setTimezone(configuredTimezone);
        }
    }
}
```

---

### How Timezone Override Works Internally

1. When a test case executes, `AssertionEngine` (and `RuleTestCaseExecutor`) inspects `testCase.timezone()` and `suite.timezone()`.
2. It calls `PathoraClock.setThreadClock(Clock.system(ZoneId.of(zoneId)))`.
3. All evaluator calls (`PathoraClock.today()`, `PathoraClock.now()`) and expression resolvers (`DateExpressionResolver.resolve(...)`) query the `ThreadLocal` clock first.
4. In a `finally` block, `PathoraClock.clearThreadClock()` is invoked, ensuring zero state leakage across test cases or worker threads.

---

## 6. Deterministic Time-Travel Testing with `PathoraClock`

`PathoraClock` provides complete control over time during automated tests.

### Global Freezing (`freeze`)

```java
// Freeze to specific Instant in UTC
PathoraClock.freeze(Instant.parse("2026-01-01T00:00:00Z"));

// Freeze to LocalDate with specific ZoneId
PathoraClock.freeze(LocalDate.of(2028, 2, 29), ZoneId.of("Africa/Johannesburg"));
```

### Thread-Isolated Freezing (`freezeThread`)

```java
@Test
void testLeapYearEdgeCase() {
    PathoraClock.freezeThread(LocalDate.of(2028, 2, 29), ZoneOffset.UTC);
    try {
        // Assertions execute with clock frozen on Leap Day
    } finally {
        PathoraClock.clearThreadClock();
    }
}
```

### Lifecycle Best Practices

In JUnit 5 tests, always reset the clock in `@BeforeEach` and `@AfterEach` lifecycle methods:

```java
@BeforeEach
@AfterEach
void resetClock() {
    PathoraClock.reset();
    System.clearProperty("pathora.timezone");
}
```

---

## 7. End-to-End JSON Test Suite Examples

```json
{
  "requestPath": "../requests/complex-policy-request.json",
  "timezone": "Africa/Johannesburg",
  "tests": [
    {
      "name": "Suite Timezone Dynamic Evaluation (+15d)",
      "description": "Mutates effectiveDate with +15d dynamic token and asserts equality and range bounds in Africa/Johannesburg.",
      "operation": "policy-evaluation-service",
      "mutations": [
        {
          "path": "$.policyHeader.effectiveDate",
          "value": "{{$CURRENT_DATE + 15d}}T00:00:00"
        }
      ],
      "assertions": [
        {
          "path": "$.policyHeaderEffectiveDate",
          "operator": "DATE_EQUALS",
          "value": "{{$CURRENT_DATE + 15d}}",
          "description": "Effective date equals current date + 15 days"
        },
        {
          "path": "$.policyHeaderEffectiveDate",
          "operator": "DATE_AFTER",
          "value": "{{$CURRENT_DATE}}",
          "description": "Effective date is after today"
        },
        {
          "path": "$.policyHeaderEffectiveDate",
          "operator": "DATE_BETWEEN",
          "value": {
            "min": "{{$CURRENT_DATE}}",
            "max": "{{$CURRENT_DATE + 30d}}"
          },
          "description": "Effective date is within the next 30 days"
        },
        {
          "path": "$.currentDate",
          "operator": "IS_TODAY",
          "description": "Current date is today"
        }
      ]
    },
    {
      "name": "Test-Case Specific Timezone Override (Asia/Tokyo)",
      "description": "Overrides timezone to Asia/Tokyo for this specific test case.",
      "operation": "policy-evaluation-service",
      "timezone": "Asia/Tokyo",
      "mutations": [
        {
          "path": "$.policyHeader.effectiveDate",
          "value": "{{$CURRENT_DATE + 5d}}T00:00:00"
        }
      ],
      "assertions": [
        {
          "path": "$.policyHeaderEffectiveDate",
          "operator": "DATE_EQUALS",
          "value": "{{$CURRENT_DATE + 5d}}",
          "description": "Effective date equals +5 days evaluated in Tokyo timezone"
        },
        {
          "path": "$.policyHeaderEffectiveDate",
          "operator": "DATE_AFTER",
          "value": "{{$CURRENT_DATE}}",
          "description": "Effective date is after today in Tokyo"
        }
      ]
    }
  ]
}
```

