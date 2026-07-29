# Pathora Test Harness — Spring Boot Demo Application

This project provides a comprehensive, production-ready demonstration of **Pathora Test Harness**, a powerful data-driven testing framework designed to evaluate enterprise application services using JSON test suite definitions, JSONPath parameter mutations, service entry point executors, and rich assertion engines.

---

## Table of Contents
- [Overview](#overview)
- [Role of EntryPoint Executors (`EntryPointExecutor` SPI)](#role-of-entrypoint-executors-entrypointexecutor-spi)
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

Pathora Test Harness allows developers and QA engineers to define service test scenarios in declarative JSON files. Tests can:
1. Load base JSON request templates.
2. Override specific JSON fields using JSONPath syntax (`TestCaseParameterValues`).
3. Dispatch mutated requests to Spring domain services via registered **EntryPoint Executors**.
4. Validate service responses using rich assertion rules (`ResponseAssertions`).

---

## Role of EntryPoint Executors (`EntryPointExecutor` SPI)

### What is an `EntryPointExecutor`?
An `EntryPointExecutor` is a Service Provider Interface (SPI) contract provided by Pathora Test Harness (`io.github.molorane.pathora.testharness.spi.EntryPointExecutor`). 

It acts as the **bridge/adapter** between Pathora Test Harness and your application's domain services, REST clients, gRPC endpoints, or internal business components.

```text
┌─────────────────────────┐
│  Pathora Test Harness   │
│  (JSON Test Suite &     │
│   Mutation Engine)      │
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

### Why are Executors Important?
1. **Decoupling**: Decouples JSON test definitions from the underlying Java implementation details. Test files reference a simple logical name (e.g. `"order-processing-service"`).
2. **Type Safety**: Executors declare the exact Java DTO class (`getRequestType()`). Pathora uses Jackson to deserialize the mutated JSON payload directly into your strongly-typed Java DTOs.
3. **Flexibility**: An Executor can call a Spring `@Service`, invoke a HTTP REST endpoint via `RestClient`/`WebClient`, or invoke a legacy component.

### The `EntryPointExecutor` Interface Methods

```java
package io.github.molorane.pathora.testharness.spi;

public interface EntryPointExecutor {

    /**
     * Unique logical name matching "EntryPointName" in JSON test suite files.
     */
    String getEntryPointName();

    /**
     * Java DTO class to deserialize the JSON request payload into.
     */
    Class<?> getRequestType();

    /**
     * Invokes the target domain service using the deserialized DTO request.
     * @param request Deserialized instance of getRequestType()
     * @return Response object (DTO, Map, Record, or String)
     */
    Object execute(Object request);
}
```

### Example Executor Implementation

Below is a complete example from this demo application ([OrderProcessingExecutor.java](file:///Users/mothusi/workspace/pathora-test-harness/example/src/main/java/com/example/demo/executor/OrderProcessingExecutor.java)):

```java
package com.example.demo.executor;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderProcessingExecutor implements EntryPointExecutor {

    @Override
    public String getEntryPointName() {
        return "order-processing-service"; // Matches "EntryPointName" in JSON test files
    }

    @Override
    public Class<?> getRequestType() {
        return OrderRequest.class; // Target DTO class
    }

    @Override
    public Object execute(Object request) {
        OrderRequest orderReq = (OrderRequest) request;

        // 1. Calculate business logic
        int totalItems = 0;
        double subtotal = 0.0;
        if (orderReq.items() != null) {
            for (OrderRequest.OrderItem item : orderReq.items()) {
                totalItems += item.quantity();
                subtotal += (item.price() * item.quantity());
            }
        }

        double tax = subtotal * 0.15; // 15% tax
        double total = subtotal + tax;

        // 2. Return response domain DTO
        return new OrderResponse(
                "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                orderReq.customerId(),
                totalItems,
                subtotal,
                tax,
                total,
                "CREATED"
        );
    }
}
```

In a Spring Boot application, simply annotate your executor with `@Component`. The `EntryPointRegistry` automatically collects all registered `EntryPointExecutor` beans upon startup.

---

## How Pathora Test Harness Works

1. **Loader Phase**: `TestSuiteLoader` reads a JSON test file (e.g. `templates/tests/order-checkout-test.json`) and loads the base request template specified in `"DefaultJSONRequestPath"` (`templates/requests/order-checkout-request.json`).
2. **Mutation Phase**: `JsonMutationEngine` takes the base request JSON and applies any JSONPath overrides declared in `"TestCaseParameterValues"`.
3. **Dispatch Phase**: `EntryPointDispatcher` receives the mutated JSON payload, locates the matching `EntryPointExecutor` via `getEntryPointName()`, deserializes the JSON to the executor's `getRequestType()`, and calls `execute()`.
4. **Assertion Phase**: `AssertionEngine` converts the response to JSON and evaluates all rules declared in `"ResponseAssertions"`.

---

## Directory Structure

```text
example/
├── pom.xml                               # Spring Boot POM depending on pathora-test-harness
├── README.md                             # This usage guide
├── templates/
│   ├── requests/                         # Base JSON Request Templates
│   │   ├── user-create-request.json
│   │   ├── order-checkout-request.json
│   │   ├── payment-process-request.json
│   │   ├── inventory-update-request.json
│   │   ├── loan-application-request.json
│   │   └── complex-policy-request.json   # Deeply nested request template
│   └── tests/                            # Declarative JSON Test Suite Files
│       ├── user-create-test.json
│       ├── order-checkout-test.json
│       ├── payment-process-test.json
│       ├── inventory-update-test.json
│       ├── loan-application-test.json
│       ├── policy-evaluation-test.json   # Comprehensive operator test suite
│       └── policy-risk-assessment-test.json
└── src/
    ├── main/
    │   └── java/
    │       └── com/example/demo/
    │           ├── DemoApplication.java
    │           ├── config/
    │           │   └── TestHarnessConfig.java # Spring configuration for Pathora beans
    │           ├── dto/                  # Request / Response DTO Records
    │           └── executor/             # EntryPointExecutor SPI Implementations
    │               ├── UserRegistrationExecutor.java
    │               ├── OrderProcessingExecutor.java
    │               ├── PaymentGatewayExecutor.java
    │               ├── InventoryUpdateExecutor.java
    │               ├── LoanApplicationExecutor.java
    │               └── ComplexPolicyExecutor.java
    └── test/
        └── java/
            └── com/example/demo/
                ├── SingleTestSuiteDemoTest.java # Tests individual files explicitly
                ├── AllSuiteTest.java             # Batch test execution using @TestFactory
                └── adapter/
                    └── DynamicTestAdapter.java  # Scans templates/tests/ & builds DynamicNode stream
```

---

## Writing Test Suites & Request Templates

### 1. Base Request Template (`templates/requests/order-checkout-request.json`)
```json
{
  "customerId": "CUST-10001",
  "items": [
    { "productId": "PROD-A1", "quantity": 2, "price": 49.99 },
    { "productId": "PROD-B2", "quantity": 1, "price": 19.99 }
  ],
  "currency": "USD"
}
```

### 2. Test Suite File (`templates/tests/order-checkout-test.json`)
```json
{
  "DefaultJSONRequestPath": "../requests/order-checkout-request.json",
  "Tests": [
    {
      "TestName": "Order Checkout Calculation Test",
      "TestDescription": "Validates total items, subtotal, 15% tax, and order status.",
      "EntryPointName": "order-processing-service",
      "TestCaseParameterValues": [
        { "JsonPath": "$.customerId", "Value": "CUST-99001" }
      ],
      "ResponseAssertions": [
        {
          "JsonPath": "$.orderId",
          "Operator": "STARTS_WITH",
          "Value": "ORD-",
          "Description": "Order ID must start with ORD-"
        },
        {
          "JsonPath": "$.customerId",
          "Value": "CUST-99001",
          "Description": "Omitted Operator field automatically defaults to EQUALS"
        },
        {
          "JsonPath": "$.subtotal",
          "Operator": "GREATER_THAN",
          "Value": 100.0
        }
      ]
    }
  ]
}
```

> **Tip**: If `"Operator"` is omitted, Pathora Test Harness automatically defaults to `EQUALS`.

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
[INFO] Tests run: 8, Failures: 0, Errors: 0

[INFO] Running com.example.demo.AllSuiteTest
[INFO] Tests run: 15, Failures: 0, Errors: 0

[INFO] Results:
[INFO] Tests run: 23, Failures: 0, Errors: 0
[INFO] BUILD SUCCESS
```

---

## Testing Approaches

Pathora Test Harness supports two test runner styles:

### Approach A: Individual File Testing (`SingleTestSuiteDemoTest.java`)
Ideal when a developer wants to target and debug a specific `.json` test file:

```java
@SpringBootTest
class SingleTestSuiteDemoTest {

    @Autowired
    private TestSuiteLoader testSuiteLoader;
    @Autowired
    private EntryPointDispatcher dispatcher;
    @Autowired
    private JsonMutationEngine mutationEngine;
    @Autowired
    private AssertionEngine assertionEngine;

    @Test
    @DisplayName("Execute Order Checkout Test Suite")
    void testOrderCheckoutSuite() throws Exception {
        runTestSuite("templates/tests/order-checkout-test.json");
    }
}
```

### Approach B: Dynamic Directory Batch Execution (`AllSuiteTest.java`)
Ideal for CI/CD pipelines. Uses JUnit 5 `@TestFactory` to automatically discover all test suite JSON files in `templates/tests/` and execute them in a single test run:

```java
@SpringBootTest
class AllSuiteTest {

    @Autowired
    private DynamicTestAdapter adapter;

    @TestFactory
    Stream<DynamicNode> executeTestSuite() {
        return adapter.generate("templates/tests");
    }
}
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

---
