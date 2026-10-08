# Operator Extension Guide

This document is intentionally limited to operator extension and plugin registration. It focuses on how to add custom assertion operators to Pathora Test Harness without changing the core built-in operator set.

## Overview

Pathora Test Harness supports domain-specific assertion logic through custom operators registered at runtime. The extension model is deliberately simple:

- built-in operators continue to live in the core enum and engine
- custom operators are registered by string name, not enum value
- a custom operator is implemented as an `AssertionEvaluator`
- the custom evaluator is bound into the `AssertionEngine` via `registerOperator(...)`

That means you can add business rules like `HAS_ACTIVE_SUBSCRIPTION` or `IN_REGION` without modifying the library source or disturbing the standard operator catalog.

## The extension contract

Each custom operator must provide an `AssertionEvaluator` implementation. The key method is `apply(...)`, which receives:

- `path`: the JSONPath of the value being asserted
- `actual`: the value extracted from the response
- `expected`: the expected configuration or value from the assertion definition
- `pathExists`: whether the JSONPath existed in the response

A custom evaluator can also override `operatorName()`. The engine resolves custom operators by name and performs a case-insensitive match.

```java
public final class HasActiveSubscriptionEvaluator implements AssertionEvaluator {
    @Override
    public String operatorName() {
        return "HAS_ACTIVE_SUBSCRIPTION";
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        // domain-specific validation logic
    }
}
```

## Registration pattern

The recommended pattern is to register extension evaluators in a dedicated configuration class or startup bootstrap. The registration is done on the shared `AssertionEngine` instance:

```java
AssertionEngine engine = new AssertionEngine();
engine.registerOperator("HAS_ACTIVE_SUBSCRIPTION", new HasActiveSubscriptionEvaluator());
engine.registerOperator("IN_REGION", new InRegionEvaluator());
```

The registry accepts custom names in a case-insensitive manner, so these names are all equivalent:

- `HAS_ACTIVE_SUBSCRIPTION`
- `Has_Active_Subscription`
- `has_active_subscription`

The same rule applies to `IN_REGION`.

## Example: subscription and region validation

The extension pattern is demonstrated in the example project with a dedicated service and executor. The service fetches the business facts, while the custom evaluators decide whether they satisfy the rule.

### 1. Domain service

The service is responsible for business facts, such as the current subscription status and customer region.

```java
@Service
public class CustomerEligibilityService {
    public String getSubscriptionStatus(String customerId) {
        // returns ACTIVE, INACTIVE, etc.
    }

    public String getRegion(String customerId) {
        // returns EU, US, APAC, etc.
    }
}
```

### 2. Dedicated entry point executor

The executor exposes the service through the harness entry point SPI without changing the core engine or any built-in operator logic.

```java
@Component
public class CustomerEligibilityExecutor implements EntryPointExecutor<CustomerEligibilityRequest, CustomerEligibilityResponse> {
    @Override
    public String getEntryPointName() {
        return "customer-eligibility-service";
    }

    @Override
    public CustomerEligibilityResponse execute(CustomerEligibilityRequest request) {
        // delegate to domain service
    }
}
```

### 3. Operator registration

The custom operators are then registered in a dedicated configuration class:

```java
@Bean
public CustomOperatorRegistrar customOperatorRegistrar(AssertionEngine assertionEngine,
        CustomerEligibilityService customerEligibilityService) {
    return new CustomOperatorRegistrar(assertionEngine, customerEligibilityService);
}
```

Inside the registrar, you add:

```java
assertionEngine.registerOperator("HAS_ACTIVE_SUBSCRIPTION", new HasActiveSubscriptionEvaluator(customerEligibilityService));
assertionEngine.registerOperator("IN_REGION", new InRegionEvaluator(customerEligibilityService));
```

## Assertion examples

The custom operators can then be used in declarative test suites:

```json
{
  "path": "$.customerId",
  "operator": "HAS_ACTIVE_SUBSCRIPTION",
  "value": "ACTIVE"
}
```

```json
{
  "path": "$.customerId",
  "operator": "IN_REGION",
  "value": ["EU", "APAC"]
}
```

These operators evaluate business rules at runtime using the service and do not depend on a new enum value in the library.

## Best practices

- Keep the custom evaluator thin and focused on one business rule.
- Put the domain lookups in a service layer rather than inside the evaluator.
- Register custom operators in a dedicated config class so startup wiring remains explicit.
- Prefer names that are stable, uppercase, and obvious for test authors.
- Return clear assertion failures with meaningful messages for production troubleshooting.

## Why this is the right pattern

This pattern keeps the core library stable while allowing feature teams to extend the assertion vocabulary with business rules that are meaningful to their domain. No changes to existing built-in operators are required, and the extension remains isolated to the consuming project.

