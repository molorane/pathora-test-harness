# Design Patterns & Principles in Pathora Test Harness

This document outlines the architectural patterns and design principles employed throughout the pathora-test-harness codebase.

---

## Design Patterns

### 1. Strategy Pattern

**Purpose**: Allow different assertion algorithms to be selected at runtime based on the operator type.

**Implementation**:
```
OperatorAssertion (interface)
└── Concrete Strategies:
    ├── EqualsOperator
    ├── GreaterThanOperator
    ├── ContainsAllOperator
    ├── DoesNotContainAllOperator
    ├── DoesNotContainAnyOperator
    ├── ValueInOperator
    └── ... 40+ other operator implementations
```

**Benefits**:
- New operators can be added without modifying assertion logic
- Each operator encapsulates its own validation logic
- Easy to test operators in isolation

**Example**:
```java
OperatorAssertion handler = operators.get(assertion.operator());
handler.apply(path, actual, expected, pathExists);
```

---

### 2. Factory + Registry Pattern

**Purpose**: Centralize operator instantiation and provide type-safe, O(1) lookup.

**Implementation**:
```java
public AssertionEngine() {
    operators = new EnumMap<>(AssertionOperator.class);
    operators.put(AssertionOperator.EQUALS, new EqualsOperator());
    operators.put(AssertionOperator.CONTAINS_ALL, new ContainsAllOperator());
    operators.put(AssertionOperator.DOES_NOT_CONTAIN_ALL, new DoesNotContainAllOperator());
    // ... 40+ more registrations
}
```

**Benefits**:
- `EnumMap` provides O(1) constant-time lookup
- Type-safe (compile-time checked enum keys)
- No if/switch chains scattered throughout code
- Single point of registration for new operators

**Contrast with alternatives**:
- ❌ `if(operator == EQUALS)` chains → Hard to maintain, O(n) lookup
- ❌ Reflection-based discovery → Runtime overhead, classpath scanning
- ✅ `EnumMap` → Fast, predictable, straightforward

---

### 3. Service Provider Interface (SPI) Pattern

**Purpose**: Enable third-party implementations of rule engines without modifying core harness code.

**Implementation**:
```java
public interface EntryPointExecutor {
    String getEntryPointName();
    String execute(String request);
}

// Discovered via:
ServiceLoader<EntryPointExecutor> loader = 
    ServiceLoader.load(EntryPointExecutor.class);
```

**Concrete Implementations**:
- `ApprovalRulesExecutor`
- `CollateralRulesExecutor`
- Custom implementations in external projects (via `META-INF/services/`)

**Benefits**:
- True plugin architecture
- Zero coupling to specific rule engines
- Test harness works with any rule engine that implements interface
- New rule engines integrate without rebuilding harness

---

### 4. Decorator / Wrapper Pattern

**Purpose**: Handle operators that need full JSON context differently from simple path-value comparisons.

**Implementation**:
```java
public interface DocumentContextAwareOperator extends OperatorAssertion {
    void apply(DocumentContext context, Object expected);
}

// Usage in engine:
if (handler instanceof DocumentContextAwareOperator) {
    DocumentContextAwareOperator contextAware = (DocumentContextAwareOperator) handler;
    contextAware.apply(context, assertion.value());
    return;
}
```

**Examples of Context-Aware Operators**:
- `AllMatchOperator` — needs to evaluate conditions on each array element
- `ArrayContainsObjectWithFieldsOperator` — needs to query nested objects

**Benefits**:
- Cleanly separates concerns:
  - Simple operators: receive resolved value
  - Complex operators: receive full document context
- No special cases in main evaluation loop
- Easy to add new context-aware operators

---

### 5. Template Method Pattern

**Purpose**: Define the skeleton of assertion evaluation; let subclasses customize specific steps.

**Implementation**:
```java
private void evaluateAssertion(
    JsonAssertion assertion,
    DocumentContext context,
    RuleTestCase testCase,
    String response,
    String mutatedRequest
) {
    // Template steps:
    
    // Step 1: Handle logical composition operators first
    if (assertion.operator() == AssertionOperator.AND) { ... }
    if (assertion.operator() == AssertionOperator.OR) { ... }
    if (assertion.operator() == AssertionOperator.NOT) { ... }
    
    // Step 2: Delegate to context-aware handler or path evaluation
    OperatorAssertion handler = operators.get(assertion.operator());
    if (handler instanceof DocumentContextAwareOperator) {
        ((DocumentContextAwareOperator) handler).apply(context, assertion.value());
        return;
    }
    
    // Step 3: Resolve path and apply operator
    Object actual = context.read(assertion.jsonPath());
    applyAssertion(assertion, actual, pathExists);
}
```

**Benefits**:
- Consistent error handling across all operators
- Uniform logging and failure reporting
- New operators follow the same contract automatically

---

### 6. Composite Pattern

**Purpose**: Allow assertions to be composed into tree structures (AND / OR / NOT hierarchies).

**Implementation**:
```java
public class JsonAssertion {
    private AssertionOperator operator;
    private String jsonPath;
    private Object value;
    private List<JsonAssertion> assertions;  // ← Enables composition
}

// Usage:
/*
{
  "Operator": "AND",
  "Assertions": [
    { "JsonPath": "$.status", "Operator": "EQUALS", "Value": "APPROVED" },
    { "JsonPath": "$.amount", "Operator": "GREATER_THAN", "Value": 1000 }
  ]
}
*/
```

**Tree Structure**:
```
AND
├── EQUALS ($.status == "APPROVED")
├── OR
│   ├── GREATER_THAN ($.amount > 1000)
│   └── LESS_THAN ($.amount < 100)
└── NOT
    └── CONTAINS_ALL ($.items does NOT contain all ["X", "Y"])
```

**Benefits**:
- Recursive evaluation handles arbitrary nesting depth
- Expressive assertion composition
- Client treats composite and leaf assertions uniformly

---

## Architectural Principles

### SOLID Principles

#### **S — Single Responsibility Principle (SRP)**

Each class has one reason to change:

| Class | Responsibility |
|-------|-----------------|
| `EqualsOperator` | Equality comparison only |
| `ContainsAllOperator` | Check if array contains all values |
| `AssertionEngine` | Orchestrate evaluation pipeline |
| `FailureLogger` | Format and report failures |
| `ResponseAssertionExecutor` | Expose public API boundary |

**Violation Example** ❌:
```java
// BAD: Operator + Logging + Error Handling all mixed
class EqualsOperator {
    void apply(...) {
        validate(...);
        logDebug(...);
        reportMetrics(...);  // ← Too many responsibilities
        assertEquals(...);
    }
}
```

**Correct Example** ✅:
```java
class EqualsOperator implements OperatorAssertion {
    void apply(String path, Object actual, Object expected, boolean pathExists) {
        if (!Objects.equals(actual, expected)) {
            throw new HarnessAssertionException(...);
        }
    }
}
```

---

#### **O — Open/Closed Principle (OCP)**

Open for extension, closed for modification.

**Adding a new operator requires NO changes to existing code**:

1. Add enum entry to `AssertionOperator`:
   ```java
   DOES_NOT_CONTAIN_ALL,  // ← Just add here
   ```

2. Create operator class:
   ```java
   public class DoesNotContainAllOperator implements OperatorAssertion {
       public void apply(String path, Object actual, Object expected, boolean pathExists) {
           // Implementation
       }
   }
   ```

3. Register in `AssertionEngine`:
   ```java
   operators.put(AssertionOperator.DOES_NOT_CONTAIN_ALL, 
                 new DoesNotContainAllOperator());
   ```

**Result**: All existing operators remain untouched. ✅

---

#### **L — Liskov Substitution Principle (LSP)**

Subtypes must be substitutable for their base type.

**Correct**:
```java
OperatorAssertion handler = operators.get(assertion.operator());
handler.apply(path, actual, expected, pathExists);  // Works for ANY operator
```

All operators honor the `OperatorAssertion` contract:
- Same method signature
- Same exception semantics (throw `HarnessAssertionException` on failure)
- Same behavioral expectations

**This is WHY the engine can use `operators.get()` without knowing the concrete type.**

---

#### **I — Interface Segregation Principle (ISP)**

Clients should not depend on interfaces they don't use.

**Split interfaces by capability**:

```java
// Minimal interface for simple operators
public interface OperatorAssertion {
    void apply(String path, Object actual, Object expected, boolean pathExists);
}

// Extended interface only for operators needing context
public interface DocumentContextAwareOperator extends OperatorAssertion {
    void apply(DocumentContext context, Object expected);
}

// Third interface for rule engine entry points
public interface EntryPointExecutor {
    String getEntryPointName();
    String execute(String request);
}
```

**Benefit**: No bloated interfaces. Each client depends only on what it needs.

---

#### **D — Dependency Inversion Principle (DIP)**

High-level modules should not depend on low-level modules. Both should depend on abstractions.

**Correct** ✅:
```
AssertionEngine (high-level)
        ↓ (depends on abstraction)
    OperatorAssertion (interface)
        ↑ (implements)
EqualsOperator, ContainsAllOperator, ... (low-level)
```

**NOT this** ❌:
```
AssertionEngine (high-level)
        ↓ (depends directly on concrete)
EqualsOperator, ContainsAllOperator, ... (low-level)
// ← Problem: Adding new operator requires changing engine
```

---

### Additional Architectural Principles

#### **Composition Over Inheritance**

Operators use **composition**, not deep inheritance hierarchies:

```
OperatorAssertion (interface)
├── EqualsOperator (implements)
├── ContainsAllOperator (implements)
└── DocumentContextAwareOperator (extends interface)
    ├── AllMatchOperator (implements)
    └── ArrayContainsObjectWithFieldsOperator (implements)
```

**No deep hierarchies** like:
```
❌ OperatorAssertion
   └── AbstractArrayOperator
       └── AbstractCollectionOperator
           ├── ContainsAllOperator
           └── ContainsAnyOperator
```

---

#### **Type Safety**

Use strong typing throughout:

```java
// Type-safe registry
EnumMap<AssertionOperator, OperatorAssertion> operators;

// Fail-fast type validation
List<?> expectedList = AssertionUtils.requireList(expected, path);

// Strong enum for operators (not strings)
public enum AssertionOperator { ... }
```

**Benefits**:
- Compile-time checking
- IDE refactoring support
- Zero runtime reflection for lookups

---

#### **Immutability**

Model classes are immutable:

```java
public class JsonAssertion {
    private final AssertionOperator operator;
    private final String jsonPath;
    private final Object value;
    private final List<JsonAssertion> assertions;
    
    // No setters
    // All fields final
}
```

**Benefits**:
- Thread-safe by construction
- Predictable behavior
- Easy to share across threads
- Better for caching

---

#### **Fail-Fast with Rich Context**

Errors include maximum diagnostic information:

```java
throw new AssertionError(
    String.format(
        "JSON_PATH_EVALUATION_FAILED\n" +
        "-----------------------------------------\n" +
        "JsonPath: %s\n" +
        "Expected Value: %s\n" +
        "Operator: %s\n" +
        "Entry Point: %s\n" +
        "\n" +
        "Path does not exist in response.\n" +
        "\n" +
        "Response:\n" +
        "%s\n\n" +
        "Mutated Request:\n" +
        "%s\n",
        assertion.jsonPath(),
        assertion.value(),
        assertion.operator(),
        testCase.entryPointName(),
        response,
        mutatedRequest
    ), e
);
```

**Benefits**:
- Failures caught immediately (not deferred)
- Full context helps debugging
- No silent failures
- Error pinpoints exact path/operator/value

---

#### **Constructor Injection Only (No Framework)**

No Spring, no magic autowiring:

```java
public class AssertionEngine {
    private final Map<AssertionOperator, OperatorAssertion> operators;
    
    public AssertionEngine() {  // ← Explicit construction
        operators = new EnumMap<>(AssertionOperator.class);
        operators.put(AssertionOperator.EQUALS, new EqualsOperator());
        // ... all dependencies created here
    }
}
```

**Benefits**:
- Lightweight — no framework container overhead
- Explicit — easy to see what's being created
- Testable — just call `new AssertionEngine()`
- Debuggable — direct stack traces, no proxy layers

---

#### **Separation of Concerns**

Each layer has distinct responsibility:

```
┌────────────────────────────────────────────┐
│ ResponseAssertionExecutor (Public API)     │ Entry point
├────────────────────────────────────────────┤
│ AssertionEngine (Orchestration)            │ Composition, delegation
├────────────────────────────────────────────┤
│ OperatorAssertion (Strategy)               │ Individual operators
├────────────────────────────────────────────┤
│ AssertionUtils (Helpers)                   │ Type validation, normalization
├────────────────────────────────────────────┤
│ HarnessAssertionException (Error Handling) │ Structured failures
├────────────────────────────────────────────┤
│ FailureLogger (Reporting)                  │ Test output formatting
├────────────────────────────────────────────┤
│ EntryPointExecutor (SPI)                   │ Pluggable rule engines
└────────────────────────────────────────────┘
```

---

## Summary

| Pattern/Principle | Purpose | Key Benefit |
|---|---|---|
| **Strategy** | Swap assertion algorithms | Operators independent |
| **Factory + Registry** | Centralize operator lookup | O(1) type-safe retrieval |
| **SPI** | Plugin rule engines | Third-party extensibility |
| **Decorator** | Handle context-aware operators | Cleanly separate concerns |
| **Template Method** | Define evaluation skeleton | Consistent error handling |
| **Composite** | Tree-structured assertions | Expressive nesting (AND/OR/NOT) |
| **SRP** | One reason to change per class | Clear responsibilities |
| **OCP** | Extend without modification | New operators don't touch existing code |
| **LSP** | Substitute implementations | Engine works with any operator |
| **ISP** | Minimal interfaces | Clients depend only on what they need |
| **DIP** | Depend on abstractions | High/low-level modules decoupled |
| **Composition** | Favor composition | Flat, clear relationships |
| **Type Safety** | Strong typing everywhere | Compile-time guarantees |
| **Immutability** | Unchangeable models | Thread-safe by design |
| **Fail-Fast** | Error immediately with context | Easy debugging |
| **Constructor Injection** | Explicit dependencies | No framework overhead |
| **Separation of Concerns** | Each layer has one job | Maintainable, testable |

---

## Implications for New Development

When adding features to pathora-test-harness, follow these patterns:

✅ **DO**:
- Implement `OperatorAssertion` for new comparison logic
- Use `EnumMap` for registries
- Fail fast with rich error context
- Write immutable model classes
- Keep classes small, focused, testable

❌ **DON'T**:
- Modify existing operators to add new logic
- Use string-based operator dispatch
- Swallow exceptions silently
- Create deep inheritance hierarchies
- Add framework dependencies (Spring, etc.)
- Make model classes mutable

---

## References

- **Design Patterns**: [Gang of Four Patterns](https://en.wikipedia.org/wiki/Design_Patterns)
- **SOLID Principles**: [Uncle Bob's SOLID](https://en.wikipedia.org/wiki/SOLID)
- **Java SPI**: [ServiceLoader](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html)
- **Composition vs. Inheritance**: [Effective Java, Item 16](https://www.oreilly.com/library/view/effective-java-3rd/9780134685991/)

