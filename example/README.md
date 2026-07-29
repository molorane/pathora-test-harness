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
- [Testing Approaches](#testing-approaches)
  - [Approach A: Individual File Testing](#approach-a-individual-file-testing)
  - [Approach B: Dynamic Directory Batch Execution](#approach-b-dynamic-directory-batch-execution)
- [Assertion Operators Reference](#assertion-operators-reference)

---

## Overview

Pathora Test Harness allows developers and QA engineers to define service test scenarios in declarative JSON/XML files. Tests can:
1. Load base JSON or XML request templates (`DefaultJSONRequestPath` or `DefaultXMLRequestPath`).
2. Override specific payload fields using JSONPath syntax (`TestCaseParameterValues`).
3. Dispatch mutated requests to Spring domain services via registered **EntryPoint Executors**.
4. Validate service responses using rich assertion rules (`ResponseAssertions`).

---

## Role of EntryPoint Executors (`EntryPointExecutor` SPI)

An `EntryPointExecutor` is a Service Provider Interface (SPI) contract provided by Pathora Test Harness (`io.github.molorane.pathora.testharness.spi.EntryPointExecutor`).

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
│   EntryPointDispatcher  │ (Matches "EntryPointName" from JSON test file)
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

---

## XML Request Template Support

Pathora Test Harness natively supports **XML request templates** (`.xml`) alongside JSON templates:
- **`DefaultXMLRequestPath`**: Specify your base XML template file in the test suite JSON file (`"DefaultXMLRequestPath": "../requests/user-create-request.xml"`).
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
  "DefaultXMLRequestPath": "../requests/user-create-request.xml",
  "Tests": [
    {
      "TestName": "Valid User Registration Test from XML Template",
      "TestDescription": "Validates user account creation using an XML base request template.",
      "EntryPointName": "user-registration-service",
      "TestCaseParameterValues": [
        { "JsonPath": "$.username", "Value": "alex_murphy" },
        { "JsonPath": "$.role", "Value": "ADMIN" }
      ],
      "ResponseAssertions": [
        { "JsonPath": "$.userId", "Operator": "STARTS_WITH", "Value": "USR-" },
        { "JsonPath": "$.username", "Value": "alex_murphy" },
        { "JsonPath": "$.role", "Value": "ADMIN" }
      ]
    }
  ]
}
```

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
[INFO] Tests run: 9, Failures: 0, Errors: 0

[INFO] Running com.example.demo.AllSuiteTest
[INFO] Tests run: 16, Failures: 0, Errors: 0

[INFO] Results:
[INFO] Tests run: 25, Failures: 0, Errors: 0
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
| **Date & Time** | `DATE_BEFORE` / `DATE_AFTER` | Date comparison (yyyy-MM-dd) | `"2030-01-01"` |
| | `DATETIME_BEFORE` / `DATETIME_AFTER` | DateTime comparison (ISO-8601) | `"2025-01-01T00:00:00"` |
| | `DATE_BEFORE_NOW` / `DATE_AFTER_NOW` | Compare against current system time | `null` |
| | `DATE_WITHIN_NEXT` / `DATE_WITHIN_LAST` | Time window check | `{"amount": 30, "unit": "DAYS"}` |
| **Duration** | `DURATION_EQUALS` | Exact duration between two date paths | `{"startPath": "$.start", "endPath": "$.end", "unit": "DAYS", "expected": 365}` |
| | `DURATION_GREATER_THAN` | Duration > threshold | `{"startPath": "$.start", "endPath": "$.end", "unit": "MONTHS", "value": 11}` |
| | `DURATION_LESS_THAN` | Duration < threshold | `{"startPath": "$.start", "endPath": "$.end", "unit": "YEARS", "value": 2}` |
| | `DATE_AFTER_DURATION` | End date is after start date + duration | `{"basePath": "$.start", "comparePath": "$.end", "amount": 30, "unit": "DAYS"}` |
| | `DATE_BEFORE_DURATION` | End date is before start date + duration | `{"basePath": "$.start", "comparePath": "$.end", "amount": 400, "unit": "DAYS"}` |
| **Structural** | `PATH_EXISTS` | JSONPath exists in response | `null` |
| | `PATH_NOT_EXISTS` | JSONPath absent or empty in response | `null` |
| | `ARRAY_SIZE_EQUALS` | Array length matches exact size | `2` |
| **Array** | `ARRAY_CONTAINS` | Array contains specific item | `"AUDIT_REPORT"` |
| | `ARRAY_CONTAINS_ONLY_VALUES` | Array contains exact set of values | `["REF-101", "REF-102"]` |
| | `ARRAY_CONTAINS_ONLY_ONE_VALUE` | Array contains exactly one element equal to value | `"PRIMARY_AUDITOR"` |
| | `ARRAY_CONTAINS_OBJECT_WITH_FIELDS` | Array contains object with matching fields | `{"clauseId": "CLS-01", "status": "APPROVED"}` |
| | `ALL_MATCH` | All array elements match condition | `{"greaterThan": 0}` |
| | `CONTAINS_ALL` / `CONTAINS_ANY` | Array contains all or any of expected values | `["TAG1", "TAG2"]` |
| | `DOES_NOT_CONTAIN_ANY` / `DOES_NOT_CONTAIN_ALL` | Exclusion checks | `["FRAUD", "BANKRUPTCY"]` |
| | `ARRAY_IS_EMPTY` | Array is empty | `null` |
| | `UNIQUE_ELEMENTS` | Array has no duplicate elements | `null` |
| | `VALUE_IN` / `VALUE_NOT_IN` | Scalar value is / isn't in allowed list | `["OPTION_A", "OPTION_B"]` |
| **Object** | `OBJECT_CONTAINS_FIELDS` | Actual object contains expected fields | `{"code": "UW-01", "region": "NORTH"}` |
| | `OBJECT_CONTAINS_FIELDS_IGNORE_NULLS` | Same as above, ignoring null expected fields | `{"code": "UW-01"}` |
| | `HAS_KEYS` | Object contains keys (values ignored) | `["key1", "key2"]` |
| | `FIELD_EQUALS_OTHER_FIELD` | Compare two fields in response | `{"leftPath": "$.fieldA", "rightPath": "$.fieldB"}` |
| **Logical** | `AND` / `OR` / `NOT` | Composite logical assertions | `{"Assertions": [...]}` |
