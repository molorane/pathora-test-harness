# Pathora Test Harness — Spring Boot Demo Application

This project provides a comprehensive, production-ready demonstration of **Pathora Test Harness**, a powerful data-driven testing framework designed to evaluate enterprise application services using JSON and XML test suite definitions, JSONPath parameter mutations, service entry point executors, and rich assertion engines.

---

## Table of Contents
- [Overview](#overview)
- [Role of EntryPoint Executors (`EntryPointExecutor` SPI)](#role-of-entrypoint-executors-entrypointexecutor-spi)
- [XML Request Template Support](#xml-request-template-support)
- [How Pathora Test Harness Works](#how-pathora-test-harness-works)
- [Directory Structure](#directory-structure)
- [Writing Test Suites & Request Templates](#writing-test-suites--request-templates)
- [Running the Demo Tests](#running-the-demo-tests)
- [Dynamic Date Expressions & Offsets](#dynamic-date-expressions--offsets)
- [Timezone & Clock Configuration (7 Ways)](#timezone--clock-configuration-7-ways)
- [Testing Approaches](#testing-approaches)
    - [Approach A: Individual File Testing](#approach-a-individual-file-testing)
    - [Approach B: Dynamic Directory Batch Execution](#approach-b-dynamic-directory-batch-execution)
    - [Approach C: Timezone & Deterministic Clock Demo (`TimezoneAndClockDemoTest`)](#approach-c-timezone--deterministic-clock-demo-timezoneandclockdemotest)
- [Assertion Operators Reference](#assertion-operators-reference)

---

## Overview

Pathora Test Harness allows developers and QA engineers to define service test scenarios in declarative JSON/XML files. Tests can:
1. Load base JSON or XML request templates (`requestPath` or `xmlRequestPath`).
2. Override specific payload fields using JSONPath syntax within `mutations`.
3. Dispatch mutated requests to Spring domain services via registered **EntryPoint Executors**.
4. Validate service responses using rich assertion rules in `assertions`.

Current suite shape:
- Root request file: `requestPath` or `xmlRequestPath`
- Test list: `tests`
- Each test: `name`, `description`, `operation`, `mutations`, `assertions`
- Each mutation/assertion: `path`, `operator`, `value`

---

## Role of EntryPoint Executors (`EntryPointExecutor` SPI)

An `EntryPointExecutor` is a Service Provider Interface (SPI) contract provided by Pathora Test Harness (`za.co.pathora.testharness.spi.EntryPointExecutor`).

It acts as the **bridge/adapter** between Pathora Test Harness and your application's domain services, REST clients, gRPC endpoints, or internal business components.

```text
┌─────────────────────────┐
│  Pathora Test Harness   │
│  (JSON / XML Test Suite │
│   & Mutation Engine)    │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│   EntryPointDispatcher  │ (Matches "operation" from JSON test file)
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│   EntryPointExecutor    │ <--- YOUR APPLICATION SPI ADAPTER
│  (Deserializes request  │
│   & invokes service)    │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│ Your Spring Boot        │
│ Domain Service / DTO    │
└─────────────────────────┘
```

### Implementing an `EntryPointExecutor`

Every executor defines the entry point name, the request class token `Class<REQ>` for type-safe JSON/XML deserialization, and the business execution logic:

```java
package com.example.demo.executor;

import com.example.demo.dto.LoanRequest;
import com.example.demo.dto.LoanResponse;
import za.co.pathora.testharness.spi.EntryPointExecutor;
import org.springframework.stereotype.Component;

@Component
public class LoanApplicationExecutor implements EntryPointExecutor<LoanRequest, LoanResponse> {

    @Override
    public String getEntryPointName() {
        return "loan-application-service";
    }

    @Override
    public Class<LoanRequest> getRequestType() {
        return LoanRequest.class;
    }

    @Override
    public LoanResponse execute(LoanRequest loanRequest) {
        // Direct strongly-typed request and response execution
        return new LoanResponse(
            "APP-1001",
            loanRequest.applicantId(),
            loanRequest.requestedAmount(),
            6.5,
            "APPROVED",
            Instant.now().toString()
        );
    }
}
```

---

## XML Request Template Support

Pathora Test Harness natively supports **XML request templates** (`.xml`) alongside JSON templates:
- **`xmlRequestPath`**: Specify your base XML template file in the test suite JSON file (`"xmlRequestPath": "../requests/user-create-request.xml"`).
- **Automatic Payload Format Detection**: `EntryPointDispatcher` automatically detects XML payloads and uses Jackson `XmlMapper` to deserialize XML into your Java DTOs.
- **JSONPath Parameter Mutations on XML**: `JsonMutationEngine` automatically converts XML templates to an in-memory representation so you can use standard JSONPath mutations (`$.username`, `$.role`) seamlessly on XML requests.

---

## Directory Structure

```text
example/
├── pom.xml                               # Spring Boot POM depending on pathora-test-harness
├── README.md                             # This usage guide
├── templates/
│   ├── requests/                         # Base JSON & XML Request Templates
│   │   ├── user-create-request.json
│   │   ├── user-create-request.xml       # Base XML Request Template
│   │   ├── order-checkout-request.json
│   │   ├── payment-process-request.json
│   │   ├── inventory-update-request.json
│   │   ├── loan-application-request.json
│   │   └── complex-policy-request.json
│   └── tests/                            # Declarative JSON Test Suite Files
│       ├── user-create-test.json
│       ├── user-create-xml-test.json     # Test Suite referencing XML request template
│       ├── order-checkout-test.json
│       ├── payment-process-test.json
│       ├── inventory-update-test.json
│       ├── loan-application-test.json
│       ├── policy-evaluation-test.json
│       └── policy-risk-assessment-test.json
└── src/
    ├── main/
    │   └── java/
    │       └── com/example/demo/
    │           ├── DemoApplication.java
    │           ├── config/
    │           │   └── TestHarnessConfig.java
    │           ├── dto/                  # DTO Records
    │           └── executor/             # EntryPointExecutor Implementations
    └── test/
        └── java/
            └── com/example/demo/
                ├── SingleTestSuiteDemoTest.java
                ├── AllSuiteTest.java
                └── adapter/
                    └── DynamicTestAdapter.java
```

---

## Writing Test Suites & Request Templates

### XML Request Template (`templates/requests/user-create-request.xml`)
```xml
<UserRequest>
    <username>john_doe</username>
    <email>john.doe@example.com</email>
    <role>USER</role>
    <status>ACTIVE</status>
</UserRequest>
```

### Test Suite referencing XML (`templates/tests/user-create-xml-test.json`)
```json
{
  "xmlRequestPath": "../requests/user-create-request.xml",
  "tests": [
    {
      "name": "Valid User Registration Test from XML Template",
      "description": "Validates user account creation using an XML base request template.",
      "operation": "user-registration-service",
      "mutations": [
        { "path": "$.username", "value": "alex_murphy" },
        { "path": "$.role", "value": "ADMIN" }
      ],
      "assertions": [
        { "path": "$.userId", "operator": "STARTS_WITH", "value": "USR-" },
        { "path": "$.username", "value": "alex_murphy" },
        { "path": "$.role", "value": "ADMIN" }
      ]
    }
  ]
}
```

---

## Dynamic Date Expressions & Offsets

Pathora Test Harness provides built-in expression resolution for dynamic date and datetime values across both **payload mutations** and **response assertions**.

### Expression Syntax

| Token | Description | Resolved Format (Default) |
| :--- | :--- | :--- |
| `{{$CURRENT_DATE}}` / `{{$TODAY}}` | Current date | `yyyy-MM-dd` (e.g. `2026-10-07`) |
| `{{$CURRENT_DATETIME}}` / `{{$NOW}}` | Current date and time | `yyyy-MM-ddTHH:mm:ss` |
| `{{$CURRENT_TIME}}` | Current time | `HH:mm:ss` |
| `{{$EPOCH_MILLIS}}` / `{{$TIMESTAMP}}` | Epoch millisecond timestamp | Integer `1791374400000` |
| `{{$EPOCH_SECONDS}}` | Epoch second timestamp | Integer `1791374400` |

### Relative Offsets & Custom Formatting

Append `+` or `-` offsets using `d` (days), `w` (weeks), `m` (months), `y` (years), `h` (hours), `min` (minutes), `s` (seconds):

- `{{$CURRENT_DATE + 30d}}` — 30 days in the future (`2026-11-06`)
- `{{$CURRENT_DATE - 25y}}` — 25 years in the past (`2001-10-07`)
- `{{$CURRENT_DATETIME + 2h}}` — 2 hours from now (`2026-10-07T14:30:00`)
- `{{$CURRENT_DATE:yyyy/MM/dd}}` — Custom date format (`2026/10/07`)
- `{{$CURRENT_DATE + 1m:dd-MM-yyyy}}` — Offset combined with custom formatting

### JSON Test Suite Example (`templates/tests/dynamic-date-timezone-test.json`)

```json
{
  "requestPath": "../requests/complex-policy-request.json",
  "timezone": "Africa/Johannesburg",
  "tests": [
    {
      "name": "Dynamic Date Token Mutation & Evaluation",
      "operation": "policy-evaluation-service",
      "mutations": [
        { "path": "$.policyHeader.effectiveDate", "value": "{{$CURRENT_DATE + 15d}}T00:00:00" }
      ],
      "assertions": [
        { "path": "$.policyHeaderEffectiveDate", "operator": "DATE_EQUALS", "value": "{{$CURRENT_DATE + 15d}}" },
        { "path": "$.policyHeaderEffectiveDate", "operator": "DATE_AFTER", "value": "{{$CURRENT_DATE}}" },
        { "path": "$.policyHeaderEffectiveDate", "operator": "DATE_BETWEEN", "value": { "min": "{{$CURRENT_DATE}}", "max": "{{$CURRENT_DATE + 30d}}" } },
        { "path": "$.currentDate", "operator": "IS_TODAY" }
      ]
    }
  ]
}
```

---

## Timezone & Clock Configuration (7 Ways)

Pathora Test Harness provides full flexibility to manage timezones and deterministic clocks across environments and test setups:

### Way 1: Test Suite JSON Level Timezone
Define `"timezone"` at the root of the test suite JSON file. All test cases in the suite inherit this timezone unless overridden.
```json
{
  "requestPath": "../requests/complex-policy-request.json",
  "timezone": "Africa/Johannesburg",
  "tests": [ ... ]
}
```

### Way 2: Test Case JSON Level Timezone Override
Override the suite timezone for a specific test case without affecting sibling test cases.
```json
{
  "name": "Tokyo Regional Policy Evaluation",
  "operation": "policy-evaluation-service",
  "timezone": "Asia/Tokyo",
  "mutations": [
    { "path": "$.policyHeader.effectiveDate", "value": "{{$CURRENT_DATE}}" }
  ],
  "assertions": [
    { "path": "$.policyHeaderEffectiveDate", "operator": "DATE_EQUALS", "value": "{{$CURRENT_DATE}}" }
  ]
}
```

### Way 3: Programmatic Global Clock Freezing with ZoneId (Time-Travel Testing)
Deterministically freeze system time globally across your entire test class or fixture:
```java
LocalDate leapDay = LocalDate.of(2028, 2, 29);
PathoraClock.freeze(leapDay, ZoneId.of("Africa/Johannesburg"));

// All expressions and date operators now evaluate against 2028-02-29
    DateExpressionResolver.resolveToString("{{$CURRENT_DATE}}"); // "2028-02-29"
DateExpressionResolver.resolveToString("{{$CURRENT_DATE + 30d}}"); // "2028-03-30"
```

### Way 4: Programmatic Thread-Scoped Clock Freezing (Parallel Isolation)
Isolate deterministic time overrides per test thread so parallel tests never collide:
```java
Instant threadInstant = Instant.parse("2030-07-04T16:00:00Z");
PathoraClock.freezeThread(threadInstant, ZoneId.of("America/New_York"));

    try {
    // Current thread sees America/New_York (2030-07-04)
    // Other concurrent threads still see the global clock
    } finally {
    PathoraClock.clearThreadClock();
}
```

### Way 5: Programmatic Custom Clock Injection
Inject any `java.time.Clock` instance (e.g., `Clock.offset`, `Clock.fixed`, `Clock.system`):
```java
Clock londonClock = Clock.system(ZoneId.of("Europe/London"));
PathoraClock.setClock(londonClock);
```

### Way 6: JVM System Property Configuration
Set `-Dpathora.timezone` on the JVM startup command line or via `System.setProperty`:
```bash
mvn test -Dpathora.timezone=Australia/Sydney
```
Or in code:
```java
System.setProperty("pathora.timezone", "Australia/Sydney");
PathoraClock.reset();
```

### Way 7: Spring Environment / `application.yml` Configuration
```yaml
pathora:
  timezone: "Africa/Johannesburg"
```
Then initialize the harness from your Spring config:
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

### Way 8: Default System Timezone
When no explicit timezone is configured anywhere else, the harness falls back to the JVM default zone.

---

## Testing Approaches

### Approach A: Individual File Testing (`SingleTestSuiteDemoTest`)
Runs individual test suite JSON/XML files directly using explicit Spring test methods.

### Approach B: Dynamic Directory Batch Execution (`AllSuiteTest`)
Automatically discovers and executes all test suites located under `templates/tests/` dynamically generating JUnit 5 dynamic tests.

### Approach C: Timezone & Deterministic Clock Demo (`TimezoneAndClockDemoTest`)
Demonstrates all 7 timezone configuration and clock freezing strategies with assertions against mutated JSON responses.

---

## Running the Demo Tests

Run Maven test from the `example/` directory:

```bash
cd example
sh ../mvnw test
```

Expected output:
```text
[INFO] Running com.example.demo.SingleTestSuiteDemoTest
[INFO] Tests run: 10, Failures: 0, Errors: 0

[INFO] Running com.example.demo.traditional.TimezoneAndClockDemoTest
[INFO] Tests run: 6, Failures: 0, Errors: 0

[INFO] Running com.example.demo.AllSuiteTest
[INFO] Tests run: 20, Failures: 0, Errors: 0

[INFO] Results:
[INFO] Tests run: 36, Failures: 0, Errors: 0
[INFO] BUILD SUCCESS
```

---

## Assertion Operators Reference

Pathora Test Harness provides comprehensive operators for validating JSON responses:

| Category | Operator | Description | Example Value Syntax |
| :--- | :--- | :--- | :--- |
| **Scalar** | *(omitted / `EQUALS`)* | Exact value equality | `"ACTIVE"` / `100` |
| | `NOT_EQUALS` | Value must not equal expected | `"REJECTED"` |
| | `GREATER_THAN` | Value > threshold | `10.0` |
| | `GREATER_THAN_OR_EQUALS` | Value >= threshold | `10` |
| | `LESS_THAN` | Value < threshold | `500.0` |
| | `LESS_THAN_OR_EQUALS` | Value <= threshold | `100.0` |
| | `BETWEEN` | Numeric value within range | `{"min": 10.0, "max": 100.0}` |
| **String** | `STARTS_WITH` | String starts with prefix | `"ORD-"` |
| | `ENDS_WITH` | String ends with suffix | `"_SOUTH"` |
| | `REGEX_MATCH` | Matches regular expression | `"^[A-Z0-9]+$"` |
| | | **Date** | `DATE_BEFORE` / `DATE_AFTER` | Date comparison (yyyy-MM-dd) | `"2030-01-01"` |
| | | | `DATE_EQUALS` / `DATE_BETWEEN` | Date equality & range | `{"min": "2025-01-01", "max": "2025-12-31"}` |
| | | | `DATE_YEAR_EQUALS` | Year component matches expected year | `2025` |
| | | | `DATE_MONTH_EQUALS` | Month component matches expected month number or name | `"JUNE"` / `6` |
| | | | `DATE_DAY_EQUALS` | Day-of-month component matches expected day (1-31) | `13` |
| | | | `DATE_DAY_OF_WEEK_EQUALS` | Day-of-week component matches expected day of week | `"FRIDAY"` / `5` |
| | | | `IS_TODAY` / `IS_PAST_DATE` / `IS_FUTURE_DATE` | Compare against current system date | `null` |
| | | `DATE_WITHIN_NEXT` / `DATE_WITHIN_LAST` | Date window check | `{"amount": 30, "unit": "DAYS"}` |
| | | **DateTime / Time** | `DATETIME_BEFORE` / `DATETIME_AFTER` / `DATETIME_EQUALS` | DateTime comparison (ISO-8601) | `"2025-01-01T00:00:00"` |
| | | | `DATETIME_EQUALS_WITH_TOLERANCE` / `DATETIME_BETWEEN` | DateTime tolerance & range | `{"expected": "2025-01-01T00:00:00", "tolerance": 5, "unit": "SECONDS"}` |
| | | | `TIME_EQUALS` | Time component matches expected time (HH:mm or HH:mm:ss) | `"23:59:59"` / `"23:59"` |
| | | | `TIME_BEFORE` / `TIME_AFTER` / `TIME_BETWEEN` | Time comparisons | `{"min": "08:00:00", "max": "17:00:00"}` |
| | | | `DATE_HOUR_EQUALS` / `DATE_MINUTE_EQUALS` / `DATE_SECOND_EQUALS` | Individual time component equality | `23` / `59` |
| | | | `IS_PAST_DATETIME` / `IS_FUTURE_DATETIME` | Compare against current system timestamp | `null` |
| | | | `DATETIME_WITHIN_NEXT` / `DATETIME_WITHIN_LAST` | DateTime window check | `{"amount": 24, "unit": "HOURS"}` |
| **Duration** | `DURATION_EQUALS` | Exact duration between two date paths | `{"startPath": "$.start", "endPath": "$.end", "unit": "DAYS", "expected": 365}` |
| | `DURATION_GREATER_THAN` | Duration > threshold | `{"startPath": "$.start", "endPath": "$.end", "unit": "MONTHS", "value": 11}` |
| | `DURATION_LESS_THAN` | Duration < threshold | `{"startPath": "$.start", "endPath": "$.end", "unit": "YEARS", "value": 2}` |
| | `DATE_AFTER_DURATION` | End date is after start date + duration | `{"basePath": "$.start", "comparePath": "$.end", "amount": 30, "unit": "DAYS"}` |
| | `DATE_BEFORE_DURATION` | End date is before start date + duration | `{"basePath": "$.start", "comparePath": "$.end", "amount": 400, "unit": "DAYS"}` |
| **Structural** | `PATH_EXISTS` | JSONPath exists in response | `null` |
| | `PATH_NOT_EXISTS` | JSONPath absent or empty in response | `null` |
| | | `LIST_SIZE_EQUALS` | Array length matches exact size | `2` |
| | **Array** | `LIST_CONTAINS` | Array contains specific item | `"AUDIT_REPORT"` |
| | | `LIST_CONTAINS_ONLY_VALUES` | Array contains exact set of values | `["REF-101", "REF-102"]` |
| | | `LIST_CONTAINS_ONLY_ONE_VALUE` | Array contains exactly one element equal to value | `"PRIMARY_AUDITOR"` |
| | | | `LIST_CONTAINS_OBJECT_WITH_FIELDS` | Array contains object with matching fields | `{"clauseId": "CLS-01", "status": "APPROVED"}` |
| | | | `LIST_CONTAINS_PARTIAL_OBJECT_WITH_FIELDS` | Array contains object with matching fields (deep/recursive partial matching) | `{"clauseId": "CLS-01", "details": [{"name": "A"}]}` |
| | | | `ALL_MATCH` | All array elements match condition | `{"greaterThan": 0}` |
| | | | `CONTAINS_ALL` / `CONTAINS_ANY` | Array contains all or any of expected values | `["TAG1", "TAG2"]` |
| | | | | `DOES_NOT_CONTAIN_ANY` / `DOES_NOT_CONTAIN_ALL` | Exclusion checks | `["FRAUD", "BANKRUPTCY"]` |
| | | | | `IS_EMPTY_LIST` | Array is empty | `null` |
| | | | `UNIQUE_ELEMENTS` | Array has no duplicate elements | `null` |
| | | `VALUE_IN` / `VALUE_NOT_IN` | Scalar value is / isn't in allowed list | `["OPTION_A", "OPTION_B"]` |
| | **Object** | `OBJECT_CONTAINS_FIELDS` | Actual object contains expected fields | `{"code": "UW-01", "region": "NORTH"}` |
| | | `OBJECT_CONTAINS_PARTIAL_FIELDS` | Actual object contains expected fields (deep/recursive partial matching) | `{"code": "UW-01", "items": [{"id": 1}]}` |
| | | `OBJECT_CONTAINS_FIELDS_IGNORE_NULLS` | Same as above, ignoring null expected fields | `{"code": "UW-01"}` |
| | `HAS_KEYS` | Object contains keys (values ignored) | `["key1", "key2"]` |
| | `FIELD_EQUALS_OTHER_FIELD` | Compare two fields in response | `{"leftPath": "$.fieldA", "rightPath": "$.fieldB"}` |
| **Logical** | `AND` / `OR` / `NOT` | Composite logical assertions | `{"Assertions": [...]}` |