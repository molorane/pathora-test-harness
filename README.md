# Pathora Test Harness

**Pathora Test Harness** is a lightweight, high-performance Java testing framework designed to evaluate complex, deeply nested enterprise services using declarative JSON test suites, JSONPath parameter mutations, service entry point executors, and a rich assertion engine.

---

## 📚 Documentation & Guides

- **[Why Pathora Test Harness Exists (Design Rationale)](docs/WHY_PATHORA.md)**: Explains the problem Pathora solves, why traditional Java DTO builders and HTTP testing tools fall short, and why Pathora relies on JayWay JsonPath for in-memory payload mutations and assertions.
- **[Industry Readiness, Reliability & Accuracy Assurance](docs/INDUSTRY_READINESS_AND_RELIABILITY.md)**: Details the architectural guarantees, multi-threaded safety, type normalization, and 650+ test verification suite that ensure 100% correct evaluations in enterprise production environments.
- **[Dynamic Date Expressions, Timezones & Temporal Testing Guide](docs/DATE_EXPRESSIONS_AND_TIMEZONES.md)**: Complete guide to dynamic temporal tokens (`{{$CURRENT_DATE}}`, `{{$CURRENT_DATETIME}}`, relative offsets, custom formats), 6 timezone configuration methods, and deterministic `PathoraClock` time-travel testing.
- **[Complete Assertion Operators Reference Catalog](docs/ASSERTION_OPERATORS_CATALOG.md)**: Full catalog of all supported assertion operators across Scalar, String, Date, DateTime, Duration, Structural, Array, Object, Money, and Logical categories.
- **[Assertion Operator In-Depth Usage Guide](docs/OPERATOR_USAGE_GUIDE.md)**: Comprehensive guide with JSON payload examples and schema configurations for every operator.
- **[Architecture & Design Patterns](docs/DESIGN_PATTERNS.md)**: Explains the design patterns used throughout the codebase (Strategy, Factory, Registry, Dispatcher, Builder, SPI).
- **[Development Guidelines & Coding Standards](docs/CODE_GUIDES.md)**: Standards, principles, and guidelines for contributors.
- **[Spring Boot Demo Application & Executor Guide](example/README.md)**: A complete, working Spring Boot 4.1.0 demo project showcasing how to write `EntryPointExecutor` SPI adapters, JSON request templates, and JSON test suite definitions.

---

## 🌟 Key Features

- 📄 **Declarative JSON Test Suites**: Store base JSON request templates and test definitions in human-readable JSON files.
- ⚡ **Surgical Parameter Mutation**: Mutate specific JSON properties using JSONPath expressions (`TestCaseParameterValues`), eliminating duplicate test data files.
- 🔌 **In-Process SPI Execution (`EntryPointExecutor`)**: Dispatches mutated requests directly to Java DTOs and Spring `@Service` beans in-memory. **Zero HTTP network latency, zero web server startup overhead.**
- 🎯 **Rich JsonPath Assertions**: Validate response nodes using Scalar, String, Date/Time, Duration, Structural, Array, Object, and Logical operators.
- 🕒 **Dynamic Date Expressions & Time-Travel Testing**: Use dynamic tokens like `{{$CURRENT_DATE}}`, `{{$CURRENT_DATETIME}}`, relative offsets (`+30d`, `-25y`, `+2h`), epoch timestamps, and thread-safe `PathoraClock` freezing for deterministic date assertions and payload mutations.
- 🧪 **Flexible Test Runners**: Supports both individual test file execution (`SingleTestSuiteDemoTest`) and dynamic directory batch execution (`AllSuiteTest` via JUnit 5 `@TestFactory`).

---

## 🚀 Quick Start

### 1. Add Dependency (Maven)

```xml
<dependency>
    <groupId>io.github.molorane</groupId>
    <artifactId>pathora-test-harness</artifactId>
    <version>0.0.4</version>
</dependency>
```

### 2. Implement an `EntryPointExecutor` SPI Adapter

```java
package com.example.demo.executor;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessingExecutor implements EntryPointExecutor<OrderRequest, OrderResponse> {

    @Override
    public String getEntryPointName() {
        return "order-processing-service";
    }

    @Override
    public Class<OrderRequest> getRequestType() {
        return OrderRequest.class;
    }

    @Override
    public OrderResponse execute(OrderRequest orderReq) {
        // Invoke domain service logic and return response DTO
        return new OrderResponse("ORD-1001", orderReq.customerId(), 150.00, "CREATED");
    }
}
```

### 3. Define Request Template & Test Suite

**Request Template (`templates/requests/order-request.json`)**:
```json
{
  "customerId": "CUST-10001",
  "currency": "USD"
}
```

**Test Suite (`templates/tests/order-test.json`)**:
```json
{
  "DefaultJSONRequestPath": "../requests/order-request.json",
  "Tests": [
    {
      "TestName": "Order Checkout Calculation Test",
      "TestDescription": "Validates customer ID and CREATED status.",
      "EntryPointName": "order-processing-service",
      "TestCaseParameterValues": [
        { "JsonPath": "$.customerId", "Value": "CUST-99001" }
      ],
      "ResponseAssertions": [
        {
          "JsonPath": "$.orderId",
          "Operator": "STARTS_WITH",
          "Value": "ORD-"
        },
        {
          "JsonPath": "$.status",
          "Value": "CREATED"
        }
      ]
    }
  ]
}
```

### 4. Dynamic Date & Time Expressions

Pathora provides built-in expression resolution for date mutations and assertions:

```json
{
  "name": "Applicant Application Test",
  "operation": "loan-service",
  "mutations": [
    { "path": "$.applicationDate", "value": "{{$CURRENT_DATE}}" },
    { "path": "$.dateOfBirth", "value": "{{$CURRENT_DATE - 25y}}" },
    { "path": "$.submittedAt", "value": "{{$CURRENT_DATETIME}}" }
  ],
  "assertions": [
    { "path": "$.approvalDate", "operator": "DATE_EQUALS", "value": "{{$CURRENT_DATE}}" },
    { "path": "$.expiryDate", "operator": "DATE_EQUALS", "value": "{{$CURRENT_DATE + 30d}}" },
    {
      "path": "$.evaluatedAt",
      "operator": "DATETIME_WITHIN_LAST",
      "value": { "amount": 5, "unit": "MINUTES" }
    }
  ]
}
```

#### Deterministic Time-Travel Testing with `PathoraClock`
```java
// Freeze time globally or per-thread during tests
PathoraClock.freeze(LocalDate.of(2028, 2, 29)); // leap day
PathoraClock.freeze(Instant.parse("2026-12-31T23:59:59Z"));

// Reset when done
PathoraClock.reset();
```

### 5. Run the Demo

```bash
cd example
sh ../mvnw test
```

---

## 📄 License

This project is open-source software licensed under the MIT License.
