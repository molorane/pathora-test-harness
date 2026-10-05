# Design Patterns & Principles in Pathora Test Harness

This document describes the architecture used by the current
`pathora-test-harness` codebase.

---

## Strategy Pattern

Assertion behavior is selected at runtime through the
`AssertionEvaluator` interface.

```java
public interface AssertionEvaluator {
    AssertionOperator operator();

    void apply(String path, Object actual, Object expected, boolean pathExists);
}
```

Concrete evaluators such as `EqualsEvaluator`, `GreaterThanEvaluator`,
`ContainsAllEvaluator`, and `MoneyEqualsEvaluator` encapsulate one
operator each. `AssertionEngine` does not need a switch statement for
ordinary operators; it asks `OperatorRegistry` for the evaluator that
supports the assertion's `AssertionOperator`.

---

## ServiceLoader Registry Pattern

Operators are discovered through Java `ServiceLoader`.

```java
ServiceLoader.load(AssertionEvaluator.class)
```

The registry builds a map from each evaluator's `operator()` value to
the evaluator instance:

```java
private Map<AssertionOperator, AssertionEvaluator> loadOperators() {
    return StreamSupport.stream(
                    ServiceLoader.load(AssertionEvaluator.class).spliterator(),
                    false)
            .collect(Collectors.toUnmodifiableMap(
                    AssertionEvaluator::operator,
                    Function.identity()
            ));
}
```

New evaluators are registered by adding their fully qualified class name
to:

```text
src/main/resources/META-INF/services/io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator
```

This keeps `AssertionEngine` closed to ordinary operator-registration
changes.

---

## Entry Point SPI Pattern

Consumer applications plug business logic into the harness by
implementing `EntryPointExecutor<REQ, RES>`.

```java
public interface EntryPointExecutor<REQ, RES> {
    String getEntryPointName();
    Class<REQ> getRequestType();
    RES execute(REQ request);
}
```

`EntryPointRegistry` stores these executors by name. A test case's
`operation` value selects the executor used by `EntryPointDispatcher`.

---

## Context-Aware Evaluators

Most evaluators receive a single JSONPath result. Operators that need
the whole response document implement `DocumentContextAwareEvaluator`.

```java
public interface DocumentContextAwareEvaluator extends AssertionEvaluator {
    void apply(DocumentContext context, Object expected);
}
```

Examples include cross-field object operators such as
`FIELD_EQUALS_OTHER_FIELD`, where the evaluator needs to read multiple
paths from the same response.

---

## Composite Pattern

`JsonAssertion` supports nested assertions:

```java
public record JsonAssertion(
        String path,
        AssertionOperator operator,
        Object value,
        String description,
        List<JsonAssertion> assertions
) {
}
```

Logical operators use that tree structure:

```json
{
  "operator": "AND",
  "assertions": [
    { "path": "$.status", "operator": "EQUALS", "value": "APPROVED" },
    {
      "operator": "OR",
      "assertions": [
        { "path": "$.amount", "operator": "GREATER_THAN", "value": 1000 },
        { "path": "$.priority", "operator": "EQUALS", "value": "HIGH" }
      ]
    }
  ]
}
```

`AND` and `OR` accept one or more nested assertions. `NOT` accepts
exactly one.

---

## Evaluation Template

`AssertionEngine` follows a fixed evaluation sequence:

1. Parse the response with Jayway JSONPath.
2. Handle `AND`, `OR`, and `NOT` directly.
3. Look up the evaluator in `OperatorRegistry`.
4. If the evaluator is context-aware, pass the full `DocumentContext`.
5. Otherwise, resolve `assertion.path()` and call the evaluator.

This keeps logical composition, JSONPath resolution, and operator logic
separate.

---

## Design Principles

### Single Responsibility

Each layer has one job:

| Class | Responsibility |
|---|---|
| `AssertionEngine` | Orchestrate assertion evaluation |
| `OperatorRegistry` | Discover and map evaluators |
| `AssertionEvaluator` implementations | Perform one assertion algorithm |
| `JsonMutationEngine` | Apply request mutations |
| `EntryPointDispatcher` | Deserialize, execute, and serialize entry point calls |
| `FailureLogger` | Write structured failure reports |

### Open/Closed

Adding a normal operator does not require editing `AssertionEngine`.
Add the enum value, create the evaluator, register it in the service
file, and test it.

### Interface Segregation

Simple evaluators implement `AssertionEvaluator`. Only operators that
need the full JSON document implement `DocumentContextAwareEvaluator`.

### Fail Fast

Path errors, missing operators, failed assertions, and mutation failures
surface immediately with diagnostic context. Operator failures should use
`HarnessAssertionException` so failures include operator, path,
expected, and actual values.

### Framework Independence

The core harness uses plain Java construction. The example project wires
it with Spring, but Spring is not part of the core library contract.

---

## Implications for New Development

Do:

- Implement `AssertionEvaluator` for ordinary operators.
- Implement `DocumentContextAwareEvaluator` only when multiple response
  paths are required.
- Register new evaluators in the `META-INF/services` file.
- Use lower camel-case suite fields: `requestPath`, `tests`,
  `operation`, `mutations`, `assertions`, `path`, `operator`, `value`.
- Keep models immutable records.
- Use `AssertionUtils` instead of duplicating normalization logic.

Do not:

- Register operators inside `AssertionEngine`.
- Use old PascalCase suite fields in new docs or examples.
- Add framework dependencies to the core library.
- Hide `PathNotFoundException` or mutation failures.
- Add deep inheritance hierarchies for operators.
