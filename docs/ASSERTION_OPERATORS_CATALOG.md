# Pathora Test Harness: Complete Assertion Operators Reference

This document provides a comprehensive reference of all assertion operators supported by the Pathora Test Harness engine. Each operator is categorized by functional domain and includes its description, `AssertionOperator` enum value, underlying evaluator class, and sample JSON usage.

> **💡 Temporal Testing & Dynamic Expressions Guide**:
> For an in-depth reference on dynamic date tokens (`{{$CURRENT_DATE}}`, `{{$CURRENT_DATETIME}}`), relative offsets (`+30d`, `-25y`, `+2h`), custom formatting patterns, 6 timezone configuration methods, and `PathoraClock` deterministic time-travel testing, see **[DATE_EXPRESSIONS_AND_TIMEZONES.md](DATE_EXPRESSIONS_AND_TIMEZONES.md)**.

---

## Table of Contents
1. [Scalar Operators](#1-scalar-operators)
2. [String Operators](#2-string-operators)
3. [Date Operators](#3-date-operators)
4. [Datetime & Time Operators](#4-datetime--time-operators)
5. [Duration Operators](#5-duration-operators)
6. [Structural Operators](#6-structural-operators)
7. [Array & Collection Operators](#7-array--collection-operators)
8. [Object Operators](#8-object-operators)
9. [Money Operators](#9-money-operators)
10. [Logical Operators](#10-logical-operators)

---

## 1. Scalar Operators

Scalar operators assert equality, numeric ranges, sign, nullability, type classifications, and boolean flags on scalar values.

### Operators Summary

| Enum Name | Evaluator Class | Description |
| :--- | :--- | :--- |
| `EQUALS` | `EqualsEvaluator` | Validates strict equality between actual and expected value. (Default if operator is omitted). |
| `NOT_EQUALS` | `NotEqualsEvaluator` | Validates that actual value does not equal expected value. |
| `GREATER_THAN` | `GreaterThanEvaluator` | Validates `actual > expected` numerically. |
| `GREATER_THAN_OR_EQUALS` | `GreaterThanOrEqualsEvaluator` | Validates `actual >= expected` numerically. |
| `LESS_THAN` | `LessThanEvaluator` | Validates `actual < expected` numerically. |
| `LESS_THAN_OR_EQUALS` | `LessThanOrEqualsEvaluator` | Validates `actual <= expected` numerically. |
| `BETWEEN` | `BetweenEvaluator` | Validates `min <= actual <= max` inclusive. |
| `EQUALS_WITH_TOLERANCE` | `EqualsWithToleranceEvaluator` | Validates numeric equality within a tolerance delta. |
| `IS_NULL` | `IsNullEvaluator` | Validates actual value is `null`. |
| `IS_NOT_NULL` | `IsNotNullEvaluator` | Validates actual value is not `null`. |
| `IS_TRUE` | `IsTrueEvaluator` | Validates actual value is boolean `true` (or string `"true"`). |
| `IS_FALSE` | `IsFalseEvaluator` | Validates actual value is boolean `false` (or string `"false"`). |
| `IS_NUMBER` | `IsNumberEvaluator` | Validates actual value is a valid numeric type. |
| `IS_INTEGER` | `IsIntegerEvaluator` | Validates actual value is a whole integer without fractional parts. |
| `IS_DECIMAL` | `IsDecimalEvaluator` | Validates actual value is a floating-point decimal. |
| `IS_POSITIVE` | `IsPositiveEvaluator` | Validates `actual > 0`. |
| `IS_NEGATIVE` | `IsNegativeEvaluator` | Validates `actual < 0`. |
| `IS_ZERO` | `IsZeroEvaluator` | Validates `actual == 0`. |

### JSON Usage Examples

```json
{
  "assertions": [
    {
      "path": "$.statusCode",
      "operator": "EQUALS",
      "value": 200
    },
    {
      "path": "$.status",
      "operator": "NOT_EQUALS",
      "value": "FAILED"
    },
    {
      "path": "$.score",
      "operator": "GREATER_THAN_OR_EQUALS",
      "value": 750
    },
    {
      "path": "$.riskScore",
      "operator": "BETWEEN",
      "value": {
        "min": 100,
        "max": 900
      }
    },
    {
      "path": "$.interestRate",
      "operator": "EQUALS_WITH_TOLERANCE",
      "value": {
        "expected": 6.5,
        "tolerance": 0.05
      }
    },
    {
      "path": "$.deletedAt",
      "operator": "IS_NULL"
    },
    {
      "path": "$.active",
      "operator": "IS_TRUE"
    },
    {
      "path": "$.approvedAmount",
      "operator": "IS_POSITIVE"
    }
  ]
}
```

---

## 2. String Operators

String operators evaluate substrings, regex patterns, casing, formatting, blankness, and character classifications.

### Operators Summary

| Enum Name | Evaluator Class | Description |
| :--- | :--- | :--- |
| `REGEX_MATCH` | `RegexMatchEvaluator` | Validates string matches regular expression. |
| `STARTS_WITH` | `StartsWithEvaluator` | Validates string starts with prefix (case-sensitive). |
| `ENDS_WITH` | `EndsWithEvaluator` | Validates string ends with suffix (case-sensitive). |
| `STRING_CONTAINS` | `StringContainsEvaluator` | Validates string contains substring (case-sensitive). |
| `STRING_CONTAINS_IGNORE_CASE` | `StringContainsIgnoreCaseEvaluator` | Validates string contains substring (case-insensitive). |
| `STRING_DOES_NOT_CONTAIN` | `StringDoesNotContainEvaluator` | Validates string does not contain substring. |
| `STRING_DOES_NOT_CONTAIN_IGNORE_CASE` | `StringDoesNotContainIgnoreCaseEvaluator` | Validates string does not contain substring (case-insensitive). |
| `STRING_EQUALS_IGNORE_CASE` | `StringEqualsIgnoreCaseEvaluator` | Validates equality ignoring character casing. |
| `STRING_STARTS_WITH_IGNORE_CASE` | `StringStartsWithIgnoreCaseEvaluator` | Validates prefix match ignoring character casing. |
| `STRING_STARTS_WITH_ANY` | `StringStartsWithAnyEvaluator` | Validates string starts with any prefix in candidate list. |
| `STRING_ENDS_WITH_ANY` | `StringEndsWithAnyEvaluator` | Validates string ends with any suffix in candidate list. |
| `STRING_ENDS_WITH_ANY_IGNORE_CASE` | `StringEndsWithAnyIgnoreCaseEvaluator` | Validates string ends with any suffix in candidate list ignoring casing. |
| `STRING_CONTAINS_WHITESPACE` | `StringContainsWhitespaceEvaluator` | Validates string contains at least one whitespace character. |
| `STRING_EQUALS_ANY` | `StringEqualsAnyEvaluator` | Validates string equals any candidate string in list. |
| `STRING_EQUALS_ANY_IGNORE_CASE` | `StringEqualsAnyIgnoreCaseEvaluator` | Validates string equals any candidate in list ignoring casing. |
| `STRING_LENGTH_EQUALS` | `StringLengthEqualsEvaluator` | Validates string length is exactly equal to integer. |
| `STRING_LENGTH_BETWEEN` | `StringLengthBetweenEvaluator` | Validates string length is between min and max bounds. |
| `STRING_LENGTH_GREATER_THAN` | `StringLengthGreaterThanEvaluator` | Validates string length > expected length. |
| `STRING_LENGTH_LESS_THAN` | `StringLengthLessThanEvaluator` | Validates string length < expected length. |
| `IS_STRING_MIXED_CASE` | `IsStringMixedCaseEvaluator` | Validates string contains both uppercase and lowercase characters. |
| `IS_STRING_LOWER_CASE` | `IsStringLowerCaseEvaluator` | Validates string contains only lowercase characters. |
| `IS_STRING_UPPER_CASE` | `IsStringUpperCaseEvaluator` | Validates string contains only uppercase characters. |
| `IS_STRING_ALPHA_NUMERIC` | `IsStringAlphaNumericEvaluator` | Validates string contains only letters and digits. |
| `IS_STRING_ALPHA_SPACE` | `IsStringAlphaSpaceEvaluator` | Validates string contains only letters and spaces. |
| `IS_STRING_ALPHA` | `IsStringAlphaEvaluator` | Validates string contains only letters. |
| `IS_STRING_NUMERIC` | `IsStringNumericEvaluator` | Validates string contains only digits. |
| `IS_STRING_BLANK` | `IsStringBlankEvaluator` | Validates string is `null`, empty, or only whitespace. |
| `IS_STRING_NOT_BLANK` | `IsStringNotBlankEvaluator` | Validates string is non-null and contains non-whitespace characters. |
| `IS_STRING_EMPTY` | `IsStringEmptyEvaluator` | Validates string is `null` or `""`. |
| `IS_STRING_NOT_EMPTY` | `IsStringNotEmptyEvaluator` | Validates string is non-null and length > 0. |
| `IS_STRING_NONE_EMPTY` | `IsStringNoneEmptyEvaluator` | Validates none of the strings in array are empty. |
| `IS_ANY_STRING_BLANK` | `IsAnyStringBlankEvaluator` | Validates at least one string in array is blank. |
| `IS_NONE_STRING_BLANK` | `IsNoneStringBlankEvaluator` | Validates no strings in array are blank. |
| `IS_ALL_STRING_BLANK` | `IsAllStringBlankEvaluator` | Validates all strings in array are blank. |
| `IS_UUID` | `IsUuidEvaluator` | Validates standard UUID format (8-4-4-4-12 hex). |
| `IS_EMAIL` | `IsEmailEvaluator` | Validates valid email address format. |
| `IS_URL` | `IsUrlEvaluator` | Validates valid URL/URI format with scheme and host. |
| `IS_IP_ADDRESS` | `IsIpAddressEvaluator` | Validates valid IPv4 or IPv6 address format. |

### JSON Usage Examples

```json
{
  "assertions": [
    {
      "path": "$.email",
      "operator": "IS_EMAIL"
    },
    {
      "path": "$.orderId",
      "operator": "STARTS_WITH",
      "value": "ORD-"
    },
    {
      "path": "$.username",
      "operator": "STRING_LENGTH_BETWEEN",
      "value": {
        "min": 4,
        "max": 20
      }
    },
    {
      "path": "$.role",
      "operator": "STRING_EQUALS_ANY",
      "value": ["ADMIN", "MANAGER", "USER"]
    },
    {
      "path": "$.trackingCode",
      "operator": "IS_UUID"
    }
  ]
}
```

---

## 3. Date Operators

Date operators evaluate calendar dates, year/month/day components, relative past/future checks, and date ranges. All date operators support dynamic expressions (e.g. `{{$CURRENT_DATE}}`, `{{$CURRENT_DATE + 30d}}`) and temporal state sourced from `PathoraClock`.

### Operators Summary

| Enum Name | Evaluator Class | Description |
| :--- | :--- | :--- |
| `DATE_BEFORE` | `DateBeforeEvaluator` | Validates date is strictly before expected date (`YYYY-MM-DD` or dynamic token). |
| `DATE_AFTER` | `DateAfterEvaluator` | Validates date is strictly after expected date (`YYYY-MM-DD` or dynamic token). |
| `DATE_EQUALS` | `DateEqualsEvaluator` | Validates date equals expected date (`YYYY-MM-DD` or `{{$CURRENT_DATE}}`). |
| `DATE_YEAR_EQUALS` | `DateYearEqualsEvaluator` | Validates year component equals integer year (e.g. `2026`). |
| `DATE_MONTH_EQUALS` | `DateMonthEqualsEvaluator` | Validates month component equals month number (1-12) or name. |
| `DATE_DAY_EQUALS` | `DateDayEqualsEvaluator` | Validates day of month equals integer (1-31). |
| `DATE_DAY_OF_WEEK_EQUALS` | `DateDayOfWeekEqualsEvaluator` | Validates day of week equals number (1-7) or name (e.g. `MONDAY`). |
| `DATE_BETWEEN` | `DateBetweenEvaluator` | Validates date falls inclusively between min and max bounds. |
| `IS_PAST_DATE` | `IsPastDateEvaluator` | Validates date is in the past relative to PathoraClock. |
| `IS_FUTURE_DATE` | `IsFutureDateEvaluator` | Validates date is in the future relative to PathoraClock. |
| `IS_TODAY` | `IsDateTodayEvaluator` | Validates date is today relative to PathoraClock. |
| `DATE_WITHIN_LAST` | `DateWithinLastEvaluator` | Validates date is within preceding time window from now. |
| `DATE_WITHIN_NEXT` | `DateWithinNextEvaluator` | Validates date is within upcoming time window from now. |

### JSON Usage Examples

```json
{
  "assertions": [
    {
      "path": "$.effectiveDate",
      "operator": "DATE_YEAR_EQUALS",
      "value": 2026
    },
    {
      "path": "$.birthDate",
      "operator": "DATE_BEFORE",
      "value": "2000-01-01"
    },
    {
      "path": "$.renewalDate",
      "operator": "DATE_BETWEEN",
      "value": {
        "start": "2026-01-01",
        "end": "2026-12-31"
      }
    },
    {
      "path": "$.appointmentDate",
      "operator": "DATE_WITHIN_NEXT",
      "value": {
        "amount": 30,
        "unit": "DAYS"
      }
    }
  ]
}
```

---

## 4. Datetime & Time Operators

Datetime and Time operators handle ISO-8601 timestamps, hours, minutes, seconds, tolerances, and time bounds.

### Operators Summary

| Enum Name | Evaluator Class | Description |
| :--- | :--- | :--- |
| `DATETIME_BEFORE` | `DateTimeBeforeEvaluator` | Validates timestamp is before expected ISO datetime. |
| `DATETIME_AFTER` | `DateTimeAfterEvaluator` | Validates timestamp is after expected ISO datetime. |
| `DATETIME_EQUALS` | `DateTimeEqualsEvaluator` | Validates timestamp equals expected ISO datetime. |
| `DATETIME_EQUALS_WITH_TOLERANCE` | `DateTimeEqualsWithToleranceEvaluator` | Validates timestamp equals expected within duration tolerance. |
| `DATETIME_BETWEEN` | `DateTimeBetweenEvaluator` | Validates timestamp falls between start and end datetimes. |
| `IS_PAST_DATETIME` | `IsPastDateTimeEvaluator` | Validates timestamp is in the past. |
| `IS_FUTURE_DATETIME` | `IsFutureDateTimeEvaluator` | Validates timestamp is in the future. |
| `DATETIME_WITHIN_LAST` | `DateTimeWithinLastEvaluator` | Validates timestamp falls within preceding duration from now. |
| `DATETIME_WITHIN_NEXT` | `DateTimeWithinNextEvaluator` | Validates timestamp falls within upcoming duration from now. |
| `TIME_EQUALS` | `TimeEqualsEvaluator` | Validates time component equals `HH:mm` or `HH:mm:ss`. |
| `TIME_BEFORE` | `TimeBeforeEvaluator` | Validates time component is strictly before expected time. |
| `TIME_AFTER` | `TimeAfterEvaluator` | Validates time component is strictly after expected time. |
| `TIME_BETWEEN` | `TimeBetweenEvaluator` | Validates time component is inclusively between min and max times. |
| `DATE_HOUR_EQUALS` | `DateHourEqualsEvaluator` | Validates hour component equals integer (0-23). |
| `DATE_MINUTE_EQUALS` | `DateMinuteEqualsEvaluator` | Validates minute component equals integer (0-59). |
| `DATE_SECOND_EQUALS` | `DateSecondEqualsEvaluator` | Validates second component equals integer (0-59). |

### JSON Usage Examples

```json
{
  "assertions": [
    {
      "path": "$.createdAt",
      "operator": "IS_PAST_DATETIME"
    },
    {
      "path": "$.scheduledTime",
      "operator": "TIME_BETWEEN",
      "value": {
        "min": "08:00:00",
        "max": "17:00:00"
      }
    },
    {
      "path": "$.completedAt",
      "operator": "DATETIME_EQUALS_WITH_TOLERANCE",
      "value": {
        "expected": "2026-09-15T12:00:00Z",
        "tolerance": 5,
        "unit": "SECONDS"
      }
    }
  ]
}
```

---

## 5. Duration Operators

Duration operators calculate elapsed time between two nodes in a document or verify relative duration offsets.

### Operators Summary

| Enum Name | Evaluator Class | Description |
| :--- | :--- | :--- |
| `DURATION_EQUALS` | `DurationEqualsEvaluator` | Validates elapsed duration between two dates equals expected value. |
| `DURATION_BETWEEN` | `DurationBetweenDatesEvaluator` | Validates elapsed duration between two dates falls within range. |
| `DURATION_GREATER_THAN` | `DurationGreaterThanEvaluator` | Validates elapsed duration between two dates exceeds threshold. |
| `DURATION_LESS_THAN` | `DurationLessThanEvaluator` | Validates elapsed duration between two dates is less than threshold. |
| `DATE_AFTER_DURATION` | `DateAfterDurationEvaluator` | Validates actual date is at least a specified duration after a reference date. |
| `DATE_BEFORE_DURATION` | `DateBeforeDurationEvaluator` | Validates actual date is at least a specified duration before a reference date. |

### JSON Usage Examples

```json
{
  "assertions": [
    {
      "path": "$.startDate",
      "operator": "DURATION_EQUALS",
      "value": {
        "startPath": "$.startDate",
        "endPath": "$.endDate",
        "unit": "DAYS",
        "expected": 365
      }
    },
    {
      "path": "$.startDate",
      "operator": "DURATION_BETWEEN",
      "value": {
        "startPath": "$.startDate",
        "endPath": "$.endDate",
        "unit": "MONTHS",
        "min": 11,
        "max": 13
      }
    },
    {
      "path": "$.startDate",
      "operator": "DATE_AFTER_DURATION",
      "value": {
        "basePath": "$.startDate",
        "comparePath": "$.endDate",
        "amount": 300,
        "unit": "DAYS"
      }
    }
  ]
}
```

---

## 6. Structural Operators

Structural operators assert the presence, absence, and collection sizes of JSONPath targets.

### Operators Summary

| Enum Name | Evaluator Class | Description |
| :--- | :--- | :--- |
| `PATH_EXISTS` | `PathExistsEvaluator` | Validates that the JSONPath exists and resolves to a node. |
| `PATH_NOT_EXISTS` | `PathNotExistsEvaluator` | Validates that the JSONPath does not exist in the response. |
| `LIST_SIZE_EQUALS` | `ListSizeEqualsEvaluator` | Validates collection size is exactly equal to integer. |
| `LIST_SIZE_GREATER_THAN` | `ListSizeGreaterThanEvaluator` | Validates collection size > expected integer. |
| `LIST_SIZE_LESS_THAN` | `ListSizeLessThanEvaluator` | Validates collection size < expected integer. |
| `LIST_SIZE_BETWEEN` | `ListSizeBetweenEvaluator` | Validates collection size is between min and max bounds. |

### JSON Usage Examples

```json
{
  "assertions": [
    {
      "path": "$.auditHeader",
      "operator": "PATH_EXISTS"
    },
    {
      "path": "$.legacyError",
      "operator": "PATH_NOT_EXISTS"
    },
    {
      "path": "$.items",
      "operator": "LIST_SIZE_EQUALS",
      "value": 3
    },
    {
      "path": "$.clauses",
      "operator": "LIST_SIZE_BETWEEN",
      "value": {
        "min": 1,
        "max": 10
      }
    }
  ]
}
```

---

## 7. Array & Collection Operators

Array operators evaluate element membership, uniqueness, predicates across elements, sorting order, and subset relations.

### Operators Summary

| Enum Name | Evaluator Class | Description |
| :--- | :--- | :--- |
| `LIST_CONTAINS` | `ListContainsEvaluator` | Validates array contains expected element. |
| `LIST_DOES_NOT_CONTAIN` | `ListDoesNotContainEvaluator` | Validates array does not contain element. |
| `LIST_CONTAINS_ONLY_VALUES` | `ListContainsOnlyValuesEvaluator` | Validates array contains exactly the specified elements (ignoring order). |
| `LIST_CONTAINS_ONLY_ONE_VALUE` | `ListContainsOnlyOneValueEvaluator` | Validates array contains exactly one occurrence of element. |
| `LIST_CONTAINS_OBJECT_WITH_FIELDS` | `ListContainsObjectWithFieldsEvaluator` | Validates array contains an object matching all specified fields. |
| `LIST_CONTAINS_PARTIAL_OBJECT_WITH_FIELDS` | `ListContainsPartialObjectWithFieldsEvaluator` | Validates array contains an object matching partial fields recursively. |
| `CONTAINS_ALL` | `ContainsAllEvaluator` | Validates array contains all elements in expected list. |
| `CONTAINS_ANY` | `ContainsAnyEvaluator` | Validates array contains at least one element from expected list. |
| `DOES_NOT_CONTAIN_ALL` | `DoesNotContainAllEvaluator` | Validates array does not contain all elements in expected list. |
| `DOES_NOT_CONTAIN_ANY` | `DoesNotContainAnyEvaluator` | Validates array contains none of the elements in expected list. |
| `ALL_MATCH` | `AllMatchEvaluator` | Validates all array elements satisfy the condition or value. |
| `ANY_MATCH` | `AnyMatchEvaluator` | Validates at least one array element satisfies the condition. |
| `NONE_MATCH` | `NoneMatchEvaluator` | Validates no array elements satisfy the condition. |
| `IS_EMPTY_LIST` | `IsEmptyListEvaluator` | Validates array is empty (size 0). |
| `IS_NOT_EMPTY_LIST` | `IsNotEmptyListEvaluator` | Validates array is not empty (size > 0). |
| `UNIQUE_ELEMENTS` | `UniqueElementsEvaluator` | Validates array contains no duplicate items. |
| `LIST_IS_SORTED_ASC` | `ListIsSortedAscEvaluator` | Validates elements are sorted in ascending natural order. |
| `LIST_IS_SORTED_DESC` | `ListIsSortedDescEvaluator` | Validates elements are sorted in descending natural order. |
| `VALUE_IN` | `ValueInEvaluator` | Validates extracted scalar value is in expected list. |
| `VALUE_NOT_IN` | `ValueNotInEvaluator` | Validates extracted scalar value is not in expected list. |

### JSON Usage Examples

```json
{
  "assertions": [
    {
      "path": "$.roles",
      "operator": "CONTAINS_ALL",
      "value": ["USER", "ADMIN"]
    },
    {
      "path": "$.tags",
      "operator": "UNIQUE_ELEMENTS"
    },
    {
      "path": "$.referenceCodes",
      "operator": "LIST_IS_SORTED_ASC"
    },
    {
      "path": "$.approvedClauses",
      "operator": "LIST_CONTAINS_OBJECT_WITH_FIELDS",
      "value": {
        "clauseId": "CLS-01",
        "status": "APPROVED"
      }
    },
    {
      "path": "$.status",
      "operator": "VALUE_IN",
      "value": ["APPROVED", "PENDING_REVIEW"]
    }
  ]
}
```

---

## 8. Object Operators

Object operators evaluate JSON map structures, required key presence, map emptiness, and cross-path comparisons.

### Operators Summary

| Enum Name | Evaluator Class | Description |
| :--- | :--- | :--- |
| `OBJECT_CONTAINS_FIELDS` | `ObjectContainsFieldsEvaluator` | Validates target object contains all specified key-value pairs. |
| `OBJECT_CONTAINS_PARTIAL_FIELDS` | `ObjectContainsPartialFieldsEvaluator` | Validates target object contains specified fields recursively. |
| `OBJECT_CONTAINS_FIELDS_IGNORE_NULLS` | `ObjectContainsFieldsIgnoreNullsEvaluator` | Validates target object matches non-null expected fields. |
| `HAS_KEYS` | `HasKeysEvaluator` | Validates target object contains all specified key names. |
| `DOES_NOT_HAVE_KEYS` | `DoesNotHaveKeysEvaluator` | Validates target object contains none of the specified keys. |
| `IS_EMPTY_OBJECT` | `IsEmptyObjectEvaluator` | Validates target object is an empty map (`{}`). |
| `IS_NOT_EMPTY_OBJECT` | `IsNotEmptyObjectEvaluator` | Validates target object is a non-empty map. |
| `FIELD_EQUALS_OTHER_FIELD` | `FieldEqualsOtherFieldEvaluator` | Validates value at leftPath equals value at rightPath. |
| `FIELD_NOT_EQUALS_OTHER_FIELD` | `FieldNotEqualsOtherFieldEvaluator` | Validates value at leftPath does not equal rightPath. |
| `FIELD_GREATER_THAN_OTHER_FIELD` | `FieldGreaterThanOtherFieldEvaluator` | Validates numeric value at leftPath > rightPath. |
| `FIELD_LESS_THAN_OTHER_FIELD` | `FieldLessThanOtherFieldEvaluator` | Validates numeric value at leftPath < rightPath. |

### JSON Usage Examples

```json
{
  "assertions": [
    {
      "path": "$.underwritingSummary",
      "operator": "OBJECT_CONTAINS_FIELDS",
      "value": {
        "approved": true,
        "tier": "PREMIUM"
      }
    },
    {
      "path": "$.underwriter",
      "operator": "HAS_KEYS",
      "value": ["code", "region", "contact"]
    },
    {
      "path": null,
      "operator": "FIELD_EQUALS_OTHER_FIELD",
      "value": {
        "leftPath": "$.totals.subtotal",
        "rightPath": "$.totals.calculatedSubtotal"
      }
    },
    {
      "path": null,
      "operator": "FIELD_GREATER_THAN_OTHER_FIELD",
      "value": {
        "leftPath": "$.totalPremium",
        "rightPath": "$.finalPrice"
      }
    }
  ]
}
```

---

## 9. Money Operators

Money operators handle monetary values with strict currency checking, scale normalization, and delta tolerance.

### Operators Summary

| Enum Name | Evaluator Class | Description |
| :--- | :--- | :--- |
| `MONEY_EQUALS` | `MoneyEqualsEvaluator` | Validates monetary amount (and currency if provided). |
| `MONEY_EQUALS_WITH_TOLERANCE` | `MoneyEqualsWithToleranceEvaluator` | Validates monetary value within specified delta tolerance. |
| `MONEY_GREATER_THAN` | `MoneyGreaterThanEvaluator` | Validates monetary amount > expected. |
| `MONEY_GREATER_THAN_OR_EQUALS` | `MoneyGreaterThanOrEqualsEvaluator` | Validates monetary amount >= expected. |
| `MONEY_LESS_THAN` | `MoneyLessThanEvaluator` | Validates monetary amount < expected. |
| `MONEY_LESS_THAN_OR_EQUALS` | `MoneyLessThanOrEqualsEvaluator` | Validates monetary amount <= expected. |
| `MONEY_BETWEEN` | `MoneyBetweenEvaluator` | Validates monetary amount falls between min and max bounds. |

### JSON Usage Examples

```json
{
  "assertions": [
    {
      "path": "$.balance",
      "operator": "MONEY_EQUALS",
      "value": {
        "amount": 15420.50,
        "currency": "USD"
      }
    },
    {
      "path": "$.totalPremium",
      "operator": "MONEY_GREATER_THAN",
      "value": 10000.00
    },
    {
      "path": "$.finalPrice",
      "operator": "MONEY_BETWEEN",
      "value": {
        "min": 10000.00,
        "max": 20000.00
      }
    }
  ]
}
```

---

## 10. Logical Operators

Logical operators combine nested assertion rules with boolean logic (`AND`, `OR`, `NOT`). Logical assertions are processed hierarchically by `ResponseAssertionExecutor`.

### Operators Summary

| Enum Name | Evaluator Handling | Description |
| :--- | :--- | :--- |
| `AND` | Built-in Logical Engine | Requires all child assertions in the `assertions` list to pass. |
| `OR` | Built-in Logical Engine | Requires at least one child assertion in the `assertions` list to pass. |
| `NOT` | Built-in Logical Engine | Inverts the outcome of child assertions (passes when child fails). |

### JSON Usage Examples

```json
{
  "assertions": [
    {
      "path": "$",
      "operator": "AND",
      "description": "Status is APPROVED and legalName matches",
      "assertions": [
        {
          "path": "$.status",
          "value": "APPROVED"
        },
        {
          "path": "$.legalName",
          "value": "Pathora Enterprise Solutions"
        }
      ]
    },
    {
      "path": "$",
      "operator": "OR",
      "description": "Credit rating is either AA+ or AAA",
      "assertions": [
        {
          "path": "$.creditRating",
          "value": "AA+"
        },
        {
          "path": "$.creditRating",
          "value": "AAA"
        }
      ]
    },
    {
      "path": "$",
      "operator": "NOT",
      "description": "Status must NOT be CANCELLED or SUSPENDED",
      "assertions": [
        {
          "path": "$.status",
          "operator": "VALUE_IN",
          "value": ["CANCELLED", "SUSPENDED"]
        }
      ]
    }
  ]
}
```

