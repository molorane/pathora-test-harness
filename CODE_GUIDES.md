# PATHORA TEST HARNESS — DEVELOPMENT GUIDELINES

This document defines the mandatory coding standards for the
`pathora-test-harness` library.

All contributors and AI-generated code must strictly follow these
conventions. Every rule in this guide is derived from the actual
patterns found in the existing codebase.

------------------------------------------------------------------------

# 🧭 Core Philosophy

The harness is a **framework-independent, operator-driven assertion
library**. Its job is to:

1. Load a JSON test suite file.
2. Mutate a base JSON request using `JsonMutation` entries.
3. Dispatch the mutated request through a registered `EntryPointExecutor`.
4. Assert the JSON response using a tree of `JsonAssertion` nodes.

There is **no Spring context**. There are **no annotations**. All wiring
is done via plain constructors.

------------------------------------------------------------------------

# 🏗 Architecture Overview

```
TestSuiteLoader           → deserialises *.json suite files into TestSuite
RequestTemplateLoader     → reads the base JSON request file from disk
JsonMutationEngine        → applies JsonMutation list to the base request
EntryPointDispatcher      → deserialises request, calls EntryPointExecutor, serialises response
AssertionEngine           → evaluates the tree of JsonAssertion nodes
ResponseAssertionExecutor → thin facade over AssertionEngine (single public API)
FailureLogger             → writes structured failure reports to disk
```

Every component is a plain class with constructor injection.
No static singletons, no dependency injection frameworks.

------------------------------------------------------------------------

# 📦 Package Structure (MANDATORY)

```
engine/
  operator/           ← one class per operator
model/                ← records only
exception/            ← HarnessAssertionException only
loader/               ← file I/O only
registry/             ← EntryPointRegistry
spi/                  ← EntryPointExecutor interface
util/                 ← AssertionUtils, FailureLogger
```

New classes must go into the correct package.
Do **not** create sub-packages inside `operator/`.

------------------------------------------------------------------------

# 1️⃣ Model Layer

## Rules

- All models are **Java records**.
- All records are annotated with `@JsonIgnoreProperties(ignoreUnknown = true)`.
- All record fields are annotated with `@JsonProperty("PascalCase")`.
- Records must not contain business logic.
- Records must not extend or implement anything except when required by Jackson.

## Example

```java
@JsonIgnoreProperties(ignoreUnknown = true)
public record JsonAssertion(
        @JsonProperty("JsonPath")    String jsonPath,
        @JsonProperty("Operator")    AssertionOperator operator,
        @JsonProperty("Value")       Object value,
        @JsonProperty("Description") String description,
        @JsonProperty("Assertions")  List<JsonAssertion> assertions
) {
    public JsonAssertion {
        if (operator == null) {
            operator = AssertionOperator.EQUALS;
        }
    }
}
```

## JSON field name contract

| Java record field           | JSON key                  |
|-----------------------------|---------------------------|
| `testName`                  | `TestName`                |
| `testDescription`           | `TestDescription`         |
| `entryPointName`            | `EntryPointName`          |
| `testCaseParameterValues`   | `TestCaseParameterValues` |
| `responseAssertions`        | `ResponseAssertions`      |
| `jsonPath`                  | `JsonPath`                |
| `operator`                  | `Operator`                |
| `value`                     | `Value`                   |
| `assertions`                | `Assertions`              |
| `defaultJSONRequestPath`    | `DefaultJSONRequestPath`  |

------------------------------------------------------------------------

# 2️⃣ AssertionOperator Enum

## Rules

- Every new operator **must** be added to `AssertionOperator`.
- Operators must be grouped by category with a block comment:

```java
/*
 * =========================
 * CATEGORY NAME
 * =========================
 */
```

## Categories (in order)

1. `SCALAR`     — EQUALS, NOT_EQUALS, GREATER_THAN, LESS_THAN, BETWEEN, …
2. `STRING`     — REGEX_MATCH, STARTS_WITH, ENDS_WITH
3. `DATE`       — DATE_BEFORE, DATE_AFTER, DATETIME_*, DATE_*_NOW, …
4. `DURATION`   — DURATION_*, DATE_*_DURATION
5. `STRUCTURAL` — PATH_EXISTS, PATH_NOT_EXISTS, ARRAY_SIZE_EQUALS
6. `ARRAY`      — ARRAY_CONTAINS, ALL_MATCH, CONTAINS_ANY, …
7. `OBJECT`     — OBJECT_CONTAINS_FIELDS, HAS_KEYS, FIELD_EQUALS_OTHER_FIELD
8. `LOGICAL`    — AND, OR, NOT

`AND`, `OR`, `NOT` are **not** registered in the operator map.
They are handled exclusively inside `AssertionEngine.evaluateAssertion()`.

------------------------------------------------------------------------

# 3️⃣ Operator Pattern (MANDATORY)

## Standard operator — implements `OperatorAssertion`

Use this for every operator that works on a single resolved JSON value.

```java
public class MyNewOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        // 1. Normalise the actual value (unwraps singleton List from JsonPath)
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        // 2. Harmonise numeric/string types before comparison
        Object[] normalized = AssertionUtils.normalizeTypes(normalizedActual, expected);

        // 3. Business check
        if (/* condition fails */) {
            throw new HarnessAssertionException(
                    AssertionOperator.MY_NEW_OPERATOR,
                    path,
                    expected,
                    actual,
                    "MY_NEW_OPERATOR failed at " + path +
                            ". Expected: " + expected +
                            ", Actual: " + actual);
        }
    }
}
```

## Multi-path operator — implements `DocumentContextAwareOperator`

Use this **only** when the operator must resolve more than one JsonPath
from the same response (e.g. `FIELD_EQUALS_OTHER_FIELD`).

```java
public class MyMultiPathOperator implements DocumentContextAwareOperator {

    @Override
    public void apply(DocumentContext context, Object expected) {
        Map<String, Object> config = AssertionUtils.toMap(expected);
        String pathA = String.valueOf(config.get("pathA"));
        String pathB = String.valueOf(config.get("pathB"));

        Object a = context.read(pathA);
        Object b = context.read(pathB);

        if (/* condition fails */) {
            throw new HarnessAssertionException(
                    AssertionOperator.MY_MULTI_PATH_OPERATOR,
                    pathA + " vs " + pathB,
                    b,
                    a,
                    "MY_MULTI_PATH_OPERATOR failed …");
        }
    }
}
```

`DocumentContextAwareOperator` already provides a default
`apply(String, Object, Object, boolean)` that throws
`UnsupportedOperationException`. Do **not** override it.

## Registration (MANDATORY)

Every new operator **must** be registered in `AssertionEngine` constructor:

```java
operators.put(AssertionOperator.MY_NEW_OPERATOR, new MyNewOperator());
```

Operators that implement `DocumentContextAwareOperator` are dispatched
automatically via `instanceof` check inside `AssertionEngine.evaluateAssertion()`.
No extra wiring is needed beyond registration.

------------------------------------------------------------------------

# 4️⃣ AssertionUtils — Shared Helpers (MANDATORY)

Always use `AssertionUtils` helpers. Do **not** duplicate this logic
inside operators.

| Helper                                         | When to use                                                       |
|------------------------------------------------|-------------------------------------------------------------------|
| `normalizeResult(actual, path)`                | Before scalar comparison — unwraps singleton List from JsonPath   |
| `normalizeTypes(actual, expected)`             | Before numeric/string comparison — harmonises Number/String types |
| `normalizeExpected(expected)`                  | Converts JSON string `"true"` / `"42"` / `"null"` to Java type   |
| `requireList(value, path)`                     | Validates that `actual` is a `List` (required for array operators)|
| `toMap(value)`                                 | Casts `Object` to `Map<String,Object>` (object / multi-path ops) |
| `objectContainsFields(actual, expected, ign)`  | Deep field-by-field comparison with optional null-ignore flag     |
| `deepEquals(actual, expected)`                 | Recursive equality with type normalisation                        |

------------------------------------------------------------------------

# 5️⃣ HarnessAssertionException (MANDATORY)

All assertion failures **must** throw `HarnessAssertionException`.
Never throw a raw `AssertionError` from inside an operator.

```java
throw new HarnessAssertionException(
        AssertionOperator.<OPERATOR>,   // the enum value
        path,                           // the JsonPath string
        expected,                       // expected value (raw, before normalisation)
        actual,                         // actual value (raw, before normalisation)
        "<OPERATOR_NAME> failed at " + path +
                ". Expected: " + expected +
                ", Actual: " + actual);
```

`HarnessAssertionException` extends `AssertionError` and formats a
structured message block automatically:

```
Assertion: EQUALS
Path:      $.status
Expected:  APPROVED
Actual:    PENDING

EQUALS failed at $.status. Expected: APPROVED, Actual: PENDING
```

------------------------------------------------------------------------

# 6️⃣ AssertionEngine — Evaluation Rules

## Logical operators (AND / OR / NOT)

Handled **before** the operator map lookup inside `evaluateAssertion()`.
They recurse back into `evaluateAssertion()` for each nested assertion.

- `AND` — all nested assertions must pass. Fails fast on the first failure.
- `OR`  — at least one nested assertion must pass. Throws
  `LOGICAL_OR_FAILED` with the last captured error if none pass.
- `NOT` — the single nested assertion must **fail**. Throws
  `LOGICAL_NOT_FAILED` if the nested assertion passes.

Logical operators require the `Assertions` list in the JSON.
`AND` / `OR` accept any number of children. `NOT` accepts exactly one.

## Path resolution errors

`JSON_PATH_EVALUATION_FAILED` — thrown when the JsonPath does not exist
in the response. Message includes: JsonPath, Expected Value, Operator,
Entry Point, Response, and Mutated Request.

`JSON_PATH_RUNTIME_ERROR` — thrown when JsonPath evaluation throws any
other exception. Includes the same fields plus the error message.

Never swallow these errors or fall back to null.

## Context-aware dispatch

Before resolving the JsonPath, `evaluateAssertion()` checks:

```java
if (handler instanceof DocumentContextAwareOperator contextAware) {
    contextAware.apply(context, assertion.value());
    return;
}
```

This means `DocumentContextAwareOperator` implementations never receive
a pre-resolved `actual` value.

------------------------------------------------------------------------

# 7️⃣ EntryPointExecutor SPI

Consumer projects implement this interface to plug in their rule engine.

```java
public interface EntryPointExecutor {
    String getEntryPointName();   // must match "EntryPointName" in suite JSON
    Class<?> getRequestType();    // Jackson deserialisation target type
    Object execute(Object request);
}
```

Rules:
- `getEntryPointName()` must exactly match the `EntryPointName` value
  used in test suite JSON files.
- `execute()` must return a serialisable object. The dispatcher serialises
  it with `ObjectMapper` using `NON_NULL` inclusion.
- Never return `null` from `execute()`.
- The consumer project registers all executors by constructing
  `EntryPointRegistry(List<EntryPointExecutor>)`.

------------------------------------------------------------------------

# 8️⃣ Error Message Format

All structured error messages must follow this format:

```
ERROR_CODE
-----------------------------------------
Key1: value
Key2: value

Human-readable explanation.

SectionLabel:
<content>
SectionLabel:
<content>
```

## Error codes in use

| Code                          | Thrown by              | Meaning                                     |
|-------------------------------|------------------------|---------------------------------------------|
| `JSON_PATH_EVALUATION_FAILED` | `AssertionEngine`      | JsonPath not found in response              |
| `JSON_PATH_RUNTIME_ERROR`     | `AssertionEngine`      | JsonPath evaluation threw an exception      |
| `MUTATION_FAILED`             | `JsonMutationEngine`   | JsonPath mutation could not be applied      |
| `LOGICAL_OR_FAILED`           | `AssertionEngine`      | All OR branches failed                      |
| `LOGICAL_NOT_FAILED`          | `AssertionEngine`      | NOT branch passed when it should have failed|
| `SYSTEM_FAILURE`              | Consumer adapter       | Unexpected runtime error in test execution  |

Do not invent new top-level error codes without updating this table.

------------------------------------------------------------------------

# 9️⃣ FailureLogger

`FailureLogger.logFailure(...)` writes a structured failure report to
`templates/report/<testFileName>__<testName>.json` on disk.

The report contains:
- `FAILURE TIME`
- `JSON FILE`
- `ENTRY POINT`
- `TestName`
- `TestDescription`
- `MUTATED REQUEST`
- `RESPONSE`
- `ERROR`

Rules:
- Always call `FailureLogger.logFailure(...)` in the `catch` block of
  the consumer adapter before re-throwing.
- The consumer adapter is responsible for passing `mutatedRequest` and
  `response` — **not** the original base request.
- `FailureLogger` is `synchronized` — safe for parallel test execution.

------------------------------------------------------------------------

# 🧪 Testing Guide (MANDATORY)

## Philosophy

- Every operator must have its own dedicated test class.
- Tests must cover: pass cases, fail cases, edge cases, null handling,
  and type coercion.
- Tests must not load a Spring context or touch the file system.
- Test the operator class directly — do not test via `AssertionEngine`
  for operator-level correctness.

## Operator test structure

```java
class MyNewOperatorTest {

    private MyNewOperator operator;

    @BeforeEach
    void setUp() {
        operator = new MyNewOperator();
    }

    /**
     * ```json
     * { "JsonPath": "$.field", "Operator": "MY_NEW_OPERATOR", "Value": "expected" }
     * ```
     */
    @Test
    @DisplayName("PASS: <scenario description>")
    void shouldPassWhen<Condition>() {
        assertThatNoException().isThrownBy(
                () -> operator.apply("$.field", actualValue, expectedValue, true));
    }

    @Test
    @DisplayName("FAIL: <scenario description>")
    void shouldFailWhen<Condition>() {
        assertThatThrownBy(
                () -> operator.apply("$.field", wrongValue, expectedValue, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("MY_NEW_OPERATOR failed");
    }
}
```

## Rules

1. Use `assertThatNoException().isThrownBy(...)` for pass cases.
2. Use `assertThatThrownBy(...).isInstanceOf(HarnessAssertionException.class)`
   for fail cases.
3. Prefix `@DisplayName` with `"PASS: "` or `"FAIL: "`.
4. Include a JSON snippet in the Javadoc of each test method showing the
   assertion configuration as it would appear in a real suite file.
5. Use `@BeforeEach` to construct the operator — never construct inline
   inside a test method.
6. For operators with multiple modes (e.g. `AllMatchOperator`), use
   JUnit 5 `@Nested` classes named after each mode:

```java
@Nested class EqualsMode     { … }
@Nested class GreaterThanMode{ … }
@Nested class EdgeCases      { … }
```

## Integration tests (AssertionEngine level)

Integration tests for `AssertionEngine` must:

- Construct `AssertionEngine` in `@BeforeEach`.
- Build `JsonAssertion` and `RuleTestCase` inline — no file I/O.
- Call `engine.assertResponse(response, testCase, mutatedRequest)`.
- For logical operator tests use these message fragments:
  - `"LOGICAL_OR_FAILED"`
  - `"LOGICAL_NOT_FAILED"`

------------------------------------------------------------------------

# 🚫 What Is Not Allowed

1. Business logic in records or model classes.
2. `static` mutable state in operator classes.
3. Throwing raw `AssertionError` from inside an operator — always use
   `HarnessAssertionException`.
4. Registering the same `AssertionOperator` value more than once in
   `AssertionEngine`.
5. Handling `AND` / `OR` / `NOT` inside an operator class — they belong
   exclusively in `AssertionEngine.evaluateAssertion()`.
6. Reading files or making network calls from inside an operator.
7. Introducing Spring, CDI, or any DI framework as a dependency.
8. Adding `@JsonProperty` annotations to operator classes.
9. Duplicating normalisation logic — always delegate to `AssertionUtils`.
10. Swallowing `PathNotFoundException` silently — always rethrow as a
    structured error with the full context (path, expected, operator,
    entry point, response, mutated request).

------------------------------------------------------------------------

# ✅ Checklist: Adding a New Operator

1. Add the new value to `AssertionOperator` enum in the correct category group.
2. Create `MyNewOperator.java` in `engine/operator/`.
   - Implement `OperatorAssertion` (single path) or
     `DocumentContextAwareOperator` (multi-path).
   - Use `AssertionUtils` helpers.
   - Throw `HarnessAssertionException` on failure.
3. Register in `AssertionEngine` constructor:
   `operators.put(AssertionOperator.MY_NEW_OPERATOR, new MyNewOperator());`
4. Create `MyNewOperatorTest.java` in the matching test package.
   - Cover: pass, fail, null, type coercion, and edge cases.
5. Run `mvn test` — all existing tests must still pass.

------------------------------------------------------------------------

# ✅ Checklist: Adding a New Entry Point (Consumer Side)

1. Implement `EntryPointExecutor` in the consumer project.
2. Register it in `EntryPointRegistry` by adding it to the constructor list.
3. Create a suite JSON file with `"EntryPointName"` matching
   `getEntryPointName()`.
4. Create a base request JSON template file.
5. Add test cases with `TestCaseParameterValues` (mutations) and
   `ResponseAssertions`.

------------------------------------------------------------------------

# 📊 Definition of Done

An operator is complete only when:

1. `AssertionOperator` enum value added.
2. Operator class implemented and registered in `AssertionEngine`.
3. Unit tests written and passing (PASS + FAIL + edge cases).
4. No existing tests broken.
5. No `AssertionUtils` logic duplicated inside the operator.
6. Error message produced by the operator contains operator name, path,
   expected value, and actual value.

