# Pathora Test Harness: Assertion Operator Usage Guide

This guide demonstrates how to configure and use every assertion operator available in the **Pathora Test Harness** engine.

> **Note on Null Values:**
> For unary, structural, and flag-based operators where an expected value is not required, omit the `"value"` field entirely. The test harness engine defaults unassigned fields to `null`.

---

## Table of Contents

- [Pathora Test Harness: Assertion Operator Usage Guide](#pathora-test-harness-assertion-operator-usage-guide)
  - [Table of Contents](#table-of-contents)
  - [Assertion JSON Schema Overview](#assertion-json-schema-overview)
  - [1. Scalar Operators](#1-scalar-operators)
    - [EQUALS](#equals)
    - [NOT\_EQUALS](#not_equals)
    - [GREATER\_THAN](#greater_than)
    - [GREATER\_THAN\_OR\_EQUALS](#greater_than_or_equals)
    - [LESS\_THAN](#less_than)
    - [LESS\_THAN\_OR\_EQUALS](#less_than_or_equals)
    - [BETWEEN](#between)
    - [IS\_NULL](#is_null)
    - [IS\_NOT\_NULL](#is_not_null)
    - [IS\_TRUE](#is_true)
    - [IS\_FALSE](#is_false)
    - [IS\_NUMBER](#is_number)
    - [IS\_INTEGER](#is_integer)
    - [IS\_DECIMAL](#is_decimal)
    - [IS\_POSITIVE](#is_positive)
    - [IS\_NEGATIVE](#is_negative)
    - [IS\_ZERO](#is_zero)
    - [EQUALS\_WITH\_TOLERANCE](#equals_with_tolerance)
  - [2. String Operators](#2-string-operators)
    - [REGEX\_MATCH](#regex_match)
    - [STARTS\_WITH](#starts_with)
    - [ENDS\_WITH](#ends_with)
    - [STRING\_CONTAINS](#string_contains)
    - [STRING\_CONTAINS\_IGNORE\_CASE](#string_contains_ignore_case)
    - [STRING\_DOES\_NOT\_CONTAIN](#string_does_not_contain)
    - [STRING\_DOES\_NOT\_CONTAIN\_IGNORE\_CASE](#string_does_not_contain_ignore_case)
    - [STRING\_EQUALS\_IGNORE\_CASE](#string_equals_ignore_case)
    - [STRING\_STARTS\_WITH\_IGNORE\_CASE](#string_starts_with_ignore_case)
    - [STRING\_STARTS\_WITH\_ANY](#string_starts_with_any)
    - [STRING\_ENDS\_WITH\_ANY](#string_ends_with_any)
    - [STRING\_ENDS\_WITH\_ANY\_IGNORE\_CASE](#string_ends_with_any_ignore_case)
    - [STRING\_CONTAINS\_WHITESPACE](#string_contains_whitespace)
    - [STRING\_EQUALS\_ANY](#string_equals_any)
    - [STRING\_EQUALS\_ANY\_IGNORE\_CASE](#string_equals_any_ignore_case)
    - [STRING\_LENGTH\_EQUALS](#string_length_equals)
    - [STRING\_LENGTH\_BETWEEN](#string_length_between)
    - [STRING\_LENGTH\_GREATER\_THAN](#string_length_greater_than)
    - [STRING\_LENGTH\_LESS\_THAN](#string_length_less_than)
    - [IS\_STRING\_MIXED\_CASE](#is_string_mixed_case)
    - [IS\_STRING\_LOWER\_CASE](#is_string_lower_case)
    - [IS\_STRING\_UPPER\_CASE](#is_string_upper_case)
    - [IS\_STRING\_ALPHA\_NUMERIC](#is_string_alpha_numeric)
    - [IS\_STRING\_ALPHA\_SPACE](#is_string_alpha_space)
    - [IS\_STRING\_ALPHA](#is_string_alpha)
    - [IS\_STRING\_NUMERIC](#is_string_numeric)
    - [IS\_STRING\_BLANK](#is_string_blank)
    - [IS\_STRING\_NOT\_BLANK](#is_string_not_blank)
    - [IS\_STRING\_EMPTY](#is_string_empty)
    - [IS\_STRING\_NOT\_EMPTY](#is_string_not_empty)
    - [IS\_STRING\_NONE\_EMPTY](#is_string_none_empty)
    - [IS\_ANY\_STRING\_BLANK](#is_any_string_blank)
    - [IS\_NONE\_STRING\_BLANK](#is_none_string_blank)
    - [IS\_ALL\_STRING\_BLANK](#is_all_string_blank)
    - [IS\_UUID](#is_uuid)
    - [IS\_EMAIL](#is_email)
    - [IS\_URL](#is_url)
    - [IS\_IP\_ADDRESS](#is_ip_address)
  - [3. Date Operators](#3-date-operators)
    - [DATE\_BEFORE](#date_before)
    - [DATE\_AFTER](#date_after)
    - [DATE\_EQUALS](#date_equals)
    - [DATE\_YEAR\_EQUALS](#date_year_equals)
    - [DATE\_MONTH\_EQUALS](#date_month_equals)
    - [DATE\_DAY\_EQUALS](#date_day_equals)
    - [DATE\_DAY\_OF\_WEEK\_EQUALS](#date_day_of_week_equals)
    - [DATETIME\_BEFORE](#datetime_before)
    - [DATETIME\_AFTER](#datetime_after)
    - [DATETIME\_EQUALS](#datetime_equals)
    - [DATETIME\_EQUALS\_WITH\_TOLERANCE](#datetime_equals_with_tolerance)
    - [IS\_PAST\_DATE](#is_past_date)
    - [IS\_FUTURE\_DATE](#is_future_date)
    - [IS\_PAST\_DATETIME](#is_past_datetime)
    - [IS\_FUTURE\_DATETIME](#is_future_datetime)
    - [IS\_TODAY](#is_today)
    - [DATE\_BETWEEN](#date_between)
    - [DATETIME\_BETWEEN](#datetime_between)
    - [DATE\_WITHIN\_LAST](#date_within_last)
    - [DATE\_WITHIN\_NEXT](#date_within_next)
    - [DATETIME\_WITHIN\_LAST](#datetime_within_last)
    - [DATETIME\_WITHIN\_NEXT](#datetime_within_next)
    - [TIME\_EQUALS](#time_equals)
    - [TIME\_BEFORE](#time_before)
    - [TIME\_AFTER](#time_after)
    - [TIME\_BETWEEN](#time_between)
    - [DATE\_HOUR\_EQUALS](#date_hour_equals)
    - [DATE\_MINUTE\_EQUALS](#date_minute_equals)
    - [DATE\_SECOND\_EQUALS](#date_second_equals)
  - [4. Duration Operators](#4-duration-operators)
    - [DURATION\_BETWEEN](#duration_between)
    - [DURATION\_EQUALS](#duration_equals)
    - [DURATION\_GREATER\_THAN](#duration_greater_than)
    - [DURATION\_LESS\_THAN](#duration_less_than)
    - [DATE\_AFTER\_DURATION](#date_after_duration)
    - [DATE\_BEFORE\_DURATION](#date_before_duration)
  - [5. Structural Operators](#5-structural-operators)
    - [PATH\_EXISTS](#path_exists)
    - [PATH\_NOT\_EXISTS](#path_not_exists)
    - [LIST\_SIZE\_EQUALS](#list_size_equals)
    - [LIST\_SIZE\_GREATER\_THAN](#list_size_greater_than)
    - [LIST\_SIZE\_LESS\_THAN](#list_size_less_than)
    - [LIST\_SIZE\_BETWEEN](#list_size_between)
  - [6. Array / Collection Operators](#6-array--collection-operators)
    - [LIST\_CONTAINS](#list_contains)
    - [LIST\_DOES\_NOT\_CONTAIN](#list_does_not_contain)
    - [LIST\_CONTAINS\_ONLY\_VALUES](#list_contains_only_values)
    - [LIST\_CONTAINS\_ONLY\_ONE\_VALUE](#list_contains_only_one_value)
    - [LIST\_CONTAINS\_OBJECT\_WITH\_FIELDS](#list_contains_object_with_fields)
    - [LIST\_CONTAINS\_PARTIAL\_OBJECT\_WITH\_FIELDS](#list_contains_partial_object_with_fields)
    - [ALL\_MATCH](#all_match)
    - [ANY\_MATCH](#any_match)
    - [NONE\_MATCH](#none_match)
    - [CONTAINS\_ANY](#contains_any)
    - [CONTAINS\_ALL](#contains_all)
    - [DOES\_NOT\_CONTAIN\_ANY](#does_not_contain_any)
    - [DOES\_NOT\_CONTAIN\_ALL](#does_not_contain_all)
    - [IS\_EMPTY\_LIST](#is_empty_list)
    - [IS\_NOT\_EMPTY\_LIST](#is_not_empty_list)
    - [UNIQUE\_ELEMENTS](#unique_elements)
    - [LIST\_IS\_SORTED\_ASC](#list_is_sorted_asc)
    - [LIST\_IS\_SORTED\_DESC](#list_is_sorted_desc)
    - [VALUE\_IN](#value_in)
    - [VALUE\_NOT\_IN](#value_not_in)
  - [7. Object Operators](#7-object-operators)
    - [OBJECT\_CONTAINS\_FIELDS](#object_contains_fields)
    - [OBJECT\_CONTAINS\_PARTIAL\_FIELDS](#object_contains_partial_fields)
    - [OBJECT\_CONTAINS\_FIELDS\_IGNORE\_NULLS](#object_contains_fields_ignore_nulls)
    - [HAS\_KEYS](#has_keys)
    - [DOES\_NOT\_HAVE\_KEYS](#does_not_have_keys)
    - [IS\_EMPTY\_OBJECT](#is_empty_object)
    - [IS\_NOT\_EMPTY\_OBJECT](#is_not_empty_object)
    - [FIELD\_EQUALS\_OTHER\_FIELD](#field_equals_other_field)
    - [FIELD\_NOT\_EQUALS\_OTHER\_FIELD](#field_not_equals_other_field)
    - [FIELD\_GREATER\_THAN\_OTHER\_FIELD](#field_greater_than_other_field)
    - [FIELD\_LESS\_THAN\_OTHER\_FIELD](#field_less_than_other_field)
  - [8. Money Operators](#8-money-operators)
    - [MONEY\_EQUALS](#money_equals)
    - [MONEY\_EQUALS\_WITH\_TOLERANCE](#money_equals_with_tolerance)
    - [MONEY\_GREATER\_THAN](#money_greater_than)
    - [MONEY\_GREATER\_THAN\_OR\_EQUALS](#money_greater_than_or_equals)
    - [MONEY\_LESS\_THAN](#money_less_than)
    - [MONEY\_LESS\_THAN\_OR\_EQUALS](#money_less_than_or_equals)
    - [MONEY\_BETWEEN](#money_between)
  - [9. Logical Operators](#9-logical-operators)
    - [AND](#and)
    - [OR](#or)
    - [NOT](#not)

---

## Assertion JSON Schema Overview

An assertion entry contains the following attributes:

| Field | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `path` | String | Conditional | JSONPath expression targeting the node to validate (e.g. `$.status`). Omit or set to `null` for cross-field operations. |
| `operator` | String | Optional | The operator enum name. Defaults to `EQUALS` if omitted. |
| `value` | Any | Optional | The expected value, range, or configuration object. **Omit when not required.** |
| `description` | String | Optional | Descriptive summary of the assertion rule. |
| `assertions` | Array | Conditional | Nested child assertions used exclusively with logical operators (`AND`, `OR`, `NOT`). |

---

## 1. Scalar Operators

Scalar operators assert equality, numeric ranges, sign, nullability, type classifications, and boolean flags.

### EQUALS
Validates strict equality between actual and expected value.
```json
{
  "path": "$.statusCode",
  "operator": "EQUALS",
  "value": 200,
  "description": "Status code must be 200"
}
```

### NOT_EQUALS
Validates that actual value does not equal expected value.
```json
{
  "path": "$.status",
  "operator": "NOT_EQUALS",
  "value": "FAILED",
  "description": "Status must not be FAILED"
}
```

### GREATER_THAN
Validates that actual numeric value is strictly greater than expected threshold (`actual > expected`).
```json
{
  "path": "$.score",
  "operator": "GREATER_THAN",
  "value": 75,
  "description": "Score must be greater than 75"
}
```

### GREATER_THAN_OR_EQUALS
Validates that actual numeric value is greater than or equal to expected threshold (`actual >= expected`).
```json
{
  "path": "$.score",
  "operator": "GREATER_THAN_OR_EQUALS",
  "value": 80,
  "description": "Score must be at least 80"
}
```

### LESS_THAN
Validates that actual numeric value is strictly less than expected threshold (`actual < expected`).
```json
{
  "path": "$.riskIndex",
  "operator": "LESS_THAN",
  "value": 0.25,
  "description": "Risk index must be less than 0.25"
}
```

### LESS_THAN_OR_EQUALS
Validates that actual numeric value is less than or equal to expected threshold (`actual <= expected`).
```json
{
  "path": "$.latencyMs",
  "operator": "LESS_THAN_OR_EQUALS",
  "value": 250,
  "description": "Latency must not exceed 250ms"
}
```

### BETWEEN
Validates that actual numeric value is inclusively between min and max bounds (`min <= actual <= max`).
```json
{
  "path": "$.temperature",
  "operator": "BETWEEN",
  "value": {
    "min": 18.5,
    "max": 24.0
  },
  "description": "Temperature must be between 18.5 and 24.0"
}
```

### IS_NULL
Validates that actual value is `null`.
```json
{
  "path": "$.deletedAt",
  "operator": "IS_NULL",
  "description": "deletedAt must be null"
}
```

### IS_NOT_NULL
Validates that actual value is not `null`.
```json
{
  "path": "$.userId",
  "operator": "IS_NOT_NULL",
  "description": "userId must not be null"
}
```

### IS_TRUE
Validates that actual value is boolean `true` (or string `"true"`).
```json
{
  "path": "$.isActive",
  "operator": "IS_TRUE",
  "description": "User must be active"
}
```

### IS_FALSE
Validates that actual value is boolean `false` (or string `"false"`).
```json
{
  "path": "$.isDeleted",
  "operator": "IS_FALSE",
  "description": "User must not be marked as deleted"
}
```

### IS_NUMBER
Validates that actual value represents a valid numeric number.
```json
{
  "path": "$.totalWeight",
  "operator": "IS_NUMBER",
  "description": "Weight must be numeric"
}
```

### IS_INTEGER
Validates that actual value represents a whole number/integer with no fractional component.
```json
{
  "path": "$.itemCount",
  "operator": "IS_INTEGER",
  "description": "Item count must be a whole integer"
}
```

### IS_DECIMAL
Validates that actual value represents a decimal number with a fractional component.
```json
{
  "path": "$.exchangeRate",
  "operator": "IS_DECIMAL",
  "description": "Exchange rate must be a decimal"
}
```

### IS_POSITIVE
Validates that actual numeric value is strictly positive (`actual > 0`).
```json
{
  "path": "$.accountBalance",
  "operator": "IS_POSITIVE",
  "description": "Account balance must be positive"
}
```

### IS_NEGATIVE
Validates that actual numeric value is strictly negative (`actual < 0`).
```json
{
  "path": "$.variance",
  "operator": "IS_NEGATIVE",
  "description": "Variance must be negative"
}
```

### IS_ZERO
Validates that actual numeric value is equal to zero (`actual == 0`).
```json
{
  "path": "$.pendingFees",
  "operator": "IS_ZERO",
  "description": "Pending fees must be zero"
}
```

### EQUALS_WITH_TOLERANCE
Validates that actual numeric value equals expected numeric value within a specified delta tolerance.
```json
{
  "path": "$.interestRate",
  "operator": "EQUALS_WITH_TOLERANCE",
  "value": {
    "expected": 5.75,
    "tolerance": 0.05
  },
  "description": "Interest rate must equal 5.75 within +/-0.05"
}
```

---

## 2. String Operators

String operators evaluate substrings, regex patterns, casing, formatting, blankness, and character classifications.

### REGEX_MATCH
Validates that string value matches the specified regular expression pattern.
```json
{
  "path": "$.productCode",
  "operator": "REGEX_MATCH",
  "value": "^PRD-\\d{4}-[A-Z]{2}$",
  "description": "Product code must match format PRD-YYYY-XX"
}
```

### STARTS_WITH
Validates that string value starts with the specified prefix (case-sensitive).
```json
{
  "path": "$.transactionId",
  "operator": "STARTS_WITH",
  "value": "TXN-",
  "description": "Transaction ID must start with TXN-"
}
```

### ENDS_WITH
Validates that string value ends with the specified suffix (case-sensitive).
```json
{
  "path": "$.documentName",
  "operator": "ENDS_WITH",
  "value": ".pdf",
  "description": "Document must end with .pdf extension"
}
```

### STRING_CONTAINS
Validates that string value contains the specified substring (case-sensitive).
```json
{
  "path": "$.message",
  "operator": "STRING_CONTAINS",
  "value": "successful",
  "description": "Message must contain 'successful'"
}
```

### STRING_CONTAINS_IGNORE_CASE
Validates that string value contains the specified substring ignoring character casing.
```json
{
  "path": "$.summary",
  "operator": "STRING_CONTAINS_IGNORE_CASE",
  "value": "completed",
  "description": "Summary must contain 'completed' regardless of casing"
}
```

### STRING_DOES_NOT_CONTAIN
Validates that string value does not contain the specified substring (case-sensitive).
```json
{
  "path": "$.logs",
  "operator": "STRING_DOES_NOT_CONTAIN",
  "value": "FATAL",
  "description": "Logs must not contain 'FATAL'"
}
```

### STRING_DOES_NOT_CONTAIN_IGNORE_CASE
Validates that string value does not contain the specified substring ignoring character casing.
```json
{
  "path": "$.responseMessage",
  "operator": "STRING_DOES_NOT_CONTAIN_IGNORE_CASE",
  "value": "unauthorized",
  "description": "Response must not contain 'unauthorized' regardless of casing"
}
```

### STRING_EQUALS_IGNORE_CASE
Validates that string value is equal to expected string ignoring character casing.
```json
{
  "path": "$.status",
  "operator": "STRING_EQUALS_IGNORE_CASE",
  "value": "active",
  "description": "Status must be ACTIVE / active"
}
```

### STRING_STARTS_WITH_IGNORE_CASE
Validates that string value starts with the specified prefix ignoring character casing.
```json
{
  "path": "$.invoiceId",
  "operator": "STRING_STARTS_WITH_IGNORE_CASE",
  "value": "inv-",
  "description": "Invoice ID must start with inv- or INV-"
}
```

### STRING_STARTS_WITH_ANY
Validates that string value starts with at least one prefix from the expected list.
```json
{
  "path": "$.policyId",
  "operator": "STRING_STARTS_WITH_ANY",
  "value": ["POL-", "REN-", "END-"],
  "description": "Policy ID must start with POL-, REN-, or END-"
}
```

### STRING_ENDS_WITH_ANY
Validates that string value ends with at least one suffix from the expected list.
```json
{
  "path": "$.fileName",
  "operator": "STRING_ENDS_WITH_ANY",
  "value": [".png", ".jpg", ".jpeg"],
  "description": "File must end with an image extension"
}
```

### STRING_ENDS_WITH_ANY_IGNORE_CASE
Validates that string value ends with at least one suffix from the expected list ignoring character casing.
```json
{
  "path": "$.fileName",
  "operator": "STRING_ENDS_WITH_ANY_IGNORE_CASE",
  "value": [".pdf", ".docx"],
  "description": "File must end with .pdf or .docx ignoring casing"
}
```

### STRING_CONTAINS_WHITESPACE
Validates that string value contains at least one whitespace character.
```json
{
  "path": "$.fullName",
  "operator": "STRING_CONTAINS_WHITESPACE",
  "description": "Full name must contain at least one space"
}
```

### STRING_EQUALS_ANY
Validates that string value equals at least one candidate string in the expected list (case-sensitive).
```json
{
  "path": "$.userRole",
  "operator": "STRING_EQUALS_ANY",
  "value": ["SUPER_ADMIN", "ADMIN", "OPERATOR"],
  "description": "User role must be one of the permitted roles"
}
```

### STRING_EQUALS_ANY_IGNORE_CASE
Validates that string value equals at least one candidate string in the expected list ignoring character casing.
```json
{
  "path": "$.stateCode",
  "operator": "STRING_EQUALS_ANY_IGNORE_CASE",
  "value": ["ny", "ca", "tx"],
  "description": "State code must match NY, CA, or TX ignoring casing"
}
```

### STRING_LENGTH_EQUALS
Validates that character length of string is exactly equal to expected integer length.
```json
{
  "path": "$.pinCode",
  "operator": "STRING_LENGTH_EQUALS",
  "value": 6,
  "description": "PIN code must be exactly 6 characters"
}
```

### STRING_LENGTH_BETWEEN
Validates that character length of string is inclusively between min and max integer bounds.
```json
{
  "path": "$.username",
  "operator": "STRING_LENGTH_BETWEEN",
  "value": {
    "min": 4,
    "max": 20
  },
  "description": "Username length must be between 4 and 20 characters"
}
```

### STRING_LENGTH_GREATER_THAN
Validates that character length of string is strictly greater than expected integer length.
```json
{
  "path": "$.passwordHash",
  "operator": "STRING_LENGTH_GREATER_THAN",
  "value": 32,
  "description": "Password hash length must be greater than 32 characters"
}
```

### STRING_LENGTH_LESS_THAN
Validates that character length of string is strictly less than expected integer length.
```json
{
  "path": "$.shortBio",
  "operator": "STRING_LENGTH_LESS_THAN",
  "value": 160,
  "description": "Short bio must be under 160 characters"
}
```

### IS_STRING_MIXED_CASE
Validates that string contains both uppercase and lowercase characters.
```json
{
  "path": "$.mixedCaseIdentifier",
  "operator": "IS_STRING_MIXED_CASE",
  "description": "Identifier must contain mixed casing"
}
```

### IS_STRING_LOWER_CASE
Validates that string contains only lowercase characters.
```json
{
  "path": "$.slug",
  "operator": "IS_STRING_LOWER_CASE",
  "description": "Slug must contain only lowercase characters"
}
```

### IS_STRING_UPPER_CASE
Validates that string contains only uppercase characters.
```json
{
  "path": "$.isoCurrencyCode",
  "operator": "IS_STRING_UPPER_CASE",
  "description": "Currency code must be uppercase"
}
```

### IS_STRING_ALPHA_NUMERIC
Validates that string contains only unicode letters or digits.
```json
{
  "path": "$.accountReference",
  "operator": "IS_STRING_ALPHA_NUMERIC",
  "description": "Account reference must contain only letters and digits"
}
```

### IS_STRING_ALPHA_SPACE
Validates that string contains only unicode letters or space characters.
```json
{
  "path": "$.applicantName",
  "operator": "IS_STRING_ALPHA_SPACE",
  "description": "Name must contain only letters and spaces"
}
```

### IS_STRING_ALPHA
Validates that string contains only unicode letters.
```json
{
  "path": "$.countryAlphaCode",
  "operator": "IS_STRING_ALPHA",
  "description": "Country code must contain only letters"
}
```

### IS_STRING_NUMERIC
Validates that string contains only numeric digits.
```json
{
  "path": "$.nationalIdDigits",
  "operator": "IS_STRING_NUMERIC",
  "description": "National ID digits must contain only numbers"
}
```

### IS_STRING_BLANK
Validates that string is `null`, empty (`""`), or contains only whitespace characters.
```json
{
  "path": "$.optionalMiddleName",
  "operator": "IS_STRING_BLANK",
  "description": "Middle name must be blank"
}
```

### IS_STRING_NOT_BLANK
Validates that string is not `null` and contains at least one non-whitespace character.
```json
{
  "path": "$.fullName",
  "operator": "IS_STRING_NOT_BLANK",
  "description": "Full name must not be blank"
}
```

### IS_STRING_EMPTY
Validates that string is `null` or empty (`""`).
```json
{
  "path": "$.errorDetail",
  "operator": "IS_STRING_EMPTY",
  "description": "Error detail must be empty"
}
```

### IS_STRING_NOT_EMPTY
Validates that string is not `null` and has length greater than 0.
```json
{
  "path": "$.token",
  "operator": "IS_STRING_NOT_EMPTY",
  "description": "Security token must not be empty"
}
```

### IS_STRING_NONE_EMPTY
Validates that none of the strings in the list/array are empty.
```json
{
  "path": "$.tags",
  "operator": "IS_STRING_NONE_EMPTY",
  "description": "All tags in the array must be non-empty"
}
```

### IS_ANY_STRING_BLANK
Validates that at least one string in the list/array is blank.
```json
{
  "path": "$.inputFormFields",
  "operator": "IS_ANY_STRING_BLANK",
  "description": "At least one form field string must be blank"
}
```

### IS_NONE_STRING_BLANK
Validates that none of the strings in the list/array are blank.
```json
{
  "path": "$.requiredParameters",
  "operator": "IS_NONE_STRING_BLANK",
  "description": "None of the required parameter strings can be blank"
}
```

### IS_ALL_STRING_BLANK
Validates that all strings in the list/array are blank.
```json
{
  "path": "$.unfilledPlaceholders",
  "operator": "IS_ALL_STRING_BLANK",
  "description": "All placeholders in the list must be blank"
}
```

### IS_UUID
Validates that string conforms to a standard UUID format (8-4-4-4-12 hexadecimal representation).
```json
{
  "path": "$.correlationId",
  "operator": "IS_UUID",
  "description": "Correlation ID must be a valid UUID"
}
```

### IS_EMAIL
Validates that string conforms to a valid email address format.
```json
{
  "path": "$.contactEmail",
  "operator": "IS_EMAIL",
  "description": "Contact email must be valid"
}
```

### IS_URL
Validates that string is a valid URI/URL format with scheme and host.
```json
{
  "path": "$.webhookUrl",
  "operator": "IS_URL",
  "description": "Webhook URL must be a valid URL"
}
```

### IS_IP_ADDRESS
Validates that string is a valid IPv4 or IPv6 address.
```json
{
  "path": "$.clientIp",
  "operator": "IS_IP_ADDRESS",
  "description": "Client IP must be a valid IPv4 or IPv6 address"
}
```

---

## 3. Date Operators

Date operators evaluate calendar dates, timestamps, components (year, month, day, hour, minute, second), relative past/future checks, and time windows.

### DATE_BEFORE
Validates that actual date is strictly before expected date (`YYYY-MM-DD`).
```json
{
  "path": "$.effectiveDate",
  "operator": "DATE_BEFORE",
  "value": "2030-01-01",
  "description": "Effective date must be before 2030-01-01"
}
```

### DATE_AFTER
Validates that actual date is strictly after expected date (`YYYY-MM-DD`).
```json
{
  "path": "$.expirationDate",
  "operator": "DATE_AFTER",
  "value": "2026-01-01",
  "description": "Expiration date must be after 2026-01-01"
}
```

### DATE_EQUALS
Validates that actual date is equal to expected date (`YYYY-MM-DD`).
```json
{
  "path": "$.settlementDate",
  "operator": "DATE_EQUALS",
  "value": "2026-09-09",
  "description": "Settlement date must equal 2026-09-09"
}
```

### DATE_YEAR_EQUALS
Validates that year component of actual date or datetime equals expected integer year.
```json
{
  "path": "$.creationDate",
  "operator": "DATE_YEAR_EQUALS",
  "value": 2026,
  "description": "Year must be 2026"
}
```

### DATE_MONTH_EQUALS
Validates that month component of actual date or datetime equals expected month (number `1-12` or name like `"SEPTEMBER"`).
```json
{
  "path": "$.creationDate",
  "operator": "DATE_MONTH_EQUALS",
  "value": "SEPTEMBER",
  "description": "Month must be September"
}
```

### DATE_DAY_EQUALS
Validates that day-of-month component of actual date or datetime equals expected integer day (`1-31`).
```json
{
  "path": "$.creationDate",
  "operator": "DATE_DAY_EQUALS",
  "value": 9,
  "description": "Day of month must be 9"
}
```

### DATE_DAY_OF_WEEK_EQUALS
Validates that day-of-week component of actual date or datetime equals expected day of week (name like `"WEDNESDAY"` or number `1-7`).
```json
{
  "path": "$.scheduledDate",
  "operator": "DATE_DAY_OF_WEEK_EQUALS",
  "value": "WEDNESDAY",
  "description": "Scheduled date must be a Wednesday"
}
```

### DATETIME_BEFORE
Validates that actual datetime timestamp is strictly before expected timestamp.
```json
{
  "path": "$.publishedAt",
  "operator": "DATETIME_BEFORE",
  "value": "2026-12-31T23:59:59Z",
  "description": "Publication timestamp must be before end of year"
}
```

### DATETIME_AFTER
Validates that actual datetime timestamp is strictly after expected timestamp.
```json
{
  "path": "$.publishedAt",
  "operator": "DATETIME_AFTER",
  "value": "2026-01-01T00:00:00Z",
  "description": "Publication timestamp must be after start of year"
}
```

### DATETIME_EQUALS
Validates that actual datetime timestamp is equal to expected timestamp.
```json
{
  "path": "$.scheduledRunTime",
  "operator": "DATETIME_EQUALS",
  "value": "2026-09-09T08:00:00Z",
  "description": "Scheduled run time must match target timestamp"
}
```

### DATETIME_EQUALS_WITH_TOLERANCE
Validates that actual datetime timestamp is within a specified duration tolerance of expected timestamp.
```json
{
  "path": "$.completedAt",
  "operator": "DATETIME_EQUALS_WITH_TOLERANCE",
  "value": {
    "expected": "2026-09-09T12:00:00Z",
    "tolerance": 5,
    "unit": "SECONDS"
  },
  "description": "Completed timestamp must be within 5 seconds of expected"
}
```

### IS_PAST_DATE
Validates that actual date is in the past relative to current system time.
```json
{
  "path": "$.dateOfBirth",
  "operator": "IS_PAST_DATE",
  "description": "Date of birth must be in the past"
}
```

### IS_FUTURE_DATE
Validates that actual date is in the future relative to current system time.
```json
{
  "path": "$.policyMaturityDate",
  "operator": "IS_FUTURE_DATE",
  "description": "Policy maturity date must be in the future"
}
```

### IS_PAST_DATETIME
Validates that actual datetime timestamp is in the past relative to current system time.
```json
{
  "path": "$.auditLogTimestamp",
  "operator": "IS_PAST_DATETIME",
  "description": "Audit log timestamp must be in the past"
}
```

### IS_FUTURE_DATETIME
Validates that actual datetime timestamp is in the future relative to current system time.
```json
{
  "path": "$.nextSyncTime",
  "operator": "IS_FUTURE_DATETIME",
  "description": "Next sync timestamp must be in the future"
}
```

### IS_TODAY
Validates that actual date represents today's calendar date relative to current system time.
```json
{
  "path": "$.processedDate",
  "operator": "IS_TODAY",
  "description": "Processed date must be today"
}
```

### DATE_BETWEEN
Validates that actual date falls inclusively between start and end date bounds.
```json
{
  "path": "$.claimIncidentDate",
  "operator": "DATE_BETWEEN",
  "value": {
    "start": "2026-01-01",
    "end": "2026-12-31"
  },
  "description": "Incident date must fall within year 2026"
}
```

### DATETIME_BETWEEN
Validates that actual datetime timestamp falls inclusively between start and end datetime bounds.
```json
{
  "path": "$.sessionStartedAt",
  "operator": "DATETIME_BETWEEN",
  "value": {
    "start": "2026-09-09T00:00:00Z",
    "end": "2026-09-09T23:59:59Z"
  },
  "description": "Session must start within the designated day window"
}
```

### DATE_WITHIN_LAST
Validates that actual date falls within the preceding ISO-8601 duration window from current system time.
```json
{
  "path": "$.lastLoginDate",
  "operator": "DATE_WITHIN_LAST",
  "value": {
    "amount": 30,
    "unit": "DAYS"
  },
  "description": "Last login date must be within the last 30 days"
}
```

### DATE_WITHIN_NEXT
Validates that actual date falls within the upcoming ISO-8601 duration window from current system time.
```json
{
  "path": "$.renewalDueDate",
  "operator": "DATE_WITHIN_NEXT",
  "value": {
    "amount": 14,
    "unit": "DAYS"
  },
  "description": "Renewal must be due within the next 14 days"
}
```

### DATETIME_WITHIN_LAST
Validates that actual datetime timestamp falls within the preceding ISO-8601 duration window from current system time.
```json
{
  "path": "$.createdAt",
  "operator": "DATETIME_WITHIN_LAST",
  "value": {
    "amount": 15,
    "unit": "MINUTES"
  },
  "description": "Record must have been created in the last 15 minutes"
}
```

### DATETIME_WITHIN_NEXT
Validates that actual datetime timestamp falls within the upcoming ISO-8601 duration window from current system time.
```json
{
  "path": "$.scheduledExecutionTime",
  "operator": "DATETIME_WITHIN_NEXT",
  "value": {
    "amount": 2,
    "unit": "HOURS"
  },
  "description": "Execution must be scheduled within the next 2 hours"
}
```

### TIME_EQUALS
Validates that time component of actual date/datetime or time equals expected time (`HH:mm` or `HH:mm:ss`).
```json
{
  "path": "$.shiftStartTime",
  "operator": "TIME_EQUALS",
  "value": "08:30:00",
  "description": "Shift start time must equal 08:30:00"
}
```

### TIME_BEFORE
Validates that time component of actual date/datetime or time is strictly before expected time.
```json
{
  "path": "$.cutoffTime",
  "operator": "TIME_BEFORE",
  "value": "17:00:00",
  "description": "Cutoff time must be before 17:00:00"
}
```

### TIME_AFTER
Validates that time component of actual date/datetime or time is strictly after expected time.
```json
{
  "path": "$.openingTime",
  "operator": "TIME_AFTER",
  "value": "06:00:00",
  "description": "Opening time must be after 06:00:00"
}
```

### TIME_BETWEEN
Validates that time component of actual date/datetime or time is inclusively between min and max time bounds.
```json
{
  "path": "$.deliveryWindowTime",
  "operator": "TIME_BETWEEN",
  "value": {
    "min": "09:00:00",
    "max": "18:00:00"
  },
  "description": "Delivery time must fall between 09:00:00 and 18:00:00"
}
```

### DATE_HOUR_EQUALS
Validates that hour component of actual date/datetime or time equals expected integer hour (`0-23`).
```json
{
  "path": "$.timestamp",
  "operator": "DATE_HOUR_EQUALS",
  "value": 14,
  "description": "Hour component must equal 14 (2 PM)"
}
```

### DATE_MINUTE_EQUALS
Validates that minute component of actual date/datetime or time equals expected integer minute (`0-59`).
```json
{
  "path": "$.timestamp",
  "operator": "DATE_MINUTE_EQUALS",
  "value": 30,
  "description": "Minute component must equal 30"
}
```

### DATE_SECOND_EQUALS
Validates that second component of actual date/datetime or time equals expected integer second (`0-59`).
```json
{
  "path": "$.timestamp",
  "operator": "DATE_SECOND_EQUALS",
  "value": 0,
  "description": "Second component must equal 0"
}
```

---

## 4. Duration Operators

Duration operators calculate temporal deltas between two timestamp nodes in a payload or verify minimum elapsed offsets.

### DURATION_BETWEEN
Validates that elapsed duration between two dates is inclusively within min and max duration bounds.
```json
{
  "operator": "DURATION_BETWEEN",
  "value": {
    "startPath": "$.jobStartedAt",
    "endPath": "$.jobCompletedAt",
    "unit": "MINUTES",
    "min": 5,
    "max": 60
  },
  "description": "Job duration must be between 5 and 60 minutes"
}
```

### DURATION_EQUALS
Validates that elapsed duration between two dates is exactly equal to expected duration.
```json
{
  "operator": "DURATION_EQUALS",
  "value": {
    "startPath": "$.subscriptionStart",
    "endPath": "$.subscriptionEnd",
    "unit": "DAYS",
    "expected": 365
  },
  "description": "Subscription duration must equal exactly 365 days"
}
```

### DURATION_GREATER_THAN
Validates that elapsed duration between two dates is strictly greater than expected duration threshold.
```json
{
  "operator": "DURATION_GREATER_THAN",
  "value": {
    "startPath": "$.sessionStart",
    "endPath": "$.sessionEnd",
    "unit": "SECONDS",
    "value": 30
  },
  "description": "Session duration must be greater than 30 seconds"
}
```

### DURATION_LESS_THAN
Validates that elapsed duration between two dates is strictly less than expected duration threshold.
```json
{
  "operator": "DURATION_LESS_THAN",
  "value": {
    "startPath": "$.requestTimestamp",
    "endPath": "$.responseTimestamp",
    "unit": "SECONDS",
    "value": 3
  },
  "description": "Response duration must be under 3 seconds"
}
```

### DATE_AFTER_DURATION
Validates that actual date is at least a specified duration after a reference date.
```json
{
  "operator": "DATE_AFTER_DURATION",
  "value": {
    "basePath": "$.startDate",
    "comparePath": "$.maturityDate",
    "amount": 30,
    "unit": "DAYS"
  },
  "description": "Maturity date must be at least 30 days after start date"
}
```

### DATE_BEFORE_DURATION
Validates that actual date is at least a specified duration before a reference date threshold.
```json
{
  "operator": "DATE_BEFORE_DURATION",
  "value": {
    "basePath": "$.targetDate",
    "comparePath": "$.earlyNoticeDate",
    "amount": 7,
    "unit": "DAYS"
  },
  "description": "Early notice date must be before target date + 7 days"
}
```

---

## 5. Structural Operators

Structural operators assert the presence, absence, and collection sizes of JSONPath targets.

### PATH_EXISTS
Validates that the specified JSONPath exists and resolves to a node in the payload.
```json
{
  "path": "$.auditHeader.traceId",
  "operator": "PATH_EXISTS",
  "description": "traceId must exist in auditHeader"
}
```

### PATH_NOT_EXISTS
Validates that the specified JSONPath does not exist or yields no matching nodes.
```json
{
  "path": "$.internalErrorStack",
  "operator": "PATH_NOT_EXISTS",
  "description": "internalErrorStack must not exist in response"
}
```

### LIST_SIZE_EQUALS
Validates that collection size is exactly equal to expected integer size.
```json
{
  "path": "$.lineItems",
  "operator": "LIST_SIZE_EQUALS",
  "value": 3,
  "description": "lineItems list must contain exactly 3 items"
}
```

### LIST_SIZE_GREATER_THAN
Validates that collection size is strictly greater than expected integer threshold.
```json
{
  "path": "$.activeUsers",
  "operator": "LIST_SIZE_GREATER_THAN",
  "value": 0,
  "description": "activeUsers list must not be empty"
}
```

### LIST_SIZE_LESS_THAN
Validates that collection size is strictly less than expected integer threshold.
```json
{
  "path": "$.notifications",
  "operator": "LIST_SIZE_LESS_THAN",
  "value": 50,
  "description": "notifications list must have fewer than 50 items"
}
```

### LIST_SIZE_BETWEEN
Validates that collection size is inclusively between min and max integer bounds.
```json
{
  "path": "$.endorsements",
  "operator": "LIST_SIZE_BETWEEN",
  "value": {
    "min": 1,
    "max": 10
  },
  "description": "Endorsements count must be between 1 and 10"
}
```

---

## 6. Array / Collection Operators

Array operators evaluate element membership, uniqueness, predicates across elements, sorting order, and subset relations.

### LIST_CONTAINS
Validates that collection contains the specified element value.
```json
{
  "path": "$.tags",
  "operator": "LIST_CONTAINS",
  "value": "ENTERPRISE",
  "description": "Tags list must contain ENTERPRISE"
}
```

### LIST_DOES_NOT_CONTAIN
Validates that collection does not contain the specified element value.
```json
{
  "path": "$.roles",
  "operator": "LIST_DOES_NOT_CONTAIN",
  "value": "SUSPENDED",
  "description": "Roles list must not contain SUSPENDED"
}
```

### LIST_CONTAINS_ONLY_VALUES
Validates that collection contains exactly the specified elements (ignoring order).
```json
{
  "path": "$.allowedStatuses",
  "operator": "LIST_CONTAINS_ONLY_VALUES",
  "value": ["ACTIVE", "PENDING", "ARCHIVED"],
  "description": "List must contain exactly ACTIVE, PENDING, and ARCHIVED"
}
```

### LIST_CONTAINS_ONLY_ONE_VALUE
Validates that collection contains exactly one occurrence of the expected element.
```json
{
  "path": "$.approvers",
  "operator": "LIST_CONTAINS_ONLY_ONE_VALUE",
  "value": "CHIEF_RISK_OFFICER",
  "description": "Approvers list must contain CHIEF_RISK_OFFICER exactly once"
}
```

### LIST_CONTAINS_OBJECT_WITH_FIELDS
Validates that collection contains at least one object matching all specified key-value field pairs.
```json
{
  "path": "$.users",
  "operator": "LIST_CONTAINS_OBJECT_WITH_FIELDS",
  "value": {
    "role": "ADMIN",
    "verified": true
  },
  "description": "Users list must contain an admin who is verified"
}
```

### LIST_CONTAINS_PARTIAL_OBJECT_WITH_FIELDS
Validates that collection contains at least one object matching all specified key-value field pairs recursively (including nested partial list/object matches).
```json
{
  "path": "$.orders",
  "operator": "LIST_CONTAINS_PARTIAL_OBJECT_WITH_FIELDS",
  "value": {
    "status": "PAID",
    "shipping": {
      "country": "ZA"
    }
  },
  "description": "Orders must contain a paid order shipped to ZA"
}
```

### ALL_MATCH
Validates that all elements in collection satisfy the specified matching condition (e.g. `greaterThan`, `lessThan`, `between`) or value.
```json
{
  "path": "$.scores",
  "operator": "ALL_MATCH",
  "value": {
    "greaterThan": 50
  },
  "description": "All scores must be greater than 50"
}
```

### ANY_MATCH
Validates that at least one element in collection satisfies the specified matching condition or value.
```json
{
  "path": "$.scores",
  "operator": "ANY_MATCH",
  "value": {
    "greaterThan": 90
  },
  "description": "At least one score must exceed 90"
}
```

### NONE_MATCH
Validates that no elements in collection satisfy the specified matching condition or value.
```json
{
  "path": "$.riskRatings",
  "operator": "NONE_MATCH",
  "value": {
    "greaterThan": 80
  },
  "description": "No risk rating should exceed 80"
}
```

### CONTAINS_ANY
Validates that collection contains at least one of the elements in the expected list.
```json
{
  "path": "$.privileges",
  "operator": "CONTAINS_ANY",
  "value": ["SUPER_ADMIN", "MANAGER", "AUDITOR"],
  "description": "Privileges must contain at least one designated role"
}
```

### CONTAINS_ALL
Validates that collection contains all elements in the expected list.
```json
{
  "path": "$.permissions",
  "operator": "CONTAINS_ALL",
  "value": ["READ", "WRITE"],
  "description": "Permissions must include both READ and WRITE"
}
```

### DOES_NOT_CONTAIN_ANY
Validates that collection contains none of the elements in the expected list.
```json
{
  "path": "$.assignedTags",
  "operator": "DOES_NOT_CONTAIN_ANY",
  "value": ["DEPRECATED", "BANNED", "BLACKLISTED"],
  "description": "Tags must contain none of the blacklisted tags"
}
```

### DOES_NOT_CONTAIN_ALL
Validates that collection does not contain all of the elements in the expected list.
```json
{
  "path": "$.flags",
  "operator": "DOES_NOT_CONTAIN_ALL",
  "value": ["LOCKOUT", "MFA_REQUIRED", "PASSWORD_EXPIRED"],
  "description": "Must not contain all three lockout flags simultaneously"
}
```

### IS_EMPTY_LIST
Validates that collection is empty (size 0).
```json
{
  "path": "$.unresolvedErrors",
  "operator": "IS_EMPTY_LIST",
  "description": "Unresolved errors list must be empty"
}
```

### IS_NOT_EMPTY_LIST
Validates that collection is not empty (size > 0).
```json
{
  "path": "$.activeContracts",
  "operator": "IS_NOT_EMPTY_LIST",
  "description": "Active contracts list must not be empty"
}
```

### UNIQUE_ELEMENTS
Validates that all elements in collection are unique (no duplicates).
```json
{
  "path": "$.serialNumbers",
  "operator": "UNIQUE_ELEMENTS",
  "description": "Serial numbers list must contain no duplicates"
}
```

### LIST_IS_SORTED_ASC
Validates that collection elements are sorted in ascending natural order.
```json
{
  "path": "$.versionNumbers",
  "operator": "LIST_IS_SORTED_ASC",
  "description": "Version numbers must be sorted in ascending order"
}
```

### LIST_IS_SORTED_DESC
Validates that collection elements are sorted in descending natural order.
```json
{
  "path": "$.rankingScores",
  "operator": "LIST_IS_SORTED_DESC",
  "description": "Ranking scores must be sorted in descending order"
}
```

### VALUE_IN
Validates that extracted scalar value is present within expected candidate list.
```json
{
  "path": "$.paymentStatus",
  "operator": "VALUE_IN",
  "value": ["SETTLED", "PROCESSING", "PENDING_CONFIRMATION"],
  "description": "Payment status must be one of the permitted states"
}
```

### VALUE_NOT_IN
Validates that extracted scalar value is not present within expected candidate list.
```json
{
  "path": "$.lifecycleState",
  "operator": "VALUE_NOT_IN",
  "value": ["TERMINATED", "REVOKED"],
  "description": "Lifecycle state must not be terminated or revoked"
}
```

---

## 7. Object Operators

Object operators evaluate JSON map structures, required key presence, map emptiness, and cross-path comparisons.

### OBJECT_CONTAINS_FIELDS
Validates that target JSON object contains all specified key-value field pairs.
```json
{
  "path": "$.customerProfile",
  "operator": "OBJECT_CONTAINS_FIELDS",
  "value": {
    "tier": "PREMIUM",
    "verified": true
  },
  "description": "Customer profile must contain matching tier and verified status"
}
```

### OBJECT_CONTAINS_PARTIAL_FIELDS
Validates that target JSON object contains all specified key-value field pairs recursively (including nested partial list/object matches).
```json
{
  "path": "$.configuration",
  "operator": "OBJECT_CONTAINS_PARTIAL_FIELDS",
  "value": {
    "database": {
      "ssl": true,
      "poolSize": 20
    }
  },
  "description": "Configuration must match nested database settings"
}
```

### OBJECT_CONTAINS_FIELDS_IGNORE_NULLS
Validates that target JSON object contains all specified expected non-null fields and values (null expected fields are ignored during comparison).
```json
{
  "path": "$.userProfile",
  "operator": "OBJECT_CONTAINS_FIELDS_IGNORE_NULLS",
  "value": {
    "username": "johndoe",
    "avatarUrl": null
  },
  "description": "User profile must match username while ignoring null avatarUrl"
}
```

### HAS_KEYS
Validates that target JSON object contains all specified key names.
```json
{
  "path": "$.responsePayload",
  "operator": "HAS_KEYS",
  "value": ["id", "timestamp", "signature"],
  "description": "Payload must have id, timestamp, and signature keys"
}
```

### DOES_NOT_HAVE_KEYS
Validates that target JSON object contains none of the specified key names.
```json
{
  "path": "$.sanitizedResponse",
  "operator": "DOES_NOT_HAVE_KEYS",
  "value": ["password", "salt", "apiKey"],
  "description": "Response must not contain sensitive credential keys"
}
```

### IS_EMPTY_OBJECT
Validates that target JSON object is an empty map (`{}`).
```json
{
  "path": "$.errorContext",
  "operator": "IS_EMPTY_OBJECT",
  "description": "Error context must be an empty object"
}
```

### IS_NOT_EMPTY_OBJECT
Validates that target JSON object is a non-empty map.
```json
{
  "path": "$.metadata",
  "operator": "IS_NOT_EMPTY_OBJECT",
  "description": "Metadata map must not be empty"
}
```

### FIELD_EQUALS_OTHER_FIELD
Validates that value at target JSONPath equals value extracted at another JSONPath in the document.
```json
{
  "operator": "FIELD_EQUALS_OTHER_FIELD",
  "value": {
    "leftPath": "$.billing.amount",
    "rightPath": "$.invoice.total"
  },
  "description": "Billing amount must match invoice total"
}
```

### FIELD_NOT_EQUALS_OTHER_FIELD
Validates that value at target JSONPath does not equal value extracted at another JSONPath in the document.
```json
{
  "operator": "FIELD_NOT_EQUALS_OTHER_FIELD",
  "value": {
    "leftPath": "$.shippingAddress.postalCode",
    "rightPath": "$.billingAddress.postalCode"
  },
  "description": "Shipping postal code must differ from billing postal code"
}
```

### FIELD_GREATER_THAN_OTHER_FIELD
Validates that numeric value at target JSONPath is strictly greater than value at another JSONPath.
```json
{
  "operator": "FIELD_GREATER_THAN_OTHER_FIELD",
  "value": {
    "leftPath": "$.creditLimit",
    "rightPath": "$.currentBalance"
  },
  "description": "Credit limit must be greater than current balance"
}
```

### FIELD_LESS_THAN_OTHER_FIELD
Validates that numeric value at target JSONPath is strictly less than value at another JSONPath.
```json
{
  "operator": "FIELD_LESS_THAN_OTHER_FIELD",
  "value": {
    "leftPath": "$.discountedTotal",
    "rightPath": "$.grossTotal"
  },
  "description": "Discounted total must be less than gross total"
}
```

---

## 8. Money Operators

Money operators handle monetary values with strict currency validation, decimal scale normalization, and delta tolerance.

### MONEY_EQUALS
Validates financial monetary value equality taking currency and scale into account.
```json
{
  "path": "$.settlementAmount",
  "operator": "MONEY_EQUALS",
  "value": {
    "amount": 2500.50,
    "currency": "ZAR"
  },
  "description": "Settlement amount must equal 2500.50 ZAR"
}
```
*Note: A plain numeric or string amount (`2500.50` or `"2500.50"`) can also be provided if currency verification is not required.*

### MONEY_EQUALS_WITH_TOLERANCE
Validates financial monetary value equality within a specified delta tolerance.
```json
{
  "path": "$.accruedInterest",
  "operator": "MONEY_EQUALS_WITH_TOLERANCE",
  "value": {
    "expected": 145.20,
    "tolerance": 0.05,
    "currency": "USD"
  },
  "description": "Accrued interest must equal 145.20 USD within +/-0.05"
}
```

### MONEY_GREATER_THAN
Validates that monetary value is strictly greater than expected monetary threshold.
```json
{
  "path": "$.coverageLimit",
  "operator": "MONEY_GREATER_THAN",
  "value": 100000.00,
  "description": "Coverage limit must be greater than 100000.00"
}
```

### MONEY_GREATER_THAN_OR_EQUALS
Validates that monetary value is greater than or equal to expected monetary threshold.
```json
{
  "path": "$.minimumDeposit",
  "operator": "MONEY_GREATER_THAN_OR_EQUALS",
  "value": 500.00,
  "description": "Minimum deposit must be at least 500.00"
}
```

### MONEY_LESS_THAN
Validates that monetary value is strictly less than expected monetary threshold.
```json
{
  "path": "$.serviceFee",
  "operator": "MONEY_LESS_THAN",
  "value": 25.00,
  "description": "Service fee must be less than 25.00"
}
```

### MONEY_LESS_THAN_OR_EQUALS
Validates that monetary value is less than or equal to expected monetary threshold.
```json
{
  "path": "$.processingCharge",
  "operator": "MONEY_LESS_THAN_OR_EQUALS",
  "value": 15.00,
  "description": "Processing charge must not exceed 15.00"
}
```

### MONEY_BETWEEN
Validates that monetary value falls inclusively between min and max monetary bounds.
```json
{
  "path": "$.approvedLoanAmount",
  "operator": "MONEY_BETWEEN",
  "value": {
    "min": 10000.00,
    "max": 50000.00
  },
  "description": "Approved loan amount must be between 10,000.00 and 50,000.00"
}
```

---

## 9. Logical Operators

Logical operators combine nested assertion rules with boolean logic (`AND`, `OR`, `NOT`). Logical assertions group multiple child assertions under an `"assertions"` list without requiring a `"value"` field.

### AND
Logical AND operator requiring all child assertion rules in the `assertions` list to pass.
```json
{
  "path": "$",
  "operator": "AND",
  "description": "Status must be APPROVED and risk score must be at least 700",
  "assertions": [
    {
      "path": "$.status",
      "value": "APPROVED"
    },
    {
      "path": "$.riskScore",
      "operator": "GREATER_THAN_OR_EQUALS",
      "value": 700
    }
  ]
}
```

### OR
Logical OR operator requiring at least one child assertion rule in the `assertions` list to pass.
```json
{
  "path": "$",
  "operator": "OR",
  "description": "User must be either an Admin or a Super User",
  "assertions": [
    {
      "path": "$.role",
      "value": "ADMIN"
    },
    {
      "path": "$.role",
      "value": "SUPER_USER"
    }
  ]
}
```

### NOT
Logical NOT operator inverting the outcome of child assertion rules (passes when the child assertion fails).
```json
{
  "path": "$",
  "operator": "NOT",
  "description": "Account status must NOT be in restricted states",
  "assertions": [
    {
      "path": "$.accountStatus",
      "operator": "VALUE_IN",
      "value": ["BLACKLISTED", "FRAUD_ALERT", "LOCKED"]
    }
  ]
}
```

