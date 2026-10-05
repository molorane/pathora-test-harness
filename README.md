# Pathora Test Harness

**Pathora Test Harness** is a lightweight, high-performance Java testing framework designed to evaluate complex, deeply nested enterprise services using declarative JSON test suites, JSONPath parameter mutations, service entry point executors, and a rich assertion engine.

---

## 📚 Documentation & Guides

- **[Why Pathora Test Harness Exists (Design Rationale)](WHY_PATHORA.md)**: Explains the problem Pathora solves, why traditional Java DTO builders and HTTP testing tools fall short, and why Pathora relies on JayWay JSONPath for in-memory payload mutations and assertions.
- **[Spring Boot Demo Application & Executor Guide](example/README.md)**: A complete, working Spring Boot 3.4.1 demo project showcasing how to write `EntryPointExecutor` SPI adapters, JSON request templates, and JSON test suite definitions.

---

## 🌟 Key Features

- 📄 **Declarative JSON Test Suites**: Store base JSON request templates and test definitions in human-readable JSON files.
- ⚡ **Surgical Parameter Mutation**: Mutate specific JSON properties using JSONPath expressions (`mutations`), eliminating duplicate test data files.
- 🔌 **In-Process SPI Execution (`EntryPointExecutor`)**: Dispatches mutated requests directly to Java DTOs and Spring `@Service` beans in-memory. **Zero HTTP network latency, zero web server startup overhead.**
- 🎯 **Rich JSONPath Assertions**: Validate response nodes using Scalar, String, Date/Time, Duration, Structural, Array, Object, and Logical operators.
- 🧪 **Flexible Test Runners**: Supports both individual test file execution (`SingleTestSuiteDemoTest`) and dynamic directory batch execution (`AllSuiteTest` via JUnit 5 `@TestFactory`).

---

## 🚀 Quick Start

### 1. Add Dependency (Maven)

```xml
<dependency>
    <groupId>io.github.molorane</groupId>
    <artifactId>pathora-test-harness</artifactId>
    <version>1.0.0</version>
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
    public OrderResponse execute(OrderRequest request) {
        // Invoke domain service logic and return response DTO
        return new OrderResponse("ORD-1001", request.customerId(), 150.00, "CREATED");
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

### 4. Run the Demo

```bash
cd example
sh ../mvnw test
```

---

## 📄 License

This project is open-source software licensed under the MIT License.
