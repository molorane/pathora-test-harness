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
{
  "path": "$.outputData.status",
  "operator": "EQUALS",
  "value": "APPROVED"
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "status": "APPROVED"
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "status": "PENDING"
  }
}
```

> **Notes:** Performs strict comparison. Types must match. If JSONPath returns a list instead of a scalar, the assertion fails.

---

### NOT_EQUALS

**Purpose:** Validates that the extracted value is NOT equal to the expected value.

**Assertion:**
```json
{
  "path": "$.outputData.status",
  "operator": "NOT_EQUALS",
  "value": "DECLINED"
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "status": "APPROVED"
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "status": "DECLINED"
  }
}
```

> **Notes:** Used for negative validation. Strict comparison is still applied.

---

### GREATER_THAN

**Purpose:** Validates that the extracted numeric value is greater than the expected value.

**Assertion:**
```json
{
  "path": "$.outputData.score",
  "operator": "GREATER_THAN",
  "value": 50
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "score": 75
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "score": 45
  }
}
```

> **Notes:** Only valid for numeric values. Type mismatch results in failure.

---

### LESS_THAN

**Purpose:** Validates that the extracted numeric value is less than the expected value.

**Assertion:**
```json
{
  "path": "$.outputData.score",
  "operator": "LESS_THAN",
  "value": 100
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "score": 80
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "score": 120
  }
}
```

> **Notes:** Boundary condition enforcement. Numeric types required.

---

### BETWEEN

**Purpose:** Validates that the extracted numeric value falls within a specified range (inclusive on both ends).

**Assertion:**
```json
{
  "path": "$.outputData.riskScore",
  "operator": "BETWEEN",
  "value": {
    "min": 50,
    "max": 100
  }
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "riskScore": 75
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "riskScore": 30
  }
}
```

> **Notes:** value must be a JSON object with `min` and `max` keys. Both boundaries are inclusive. Numeric types required.

---


### GREATER_THAN_OR_EQUALS

**Purpose:** Validates that the extracted numeric value is greater than or equal to the expected value.

**Assertion:**
```json
{
  "path": "$.outputData.score",
  "operator": "GREATER_THAN_OR_EQUALS",
  "value": 50
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "score": 50
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "score": 49
  }
}
```

> **Notes:** Numeric types required. Inclusive boundary.

---

### LESS_THAN_OR_EQUALS

**Purpose:** Validates that the extracted numeric value is less than or equal to the expected value.

**Assertion:**
```json
{
  "path": "$.outputData.score",
  "operator": "LESS_THAN_OR_EQUALS",
  "value": 100
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "score": 100
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "score": 101
  }
}
```

> **Notes:** Numeric types required. Inclusive boundary.

---

### REGEX_MATCH

**Purpose:** Validates that the extracted string value matches the given regular expression pattern (full match).

**Assertion:**
```json
{
  "path": "$.outputData.referenceId",
  "operator": "REGEX_MATCH",
  "value": "^REF-\\d{4}-\\d{5}$",
  "Description": "Reference ID must follow format REF-XXXX-XXXXX"
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "referenceId": "REF-1234-56789"
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "referenceId": "INVALID-ID"
  }
}
```

> **Notes:** Uses full match (`matches()`), not partial find. value must be a valid regex string. Case-sensitive by default. The `Description` field is optional but recommended for regex patterns to explain intent.

---


## String Operators

### STARTS_WITH

**Purpose:** Validates that the extracted string starts with the specified prefix.

**Assertion:**
```json
{
  "path": "$.outputData.reference",
  "operator": "STARTS_WITH",
  "value": "REF-"
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "reference": "REF-12345"
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "reference": "ID-12345"
  }
}
```

> **Notes:** Case-sensitive matching.

---

### ENDS_WITH

**Purpose:** Validates that the extracted string ends with the specified suffix.

**Assertion:**
```json
{
  "path": "$.outputData.reference",
  "operator": "ENDS_WITH",
  "value": "-Z"
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "reference": "12345-Z"
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "reference": "12345-A"
  }
}
```

> **Notes:** Case-sensitive matching.

---


## Date Operators

### DATE_BEFORE

**Purpose:** Validates that the extracted date is strictly before the expected date.

**Assertion:**
```json
{
  "path": "$.outputData.issueDate",
  "operator": "DATE_BEFORE",
  "value": "2025-01-01"
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "issueDate": "2024-12-31"
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "issueDate": "2025-01-01"
  }
}
```

> **Notes:** Requires ISO-8601 date strings.

---

### DATE_AFTER

**Purpose:** Validates that the extracted date is strictly after the expected date.

**Assertion:**
```json
{
  "path": "$.outputData.expiryDate",
  "operator": "DATE_AFTER",
  "value": "2025-01-01"
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "expiryDate": "2025-01-02"
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "expiryDate": "2024-12-31"
  }
}
```

> **Notes:** Requires ISO-8601 date strings.

---

### DATETIME_BEFORE

**Purpose:** Validates that the extracted datetime is strictly before the expected datetime.

**Assertion:**
```json
{
  "path": "$.outputData.timestamp",
  "operator": "DATETIME_BEFORE",
  "value": "2025-01-01T12:00:00Z"
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "timestamp": "2025-01-01T10:00:00Z"
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "timestamp": "2025-01-01T14:00:00Z"
  }
}
```

> **Notes:** Requires ISO-8601 datetime strings.

---

### DATETIME_AFTER

**Purpose:** Validates that the extracted datetime is strictly after the expected datetime.

**Assertion:**
```json
{
  "path": "$.outputData.timestamp",
  "operator": "DATETIME_AFTER",
  "value": "2025-01-01T12:00:00Z"
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "timestamp": "2025-01-01T14:00:00Z"
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "timestamp": "2025-01-01T10:00:00Z"
  }
}
```

> **Notes:** Requires ISO-8601 datetime strings.

---

### IS_PAST_DATE

**Purpose:** Validates that the extracted date/datetime is before the current system time.

**Assertion:**
```json
{
  "path": "$.outputData.timestamp",
  "operator": "IS_PAST_DATE"
}
```

**PASS** — actual response (assuming today is `2025-01-01`):
```json
{
  "outputData": {
    "timestamp": "2024-12-31T00:00:00Z"
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "timestamp": "2030-01-01T00:00:00Z"
  }
}
```

> **Notes:** No `value` required.

---

### IS_FUTURE_DATE

**Purpose:** Validates that the extracted date/datetime is after the current system time.

**Assertion:**
```json
{
  "path": "$.outputData.expiryDate",
  "operator": "IS_FUTURE_DATE"
}
```

**PASS** — actual response (assuming today is `2025-01-01`):
```json
{
  "outputData": {
    "expiryDate": "2030-01-01"
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "expiryDate": "2020-01-01"
  }
}
```

> **Notes:** No `value` required.

---

### DATE_WITHIN_LAST

**Purpose:** Validates that the extracted date falls within the last specified duration (e.g., last 30 days).

**Assertion:**
```json
{
  "path": "$.outputData.timestamp",
  "operator": "DATE_WITHIN_LAST",
  "value": "P30D"
}
```

**PASS** — actual response (assuming today is `2025-01-31`):
```json
{
  "outputData": {
    "timestamp": "2025-01-15T00:00:00Z"
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "timestamp": "2024-01-01T00:00:00Z"
  }
}
```

> **Notes:** `value` must be an ISO-8601 duration (e.g., `P30D` for 30 days).

---

### DATE_WITHIN_NEXT

**Purpose:** Validates that the extracted date falls within the next specified duration.

**Assertion:**
```json
{
  "path": "$.outputData.expiryDate",
  "operator": "DATE_WITHIN_NEXT",
  "value": "P30D"
}
```

**PASS** — actual response (assuming today is `2025-01-01`):
```json
{
  "outputData": {
    "expiryDate": "2025-01-15T00:00:00Z"
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "expiryDate": "2026-01-01T00:00:00Z"
  }
}
```

> **Notes:** `value` must be an ISO-8601 duration.

---


## Duration Operators

### DURATION_BETWEEN

**Purpose:** Validates that the duration between two dates is within a specific range.

**Assertion:**
```json
{
  "path": "$.outputData.times",
  "operator": "DURATION_BETWEEN",
  "value": {
    "min": "P1D",
    "max": "P5D"
  }
}
```

> **Notes:** Extracts dates and asserts the duration between them falls within min/max durations.

---

### DURATION_EQUALS

**Purpose:** Validates that the extracted duration equals the expected duration.

**Assertion:**
```json
{
  "path": "$.outputData.processingTime",
  "operator": "DURATION_EQUALS",
  "value": "PT5M"
}
```

> **Notes:** Requires ISO-8601 duration string.

---

### DURATION_GREATER_THAN

**Purpose:** Validates that the extracted duration is greater than the expected duration.

**Assertion:**
```json
{
  "path": "$.outputData.processingTime",
  "operator": "DURATION_GREATER_THAN",
  "value": "PT1M"
}
```

---

### DURATION_LESS_THAN

**Purpose:** Validates that the extracted duration is less than the expected duration.

**Assertion:**
```json
{
  "path": "$.outputData.processingTime",
  "operator": "DURATION_LESS_THAN",
  "value": "PT1H"
}
```

---

### DATE_AFTER_DURATION

**Purpose:** Validates that the given date is after exactly a specified duration from another reference date.

**Assertion:**
```json
{
  "path": "$.outputData.completionDate",
  "operator": "DATE_AFTER_DURATION",
  "value": "P1D"
}
```

---

### DATE_BEFORE_DURATION

**Purpose:** Validates that the given date is before a specified duration from a reference date.

**Assertion:**
```json
{
  "path": "$.outputData.completionDate",
  "operator": "DATE_BEFORE_DURATION",
  "value": "P1D"
}
```

---

## Structural Operators

### PATH_EXISTS

**Purpose:** Validates that the JSONPath resolves to at least one value.

**Assertion:**
```json
{
  "path": "$.outputData.referenceId",
  "operator": "PATH_EXISTS"
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "referenceId": "ABC123"
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {}
}
```

> **Notes:** Does not validate value content. Only checks presence. No `value` field required.

---

### LIST_SIZE_EQUALS

**Purpose:** Validates that the extracted list has the specified length.

**Assertion:**
```json
{
  "path": "$.outputData.reasonCodes",
  "operator": "LIST_SIZE_EQUALS",
  "value": 2
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "reasonCodes": ["1004", "1011"]
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "reasonCodes": ["1004"]
  }
}
```

> **Notes:** Enforces cardinality. Prevents hidden extra elements. A null or missing list is treated as size 0.

---

## List Operators


### CONTAINS_ANY

**Purpose:** Validates that the extracted list contains at least one of the expected values.

**Assertion:**
```json
{
  "path": "$.outputData.statusTags",
  "operator": "CONTAINS_ANY",
  "value": ["PENDING", "APPROVED"]
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "statusTags": ["APPROVED", "VERIFIED"]
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "statusTags": ["DECLINED"]
  }
}
```

> **Notes:** Fails if the list lacks all the provided values.

---

### CONTAINS_ALL

**Purpose:** Validates that the extracted list contains all the expected values (order independent).

**Assertion:**
```json
{
  "path": "$.outputData.roles",
  "operator": "CONTAINS_ALL",
  "value": ["ADMIN", "USER"]
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "roles": ["ADMIN", "USER", "GUEST"]
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "roles": ["ADMIN"]
  }
}
```

> **Notes:** Can contain other elements in addition to the required ones.

---

### IS_EMPTY_LIST

**Purpose:** Validates that the extracted list is empty.

**Assertion:**
```json
{
  "path": "$.outputData.errors",
  "operator": "IS_EMPTY_LIST"
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "errors": []
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "errors": ["Timeout"]
  }
}
```

> **Notes:** No `value` required. Null lists are treated according to the evaluator implementation.

---

### UNIQUE_ELEMENTS

**Purpose:** Validates that all elements in the extracted list are unique.

**Assertion:**
```json
{
  "path": "$.outputData.ids",
  "operator": "UNIQUE_ELEMENTS"
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "ids": [1, 2, 3]
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "ids": [1, 2, 1]
  }
}
```

> **Notes:** No `value` required.

---

### LIST_CONTAINS

**Purpose:** Validates that a list contains the specified value.

**Assertion:**
```json
{
  "path": "$.outputData.reasonCodes",
  "operator": "LIST_CONTAINS",
  "value": "1004"
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "reasonCodes": ["1004", "1011"]
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "reasonCodes": ["1011"]
  }
}
```

> **Notes:** JSONPath must resolve to a list. If a scalar is returned, the assertion fails.

---

### LIST_CONTAINS_ONLY_VALUES

**Purpose:** Validates that the list contains exactly the specified values, in any order. The list must have the same size as the expected list, and contain all expected elements.

**Assertion:**
```json
{
  "path": "$.outputData.tags",
  "operator": "LIST_CONTAINS_ONLY_VALUES",
  "value": ["B", "A"]
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "tags": ["A", "B"]
  }
}
```

**FAIL** — actual response (extra element):
```json
{
  "outputData": {
    "tags": ["A", "B", "C"]
  }
}
```

**FAIL** — actual response (missing element):
```json
{
  "outputData": {
    "tags": ["A"]
  }
}
```

> **Notes:** Both size and content must match. Order is ignored. Useful when you need to assert the complete set of values without caring about ordering.

---

### LIST_CONTAINS_ONLY_ONE_VALUE

**Purpose:** Validates that the list contains exactly one element, and that element equals the expected value.

**Assertion:**
```json
{
  "path": "$.outputData.results",
  "operator": "LIST_CONTAINS_ONLY_ONE_VALUE",
  "value": "SUCCESS"
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "results": ["SUCCESS"]
  }
}
```

**FAIL** — actual response (too many elements):
```json
{
  "outputData": {
    "results": ["SUCCESS", "PENDING"]
  }
}
```

**FAIL** — actual response (wrong value):
```json
{
  "outputData": {
    "results": ["FAILED"]
  }
}
```

> **Notes:** Enforces both cardinality (exactly 1) and value equality. Fails if the list has zero or more than one element, or if the single element doesn't match.

---

### LIST_CONTAINS_OBJECT_WITH_FIELDS

**Purpose:** Validates that at least one object in the list matches the provided partial structure (subset of fields).

**Assertion:**
```json
{
  "path": "$.outputData.dataItemNeeds",
  "operator": "LIST_CONTAINS_OBJECT_WITH_FIELDS",
  "value": {
    "type": "1035"
  }
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "dataItemNeeds": [
      {
        "type": "1035",
        "status": "ACTIVE"
      }
    ]
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "dataItemNeeds": [
      {
        "type": "1040"
      }
    ]
  }
}
```

> **Notes:** Performs structural subset match against each list element. Fails if no matching element is found. Extra fields on the actual object are allowed.

---

### ALL_MATCH

**Purpose:** Validates that **every** element in the list matches a condition. The condition defaults to equality when a scalar value is provided.

**Equals mode** — scalar Value (defaults to equality):
```json
{
  "path": "$.outputData.penalties",
  "operator": "ALL_MATCH",
  "value": 0
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "penalties": [0, 0, 0]
  }
}
```

**FAIL** — actual response:
```json
{
  "outputData": {
    "penalties": [0, 0, 1]
  }
}
```

**greaterThan mode:**
```json
{
  "path": "$.outputData.scores",
  "operator": "ALL_MATCH",
  "value": {
    "greaterThan": 50
  }
}
```

**PASS:** `{ "scores": [60, 70, 80] }` — **FAIL:** `{ "scores": [60, 50, 80] }`

**lessThan mode:**
```json
{
  "path": "$.outputData.scores",
  "operator": "ALL_MATCH",
  "value": {
    "lessThan": 50
  }
}
```

**PASS:** `{ "scores": [10, 20, 30] }` — **FAIL:** `{ "scores": [10, 50, 30] }`

**between mode:**
```json
{
  "path": "$.outputData.scores",
  "operator": "ALL_MATCH",
  "value": {
    "between": {
      "min": 50,
      "max": 100
    }
  }
}
```

**PASS:** `{ "scores": [50, 75, 100] }` — **FAIL:** `{ "scores": [50, 110, 75] }`

> **Notes:** Scalar value → equality check. Object value must have one of: `greaterThan`, `lessThan`, or `between`. Empty lists pass vacuously. Error messages include the failing element's index.

---

## Object Operators


### HAS_KEYS

**Purpose:** Validates that the extracted object contains exactly the specified keys (values don't matter).

**Assertion:**
```json
{
  "path": "$.outputData.metadata",
  "operator": "HAS_KEYS",
  "value": ["id", "timestamp"]
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "metadata": {
      "id": "123",
      "timestamp": "2025",
      "extra": "allowed"
    }
  }
}
```

> **Notes:** Actual object can have extra keys, but must have all the specified keys.

---

### FIELD_EQUALS_OTHER_FIELD

**Purpose:** Validates that two fields within the response are equal to each other.

**Assertion:**
```json
{
  "path": "$.outputData.actualTotal",
  "operator": "FIELD_EQUALS_OTHER_FIELD",
  "value": "$.outputData.expectedTotal"
}
```

**PASS** — actual response:
```json
{
  "outputData": {
    "actualTotal": 100,
    "expectedTotal": 100
  }
}
```

> **Notes:** `value` must be another valid JSONPath.

---

### OBJECT_CONTAINS_FIELDS

**Purpose:** Validates that the extracted object contains the specified key-value pairs. Extra fields on the actual object are allowed.

**Assertion:**
```json
{
  "path": "$.outputData.client",
  "operator": "OBJECT_CONTAINS_FIELDS",
  "value": {
    "clientType": "1031",
    "riskLevel": "HIGH"
  }
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
  "path": "$.outputData.client",
  "operator": "OBJECT_CONTAINS_FIELDS_IGNORE_NULLS",
  "value": {
    "clientType": "1031",
    "middleName": null
  }
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


## Logical Operators

### AND

**Purpose:** Validates that all provided assertions evaluate to true.

**Assertion:**
```json
{
  "operator": "AND",
  "assertions": [
    { "path": "$.status", "operator": "EQUALS", "value": "OK" },
    { "path": "$.code", "operator": "EQUALS", "value": 200 }
  ]
}
```

> **Notes:** Used for compound logic. `path` can be omitted at the top level. `AND` accepts one or more nested assertions.

---

### OR

**Purpose:** Validates that at least one provided assertion evaluates to true.

**Assertion:**
```json
{
  "operator": "OR",
  "assertions": [
    { "path": "$.status", "operator": "EQUALS", "value": "OK" },
    { "path": "$.status", "operator": "EQUALS", "value": "ACCEPTED" }
  ]
}
```

> **Notes:** `OR` accepts one or more nested assertions and passes as soon as one child passes.

---

### NOT

**Purpose:** Validates that the provided assertion evaluates to false.

**Assertion:**
```json
{
  "operator": "NOT",
  "assertions": [
    {
      "path": "$.status",
      "operator": "EQUALS",
      "value": "ERROR"
    }
  ]
}
```

> **Notes:** `NOT` requires exactly one nested assertion.

---

## Design Philosophy & Architecture

The `AssertionOperator` model separates JSONPath navigation from semantic validation. JSONPath extracts data; operators enforce business intent. This prevents silent coercion, enforces cardinality, and improves readability.
