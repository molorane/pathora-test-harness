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
  "JsonPath": "$.outputData.status",
  "Operator": "EQUALS",
  "Value": "APPROVED"
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

> **Notes:** Performs strict comparison. Types must match. If JsonPath returns a list instead of a scalar, the assertion fails.

---

### NOT_EQUALS

**Purpose:** Validates that the extracted value is NOT equal to the expected value.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.status",
  "Operator": "NOT_EQUALS",
  "Value": "DECLINED"
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
  "JsonPath": "$.outputData.score",
  "Operator": "GREATER_THAN",
  "Value": 50
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
  "JsonPath": "$.outputData.score",
  "Operator": "LESS_THAN",
  "Value": 100
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
  "JsonPath": "$.outputData.riskScore",
  "Operator": "BETWEEN",
  "Value": {
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

> **Notes:** Value must be a JSON object with `min` and `max` keys. Both boundaries are inclusive. Numeric types required.

---


### GREATER_THAN_OR_EQUALS

**Purpose:** Validates that the extracted numeric value is greater than or equal to the expected value.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.score",
  "Operator": "GREATER_THAN_OR_EQUALS",
  "Value": 50
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
  "JsonPath": "$.outputData.score",
  "Operator": "LESS_THAN_OR_EQUALS",
  "Value": 100
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
  "JsonPath": "$.outputData.referenceId",
  "Operator": "REGEX_MATCH",
  "Value": "^REF-\\d{4}-\\d{5}$",
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

> **Notes:** Uses full match (`matches()`), not partial find. Value must be a valid regex string. Case-sensitive by default. The `Description` field is optional but recommended for regex patterns to explain intent.

---


## String Operators

### STARTS_WITH

**Purpose:** Validates that the extracted string starts with the specified prefix.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.reference",
  "Operator": "STARTS_WITH",
  "Value": "REF-"
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
  "JsonPath": "$.outputData.reference",
  "Operator": "ENDS_WITH",
  "Value": "-Z"
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
  "JsonPath": "$.outputData.issueDate",
  "Operator": "DATE_BEFORE",
  "Value": "2025-01-01"
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
  "JsonPath": "$.outputData.expiryDate",
  "Operator": "DATE_AFTER",
  "Value": "2025-01-01"
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
  "JsonPath": "$.outputData.timestamp",
  "Operator": "DATETIME_BEFORE",
  "Value": "2025-01-01T12:00:00Z"
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
  "JsonPath": "$.outputData.timestamp",
  "Operator": "DATETIME_AFTER",
  "Value": "2025-01-01T12:00:00Z"
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

### DATE_BEFORE_NOW

**Purpose:** Validates that the extracted date/datetime is before the current system time.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.timestamp",
  "Operator": "DATE_BEFORE_NOW"
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

> **Notes:** No `Value` required.

---

### DATE_AFTER_NOW

**Purpose:** Validates that the extracted date/datetime is after the current system time.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.expiryDate",
  "Operator": "DATE_AFTER_NOW"
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

> **Notes:** No `Value` required.

---

### DATE_WITHIN_LAST

**Purpose:** Validates that the extracted date falls within the last specified duration (e.g., last 30 days).

**Assertion:**
```json
{
  "JsonPath": "$.outputData.timestamp",
  "Operator": "DATE_WITHIN_LAST",
  "Value": "P30D"
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

> **Notes:** `Value` must be an ISO-8601 duration (e.g., `P30D` for 30 days).

---

### DATE_WITHIN_NEXT

**Purpose:** Validates that the extracted date falls within the next specified duration.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.expiryDate",
  "Operator": "DATE_WITHIN_NEXT",
  "Value": "P30D"
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

> **Notes:** `Value` must be an ISO-8601 duration.

---


## Duration Operators

### DURATION_BETWEEN

**Purpose:** Validates that the duration between two dates is within a specific range.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.times",
  "Operator": "DURATION_BETWEEN",
  "Value": {
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
  "JsonPath": "$.outputData.processingTime",
  "Operator": "DURATION_EQUALS",
  "Value": "PT5M"
}
```

> **Notes:** Requires ISO-8601 duration string.

---

### DURATION_GREATER_THAN

**Purpose:** Validates that the extracted duration is greater than the expected duration.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.processingTime",
  "Operator": "DURATION_GREATER_THAN",
  "Value": "PT1M"
}
```

---

### DURATION_LESS_THAN

**Purpose:** Validates that the extracted duration is less than the expected duration.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.processingTime",
  "Operator": "DURATION_LESS_THAN",
  "Value": "PT1H"
}
```

---

### DATE_AFTER_DURATION

**Purpose:** Validates that the given date is after exactly a specified duration from another reference date.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.completionDate",
  "Operator": "DATE_AFTER_DURATION",
  "Value": "P1D"
}
```

---

### DATE_BEFORE_DURATION

**Purpose:** Validates that the given date is before a specified duration from a reference date.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.completionDate",
  "Operator": "DATE_BEFORE_DURATION",
  "Value": "P1D"
}
```

---

## Structural Operators

### EXISTS

**Purpose:** Validates that the JsonPath resolves to at least one value.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.referenceId",
  "Operator": "EXISTS"
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

> **Notes:** Does not validate value content. Only checks presence. No `Value` field required.

---

### ARRAY_SIZE_EQUALS

**Purpose:** Validates that the extracted array has the specified length.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.reasonCodes",
  "Operator": "ARRAY_SIZE_EQUALS",
  "Value": 2
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

> **Notes:** Enforces cardinality. Prevents hidden extra elements. A null or missing array is treated as size 0.

---

## Array Operators


### CONTAINS_ANY

**Purpose:** Validates that the extracted array contains at least one of the expected values.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.statusTags",
  "Operator": "CONTAINS_ANY",
  "Value": ["PENDING", "APPROVED"]
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

> **Notes:** Fails if the array lacks all the provided values.

---

### CONTAINS_ALL

**Purpose:** Validates that the extracted array contains all the expected values (order independent).

**Assertion:**
```json
{
  "JsonPath": "$.outputData.roles",
  "Operator": "CONTAINS_ALL",
  "Value": ["ADMIN", "USER"]
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

### ARRAY_IS_EMPTY

**Purpose:** Validates that the extracted array is empty.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.errors",
  "Operator": "ARRAY_IS_EMPTY"
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

> **Notes:** No `Value` required. Null arrays may be treated as empty depending on config.

---

### UNIQUE_ELEMENTS

**Purpose:** Validates that all elements in the extracted array are unique.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.ids",
  "Operator": "UNIQUE_ELEMENTS"
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

> **Notes:** No `Value` required.

---

### ARRAY_CONTAINS

**Purpose:** Validates that an array contains the specified value.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.reasonCodes",
  "Operator": "ARRAY_CONTAINS",
  "Value": "1004"
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

> **Notes:** JsonPath must resolve to an array. If a scalar is returned, the assertion fails.

---

### ARRAY_CONTAINS_ONLY_VALUES

**Purpose:** Validates that the array contains exactly the specified values, in any order. The array must have the same size as the expected list, and contain all expected elements.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.tags",
  "Operator": "ARRAY_CONTAINS_ONLY_VALUES",
  "Value": ["B", "A"]
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

### ARRAY_CONTAINS_ONLY_ONE_VALUE

**Purpose:** Validates that the array contains exactly one element, and that element equals the expected value.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.results",
  "Operator": "ARRAY_CONTAINS_ONLY_ONE_VALUE",
  "Value": "SUCCESS"
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

> **Notes:** Enforces both cardinality (exactly 1) and value equality. Fails if the array has zero or more than one element, or if the single element doesn't match.

---

### ARRAY_CONTAINS_OBJECT_WITH_FIELDS

**Purpose:** Validates that at least one object in the array matches the provided partial structure (subset of fields).

**Assertion:**
```json
{
  "JsonPath": "$.outputData.dataItemNeeds",
  "Operator": "ARRAY_CONTAINS_OBJECT_WITH_FIELDS",
  "Value": {
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

> **Notes:** Performs structural subset match against each array element. Fails if no matching element is found. Extra fields on the actual object are allowed.

---

### ALL_MATCH

**Purpose:** Validates that **every** element in the array matches a condition. The condition defaults to equality when a scalar value is provided.

**Equals mode** — scalar Value (defaults to equality):
```json
{
  "JsonPath": "$.outputData.penalties",
  "Operator": "ALL_MATCH",
  "Value": 0
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
  "JsonPath": "$.outputData.scores",
  "Operator": "ALL_MATCH",
  "Value": {
    "greaterThan": 50
  }
}
```

**PASS:** `{ "scores": [60, 70, 80] }` — **FAIL:** `{ "scores": [60, 50, 80] }`

**lessThan mode:**
```json
{
  "JsonPath": "$.outputData.scores",
  "Operator": "ALL_MATCH",
  "Value": {
    "lessThan": 50
  }
}
```

**PASS:** `{ "scores": [10, 20, 30] }` — **FAIL:** `{ "scores": [10, 50, 30] }`

**between mode:**
```json
{
  "JsonPath": "$.outputData.scores",
  "Operator": "ALL_MATCH",
  "Value": {
    "between": {
      "min": 50,
      "max": 100
    }
  }
}
```

**PASS:** `{ "scores": [50, 75, 100] }` — **FAIL:** `{ "scores": [50, 110, 75] }`

> **Notes:** Scalar Value → equality check. Object Value must have one of: `greaterThan`, `lessThan`, or `between`. Empty arrays pass vacuously. Error messages include the failing element's index.

---

## Object Operators


### HAS_KEYS

**Purpose:** Validates that the extracted object contains exactly the specified keys (values don't matter).

**Assertion:**
```json
{
  "JsonPath": "$.outputData.metadata",
  "Operator": "HAS_KEYS",
  "Value": ["id", "timestamp"]
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
  "JsonPath": "$.outputData.actualTotal",
  "Operator": "FIELD_EQUALS_OTHER_FIELD",
  "Value": "$.outputData.expectedTotal"
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

> **Notes:** `Value` must be another valid JsonPath.

---

### OBJECT_CONTAINS_FIELDS

**Purpose:** Validates that the extracted object contains the specified key-value pairs. Extra fields on the actual object are allowed.

**Assertion:**
```json
{
  "JsonPath": "$.outputData.client",
  "Operator": "OBJECT_CONTAINS_FIELDS",
  "Value": {
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
  "JsonPath": "$.outputData.client",
  "Operator": "OBJECT_CONTAINS_FIELDS_IGNORE_NULLS",
  "Value": {
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
  "Operator": "AND",
  "Value": [
    { "JsonPath": "$.status", "Operator": "EQUALS", "Value": "OK" },
    { "JsonPath": "$.code", "Operator": "EQUALS", "Value": 200 }
  ]
}
```

> **Notes:** Used for compound logic. `JsonPath` might be omitted at the top level.

---

### OR

**Purpose:** Validates that at least one provided assertion evaluates to true.

**Assertion:**
```json
{
  "Operator": "OR",
  "Value": [
    { "JsonPath": "$.status", "Operator": "EQUALS", "Value": "OK" },
    { "JsonPath": "$.status", "Operator": "EQUALS", "Value": "ACCEPTED" }
  ]
}
```

---

### NOT

**Purpose:** Validates that the provided assertion evaluates to false.

**Assertion:**
```json
{
  "Operator": "NOT",
  "Value": { 
    "JsonPath": "$.status", 
    "Operator": "EQUALS", 
    "Value": "ERROR" 
  }
}
```

---

## Design Philosophy & Architecture

The `AssertionOperator` model separates JsonPath navigation from semantic validation. JsonPath extracts data; operators enforce business intent. This prevents silent coercion, enforces cardinality, and improves readability.
