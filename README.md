# Pathora Test Harness

**Pathora Test Harness** is a lightweight, high-performance Java testing framework designed to evaluate complex, deeply nested enterprise services using declarative JSON test suites, JSONPath parameter mutations, service entry point executors, and a rich assertion engine.

---

## 📚 Documentation & Guides

- **[Why Pathora Test Harness Exists (Design Rationale)](docs/WHY_PATHORA.md)**: Explains the problem Pathora solves, why traditional Java DTO builders and HTTP testing tools fall short, and why Pathora relies on JayWay JsonPath for in-memory payload mutations and assertions.
- **[Industry Readiness, Reliability & Accuracy Assurance](docs/INDUSTRY_READINESS_AND_RELIABILITY.md)**: Details the architectural guarantees, multi-threaded safety, type normalization, and 650+ test verification suite that ensure 100% correct evaluations in enterprise production environments.
- **[Dynamic Date Expressions, Timezones & Temporal Testing Guide](docs/DATE_EXPRESSIONS_AND_TIMEZONES.md)**: Complete guide to dynamic temporal tokens (`{{$CURRENT_DATE}}`, `{{$CURRENT_DATETIME}}`, relative offsets, custom formats), 7 timezone configuration methods, and deterministic `PathoraClock` time-travel testing.
- **[Complete Assertion Operators Reference Catalog](docs/ASSERTION_OPERATORS_CATALOG.md)**: Full catalog of all supported assertion operators across Scalar, String, Date, DateTime, Duration, Structural, Array, Object, Money, and Logical categories.
- **[Assertion Operator In-Depth Usage Guide](docs/OPERATOR_USAGE_GUIDE.md)**: Comprehensive guide with JSON payload examples and schema configurations for every operator.
- **[Architecture & Design Patterns](docs/DESIGN_PATTERNS.md)**: Explains the design patterns used throughout the codebase (Strategy, Factory, Registry, Dispatcher, Builder, SPI).
- **[Development Guidelines & Coding Standards](docs/CODE_GUIDES.md)**: Standards, principles, and guidelines for contributors.
- **[Spring Boot Demo Application & Executor Guide](example/README.md)**: A complete, working Spring Boot 4.1.0 demo project showcasing how to write `EntryPointExecutor` SPI adapters, JSON request templates, and JSON test suite definitions.

---

## 🌟 Key Features

- 📄 **Declarative JSON Test Suites**: Store base JSON request templates and test definitions in human-readable JSON files.
- ⚡ **Surgical Parameter Mutation**: Mutate specific JSON properties using JSONPath expressions within `mutations`, eliminating duplicate test data files.
- 🔌 **In-Process SPI Execution (`EntryPointExecutor`)**: Dispatches mutated requests directly to Java DTOs and Spring `@Service` beans in-memory. **Zero HTTP network latency, zero web server startup overhead.**
- 🎯 **Rich JsonPath Assertions**: Validate response nodes using Scalar, String, Date/Time, Duration, Structural, Array, Object, and Logical operators.
- 🔤 **Dynamic Expression Language & Property Resolution**: Support for dynamic temporal tokens (`{{$CURRENT_DATE}}`, `{{$CURRENT_DATETIME}}`, `{{$START_OF_MONTH}}`), random data (`{{$RANDOM_DECIMAL}}`, `{{$RANDOM_INT}}`, `{{$UUID}}`), arithmetic (`{{$MATH: 100 * 1.15}}`), security encoding/hashing (`{{$BASE64_ENCODE}}`, `{{$HASH_SHA256}}`), environment variables (`{{$ENV:PATH}}`), system properties (`{{$SYS:java.version}}`), and classpath configuration discovery (`{{$PROP:key}}` loaded automatically from `pathora.yml` or `pathora.properties`).
- 🧩 **Extensible Plugin SPI Architecture**: Register custom assertion operators (`AssertionEvaluator`) and custom expression language tokens (`ExpressionTokenEvaluator`) programmatically or via zero-code Java `ServiceLoader` SPI.
- 🕒 **Deterministic Time-Travel Testing**: Freeze the library clock (`PathoraClock`) globally or per-thread for reproducible assertions.
- 🧪 **Flexible Test Runners**: Supports both individual test file execution (`SingleTestSuiteDemoTest`) and dynamic directory batch execution (`AllSuiteTest` via JUnit 5 `@TestFactory`).

---

## 🚀 Quick Start

### 1. Add Dependency (Maven)

```xml
<dependency>
    <groupId>io.github.molorane</groupId>
    <artifactId>pathora-test-harness</artifactId>
    <version>1.0.5</version>
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
  "requestPath": "../requests/order-request.json",
  "tests": [
    {
      "name": "Order Checkout Calculation Test",
      "description": "Validates customer ID and CREATED status.",
      "operation": "order-processing-service",
      "mutations": [
        { "path": "$.customerId", "value": "CUST-99001" }
      ],
      "assertions": [
        {
          "path": "$.orderId",
          "operator": "STARTS_WITH",
          "value": "ORD-"
        },
        {
          "path": "$.status",
          "value": "CREATED"
        }
      ]
    }
  ]
}
```

### 4. Dynamic Expressions & Property Resolution

Pathora provides rich dynamic expression resolution for mutations and assertions:

```json
{
  "name": "Applicant Registration & Calculation Test",
  "operation": "user-service",
  "mutations": [
    { "path": "$.applicationDate", "value": "{{$CURRENT_DATE}}" },
    { "path": "$.creditLimit", "value": "{{$RANDOM_DECIMAL:10.00:500.00:2}}" },
    { "path": "$.taxedAmount", "value": "{{$MATH: 100 * 1.15}}" },
    { "path": "$.envPath", "value": "{{$ENV:PATH:default_path}}" },
    { "path": "$.environment", "value": "{{$PROP:pathora.environment}}" },
    { "path": "$.authToken", "value": "{{$BASE64_ENCODE:admin:secret}}" }
  ],
  "assertions": [
    { "path": "$.approvalDate", "operator": "DATE_EQUALS", "value": "{{$CURRENT_DATE}}" },
    { "path": "$.taxedAmount", "operator": "EQUALS", "value": 115.0 },
    { "path": "$.environment", "value": "{{$PROP:pathora.environment}}" }
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