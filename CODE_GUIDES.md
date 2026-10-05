# PATHORA TEST HARNESS - DEVELOPMENT GUIDELINES

This document defines the coding and extension conventions for the
`pathora-test-harness` library. It reflects the current source code, not
older PascalCase suite examples.

------------------------------------------------------------------------

# Core Philosophy

The harness is a framework-independent, operator-driven assertion
library. Its job is to:

1. Load a JSON test suite file.
2. Mutate a base JSON or XML request using `JsonMutation` entries.
3. Dispatch the mutated request through a registered `EntryPointExecutor`.
4. Assert the JSON response using a tree of `JsonAssertion` nodes.

The core library does not depend on Spring or any dependency injection
framework. Consumer applications can wire the pieces however they like.

------------------------------------------------------------------------

# Architecture Overview

```text
TestSuiteLoader           -> deserializes *.json suite files into TestSuite
RequestLoader             -> reads the base request template from disk
JsonMutationEngine        -> applies JsonMutation list to the base request
EntryPointDispatcher      -> deserializes request, calls EntryPointExecutor, serializes response
AssertionEngine           -> evaluates the tree of JsonAssertion nodes
OperatorRegistry          -> discovers AssertionEvaluator implementations via ServiceLoader
ResponseAssertionExecutor -> thin facade over AssertionEngine
FailureLogger             -> writes structured failure reports to disk
```

------------------------------------------------------------------------

# Package Structure

```text
engine/
  operator/           -> one class per operator
model/                -> records and enums
exception/            -> HarnessAssertionException
loader/               -> file I/O
registry/             -> EntryPointRegistry
spi/                  -> EntryPointExecutor interface
util/                 -> shared helpers
```

Operator implementations live in category subpackages under
`engine/operator/`, such as `scalar`, `string`, `list`, `object`,
`date`, `datetime`, `duration`, `money`, and `structural`.

------------------------------------------------------------------------

# Model Layer

All suite model types are Java records annotated with
`@JsonIgnoreProperties(ignoreUnknown = true)`. Public suite JSON uses
lower camel-case keys.

```java
@JsonIgnoreProperties(ignoreUnknown = true)
public record JsonAssertion(
        @JsonProperty("path")
        String path,

        @JsonProperty("operator")
        AssertionOperator operator,

        @JsonProperty("value")
        Object value,

        @JsonProperty("description")
        String description,

        @JsonProperty("assertions")
        List<JsonAssertion> assertions
) {
    public JsonAssertion {
        if (operator == null) {
            operator = AssertionOperator.EQUALS;
        }
    }
}
```

## JSON Field Contract

| Model | Java field | JSON key |
|---|---|---|
| `TestSuite` | `requestPath` | `requestPath` |
| `TestSuite` | `xmlRequestPath` | `xmlRequestPath` |
| `TestSuite` | `tests` | `tests` |
| `RuleTestCase` | `name` | `name` |
| `RuleTestCase` | `description` | `description` |
| `RuleTestCase` | `operation` | `operation` |
| `RuleTestCase` | `mutations` | `mutations` |
| `RuleTestCase` | `assertions` | `assertions` |
| `JsonMutation` | `path` | `path` |
| `JsonMutation` | `value` | `value` |
| `JsonAssertion` | `path` | `path` |
| `JsonAssertion` | `operator` | `operator` |
| `JsonAssertion` | `value` | `value` |
| `JsonAssertion` | `description` | `description` |
| `JsonAssertion` | `assertions` | `assertions` |

Do not use the older PascalCase keys such as `JSONPath`, `Operator`,
`Value`, `EntryPointName`, `TestCaseParameterValues`, or
`ResponseAssertions` in new examples.

------------------------------------------------------------------------

# Assertion Operators

Every new operator must be added to `AssertionOperator` in the correct
category block.

`AND`, `OR`, and `NOT` are not registered as service-loaded evaluators.
They are handled directly by `AssertionEngine.evaluateAssertion()`.

------------------------------------------------------------------------

# Operator Pattern

## Standard Evaluator

Use `AssertionEvaluator` for operators that work on one resolved JSONPath
value.

```java
public class MyNewEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.MY_NEW_OPERATOR;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        Object[] normalized = AssertionUtils.normalizeTypes(normalizedActual, expected);

        if (/* condition fails */) {
            throw new HarnessAssertionException(
                    AssertionOperator.MY_NEW_OPERATOR,
                    path,
                    normalized[1],
                    normalized[0],
                    "MY_NEW_OPERATOR failed at " + path);
        }
    }
}
```

## Context-Aware Evaluator

Use `DocumentContextAwareEvaluator` only when the operator must resolve
multiple paths from the same response document.

```java
public class MyMultiPathEvaluator implements DocumentContextAwareEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.MY_MULTI_PATH_OPERATOR;
    }

    @Override
    public void apply(DocumentContext context, Object expected) {
        Map<String, Object> config = AssertionUtils.toMap(expected);
        String leftPath = String.valueOf(config.get("leftPath"));
        String rightPath = String.valueOf(config.get("rightPath"));

        Object left = context.read(leftPath);
        Object right = context.read(rightPath);

        if (/* condition fails */) {
            throw new HarnessAssertionException(
                    AssertionOperator.MY_MULTI_PATH_OPERATOR,
                    leftPath + " vs " + rightPath,
                    right,
                    left,
                    "MY_MULTI_PATH_OPERATOR failed");
        }
    }
}
```

`DocumentContextAwareEvaluator` already provides a default
`apply(String, Object, Object, boolean)` method that throws
`UnsupportedOperationException`. Do not override it.

## Registration

Operators are discovered with Java `ServiceLoader`.

After creating a new evaluator class, add its fully qualified class name
to:

```text
src/main/resources/META-INF/services/io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator
```

Do not register evaluators in `AssertionEngine`.

------------------------------------------------------------------------

# Shared Helpers

Use `AssertionUtils` instead of duplicating normalization and comparison
logic.

| Helper | When to use |
|---|---|
| `normalizeResult(actual, path)` | Before scalar comparison; unwraps singleton JSONPath lists |
| `normalizeTypes(actual, expected)` | Before numeric/string comparison |
| `normalizeExpected(expected)` | Converts string `"true"`, `"42"`, and `"null"` where useful |
| `requireList(value, path)` | Validates that `actual` is a `List` |
| `toMap(value)` | Casts object configs to `Map<String, Object>` |
| `objectContainsFields(actual, expected, ignoreNulls)` | Deep object field comparison |
| `deepEquals(actual, expected)` | Recursive equality with type normalization |

------------------------------------------------------------------------

# Assertion Failures

Operator assertion failures should throw `HarnessAssertionException`.
This preserves operator, path, expected value, actual value, and optional
test metadata.

Some orchestration failures in `AssertionEngine` still use plain
`AssertionError` for logical and JSONPath runtime errors. New operator
code should still prefer `HarnessAssertionException`.

------------------------------------------------------------------------

# Logical Operators

Logical operators use the nested `assertions` list.

- `AND` requires at least one nested assertion. Every nested assertion
  must pass. It is not limited to two assertions.
- `OR` requires at least one nested assertion. It passes as soon as one
  nested assertion passes. It is not limited to two assertions.
- `NOT` requires exactly one nested assertion. The nested assertion must
  fail for `NOT` to pass.

Example:

```json
{
  "operator": "OR",
  "assertions": [
    { "path": "$.status", "operator": "EQUALS", "value": "APPROVED" },
    { "path": "$.manualReview", "operator": "IS_TRUE" },
    { "path": "$.riskScore", "operator": "LESS_THAN", "value": 10 }
  ]
}
```

------------------------------------------------------------------------

# EntryPointExecutor SPI

Consumer projects implement `EntryPointExecutor<REQ, RES>` to plug in
business logic.

```java
public interface EntryPointExecutor<REQ, RES> {
    String getEntryPointName();
    Class<REQ> getRequestType();
    RES execute(REQ request);
}
```

Rules:

- `getEntryPointName()` must match the test case `operation` value.
- `getRequestType()` returns the Jackson deserialization target type.
- `execute()` returns a serializable response object.
- Consumer projects register executors with
  `EntryPointRegistry(List<EntryPointExecutor<?, ?>> executors)`.

------------------------------------------------------------------------

# Testing Guide

- Every evaluator should have a focused unit test class.
- Test pass cases, fail cases, null handling, type coercion, and edge
  cases appropriate to the operator.
- Operator-level tests should instantiate the evaluator directly.
- Integration tests can construct `AssertionEngine`, `JsonAssertion`,
  and `RuleTestCase` inline without file I/O.
- For logical operator tests, assert message fragments such as
  `LOGICAL_OR_FAILED` and `LOGICAL_NOT_FAILED`.

Example assertion snippet in tests:

```json
{ "path": "$.field", "operator": "MY_NEW_OPERATOR", "value": "expected" }
```

------------------------------------------------------------------------

# Checklist: Adding a New Operator

1. Add the new value to `AssertionOperator`.
2. Create the evaluator in the matching `engine/operator/<category>/`
   package.
3. Implement `AssertionEvaluator` or `DocumentContextAwareEvaluator`.
4. Use `AssertionUtils` helpers where applicable.
5. Throw `HarnessAssertionException` for assertion failures.
6. Add the evaluator class name to the `META-INF/services` file.
7. Add focused unit tests.
8. Add integration coverage when the operator is user-facing.
9. Run `mvn test`.

------------------------------------------------------------------------

# Checklist: Adding a New Entry Point

1. Implement `EntryPointExecutor<REQ, RES>` in the consumer project.
2. Register it in `EntryPointRegistry`.
3. Create a suite JSON file whose test case `operation` matches
   `getEntryPointName()`.
4. Create a base request template.
5. Add `mutations` and `assertions`.
