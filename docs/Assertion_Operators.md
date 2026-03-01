# Assertion Operators – Comprehensive Technical Documentation

This document provides a complete explanation of the `AssertionOperator` model used in the JSON Test Harness.

Each operator section includes:
- **Purpose**
- **Assertion Example**
- **PASS Example**
- **FAIL Example**
- **Edge Cases & Important Notes**

---

## Scalar Operators

### EQUALS

**Purpose:** Validates strict equality between extracted value and expected value.

**Assertion:**
```json
{ "JsonPath": "$.outputData.status", "Operator": "EQUALS", "Value": "APPROVED" }
```

**PASS** — actual response:
```json
{ "outputData": { "status": "APPROVED" } }
```

**FAIL** — actual response:
```json
{ "outputData": { "status": "PENDING" } }
```

> **Notes:** Performs strict comparison. Types must match. If JsonPath returns a list instead of a scalar, the assertion fails.

---

### NOT_EQUALS

**Purpose:** Validates that the extracted value is NOT equal to the expected value.

**Assertion:**
```json
{ "JsonPath": "$.outputData.status", "Operator": "NOT_EQUALS", "Value": "DECLINED" }
```

**PASS** — actual response:
```json
{ "outputData": { "status": "APPROVED" } }
```

**FAIL** — actual response:
```json
{ "outputData": { "status": "DECLINED" } }
```

> **Notes:** Used for negative validation. Strict comparison is still applied.

---

### GREATER_THAN

**Purpose:** Validates that the extracted numeric value is greater than the expected value.

**Assertion:**
```json
{ "JsonPath": "$.outputData.score", "Operator": "GREATER_THAN", "Value": 50 }
```

**PASS** — actual response:
```json
{ "outputData": { "score": 75 } }
```

**FAIL** — actual response:
```json
{ "outputData": { "score": 45 } }
```

> **Notes:** Only valid for numeric values. Type mismatch results in failure.

---

### LESS_THAN

**Purpose:** Validates that the extracted numeric value is less than the expected value.

**Assertion:**
```json
{ "JsonPath": "$.outputData.score", "Operator": "LESS_THAN", "Value": 100 }
```

**PASS** — actual response:
```json
{ "outputData": { "score": 80 } }
```

**FAIL** — actual response:
```json
{ "outputData": { "score": 120 } }
```

> **Notes:** Boundary condition enforcement. Numeric types required.

---

### BETWEEN

**Purpose:** Validates that the extracted numeric value falls within a specified range (inclusive on both ends).

**Assertion:**
```json
{
  "JsonPath": "$.outputData.riskScore",
  "Operator": "BETWEEN",
  "Value": { "min": 50, "max": 100 }
}
```

**PASS** — actual response:
```json
{ "outputData": { "riskScore": 75 } }
```

**FAIL** — actual response:
```json
{ "outputData": { "riskScore": 30 } }
```

> **Notes:** Value must be a JSON object with `min` and `max` keys. Both boundaries are inclusive. Numeric types required.

---

### REGEX_MATCH

**Purpose:** Validates that the extracted string value matches the given regular expression pattern (full match).

**Assertion:**
```json
{
  "JsonPath": "$.outputData.referenceId",
  "Operator": "REGEX_MATCH",
  "Value": "^REF-\\d{4}-\\d{5}$",
  "Description": "Reference ID must follow format REF-XXXX-XXXXX"
}
```

**PASS** — actual response:
```json
{ "outputData": { "referenceId": "REF-1234-56789" } }
```

**FAIL** — actual response:
```json
{ "outputData": { "referenceId": "INVALID-ID" } }
```

> **Notes:** Uses full match (`matches()`), not partial find. Value must be a valid regex string. Case-sensitive by default. The `Description` field is optional but recommended for regex patterns to explain intent.

---

## Structural Operators

### EXISTS

**Purpose:** Validates that the JsonPath resolves to at least one value.

**Assertion:**
```json
{ "JsonPath": "$.outputData.referenceId", "Operator": "EXISTS" }
```

**PASS** — actual response:
```json
{ "outputData": { "referenceId": "ABC123" } }
```

**FAIL** — actual response:
```json
{ "outputData": { } }
```

> **Notes:** Does not validate value content. Only checks presence. No `Value` field required.

---

### ARRAY_SIZE_EQUALS

**Purpose:** Validates that the extracted array has the specified length.

**Assertion:**
```json
{ "JsonPath": "$.outputData.reasonCodes", "Operator": "ARRAY_SIZE_EQUALS", "Value": 2 }
```

**PASS** — actual response:
```json
{ "outputData": { "reasonCodes": ["1004", "1011"] } }
```

**FAIL** — actual response:
```json
{ "outputData": { "reasonCodes": ["1004"] } }
```

> **Notes:** Enforces cardinality. Prevents hidden extra elements. A null or missing array is treated as size 0.

---

## Array Operators

### ARRAY_CONTAINS

**Purpose:** Validates that an array contains the specified value.

**Assertion:**
```json
{ "JsonPath": "$.outputData.reasonCodes", "Operator": "ARRAY_CONTAINS", "Value": "1004" }
```

**PASS** — actual response:
```json
{ "outputData": { "reasonCodes": ["1004", "1011"] } }
```

**FAIL** — actual response:
```json
{ "outputData": { "reasonCodes": ["1011"] } }
```

> **Notes:** JsonPath must resolve to an array. If a scalar is returned, the assertion fails.

---

### ARRAY_CONTAINS_ONLY_VALUES

**Purpose:** Validates that the array contains exactly the specified values, in any order. The array must have the same size as the expected list, and contain all expected elements.

**Assertion:**
```json
{ "JsonPath": "$.outputData.tags", "Operator": "ARRAY_CONTAINS_ONLY_VALUES", "Value": ["B", "A"] }
```

**PASS** — actual response:
```json
{ "outputData": { "tags": ["A", "B"] } }
```

**FAIL** — actual response (extra element):
```json
{ "outputData": { "tags": ["A", "B", "C"] } }
```

**FAIL** — actual response (missing element):
```json
{ "outputData": { "tags": ["A"] } }
```

> **Notes:** Both size and content must match. Order is ignored. Useful when you need to assert the complete set of values without caring about ordering.

---

### ARRAY_CONTAINS_ONLY_ONE_VALUE

**Purpose:** Validates that the array contains exactly one element, and that element equals the expected value.

**Assertion:**
```json
{ "JsonPath": "$.outputData.results", "Operator": "ARRAY_CONTAINS_ONLY_ONE_VALUE", "Value": "SUCCESS" }
```

**PASS** — actual response:
```json
{ "outputData": { "results": ["SUCCESS"] } }
```

**FAIL** — actual response (too many elements):
```json
{ "outputData": { "results": ["SUCCESS", "PENDING"] } }
```

**FAIL** — actual response (wrong value):
```json
{ "outputData": { "results": ["FAILED"] } }
```

> **Notes:** Enforces both cardinality (exactly 1) and value equality. Fails if the array has zero or more than one element, or if the single element doesn't match.

---

### ARRAY_CONTAINS_OBJECT_WITH_FIELDS

**Purpose:** Validates that at least one object in the array matches the provided partial structure (subset of fields).

**Assertion:**
```json
{
  "JsonPath": "$.outputData.dataItemNeeds",
  "Operator": "ARRAY_CONTAINS_OBJECT_WITH_FIELDS",
  "Value": { "type": "1035" }
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "dataItemNeeds": [
      { "type": "1035", "status": "ACTIVE" }
    ]
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "dataItemNeeds": [
      { "type": "1040" }
    ]
  }
}
```

> **Notes:** Performs structural subset match against each array element. Fails if no matching element is found. Extra fields on the actual object are allowed.

---

### ALL_MATCH

**Purpose:** Validates that **every** element in the array matches a condition. The condition defaults to equality when a scalar value is provided.

**Equals mode** — scalar Value (defaults to equality):
```json
{ "JsonPath": "$.outputData.penalties", "Operator": "ALL_MATCH", "Value": 0 }
```

**PASS** — actual response:
```json
{ "outputData": { "penalties": [0, 0, 0] } }
```

**FAIL** — actual response:
```json
{ "outputData": { "penalties": [0, 0, 1] } }
```

**greaterThan mode:**
```json
{ "JsonPath": "$.outputData.scores", "Operator": "ALL_MATCH", "Value": { "greaterThan": 50 } }
```

**PASS:** `{ "scores": [60, 70, 80] }` — **FAIL:** `{ "scores": [60, 50, 80] }`

**lessThan mode:**
```json
{ "JsonPath": "$.outputData.scores", "Operator": "ALL_MATCH", "Value": { "lessThan": 50 } }
```

**PASS:** `{ "scores": [10, 20, 30] }` — **FAIL:** `{ "scores": [10, 50, 30] }`

**between mode:**
```json
{ "JsonPath": "$.outputData.scores", "Operator": "ALL_MATCH", "Value": { "between": { "min": 50, "max": 100 } } }
```

**PASS:** `{ "scores": [50, 75, 100] }` — **FAIL:** `{ "scores": [50, 110, 75] }`

> **Notes:** Scalar Value → equality check. Object Value must have one of: `greaterThan`, `lessThan`, or `between`. Empty arrays pass vacuously. Error messages include the failing element's index.

---

## Object Operators

### OBJECT_CONTAINS_FIELDS

**Purpose:** Validates that the extracted object contains the specified key-value pairs. Extra fields on the actual object are allowed.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.client",
  "Operator": "OBJECT_CONTAINS_FIELDS",
  "Value": { "clientType": "1031", "riskLevel": "HIGH" }
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "client": {
      "clientType": "1031",
      "riskLevel": "HIGH",
      "segment": "Retail"
    }
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "client": {
      "clientType": "1031",
      "riskLevel": "LOW"
    }
  }
}
```

> **Notes:** All specified fields must exist and match exactly. Extra fields in the actual object are ignored.

---

### OBJECT_CONTAINS_FIELDS_IGNORE_NULLS

**Purpose:** Same as `OBJECT_CONTAINS_FIELDS` but ignores null values in the expected object.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.client",
  "Operator": "OBJECT_CONTAINS_FIELDS_IGNORE_NULLS",
  "Value": { "clientType": "1031", "middleName": null }
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "client": {
      "clientType": "1031"
    }
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "client": {
      "clientType": "1032"
    }
  }
}
```

> **Notes:** Null fields in the expected object are skipped during comparison. Non-null fields must match exactly.

---

## Design Philosophy & Architecture

The `AssertionOperator` model separates JsonPath navigation from semantic validation. JsonPath extracts data; operators enforce business intent. This prevents silent coercion, enforces cardinality, and improves readability.
