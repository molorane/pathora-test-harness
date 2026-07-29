# Pathora Test Harness

**Pathora Test Harness** is a lightweight, high-performance Java testing framework designed to evaluate complex, deeply nested enterprise services using declarative JSON test suites, JSONPath parameter mutations, service entry point executors, and a rich assertion engine.

---

## 📚 Documentation & Guides

- **[Why Pathora Test Harness Exists (Design Rationale)](WHY_PATHORA.md)**: Explains the problem Pathora solves, why traditional Java DTO builders and HTTP testing tools fall short, and why Pathora relies on JayWay JsonPath for in-memory payload mutations and assertions.
- **[Spring Boot Demo Application & Executor Guide](example/README.md)**: A complete, working Spring Boot 3.4.1 demo project showcasing how to write `EntryPointExecutor` SPI adapters, JSON request templates, and JSON test suite definitions.

---

## 🌟 Key Features

- 📄 **Declarative JSON Test Suites**: Store base JSON request templates and test definitions in human-readable JSON files.
- ⚡ **Surgical Parameter Mutation**: Mutate specific JSON properties using JSONPath expressions (`TestCaseParameterValues`), eliminating duplicate test data files.
- 🔌 **In-Process SPI Execution (`EntryPointExecutor`)**: Dispatches mutated requests directly to Java DTOs and Spring `@Service` beans in-memory. **Zero HTTP network latency, zero web server startup overhead.**
- 🎯 **Rich JsonPath Assertions**: Validate response nodes using Scalar, String, Date/Time, Duration, Structural, Array, Object, and Logical operators.
- 🧪 **Flexible Test Runners**: Supports both individual test file execution (`SingleTestSuiteDemoTest`) and dynamic directory batch execution (`AllSuiteTest` via JUnit 5 `@TestFactory`).

---

## 🚀 Quick Start

### 1. Add Dependency (Maven)

```xml
<dependency>
    <groupId>io.github.molorane</groupId>
    <artifactId>pathora-test-harness</artifactId>
    <version>0.0.1</version>
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
public class OrderProcessingExecutor implements EntryPointExecutor {

    @Override
    public String getEntryPointName() {
        return "order-processing-service";
    }

    @Override
    public Class<?> getRequestType() {
        return OrderRequest.class;
    }

    @Override
    public Object execute(Object request) {
        OrderRequest orderReq = (OrderRequest) request;
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

### 4. Run the Demo

```bash
cd example
sh ../mvnw test
```

---

## 📄 License

This project is open-source software licensed under the MIT License.
