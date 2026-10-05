package io.github.molorane.pathora.testharness.model;

/**
 * Enumeration of all assertion operators supported by the Pathora Test Harness engine.
 * <p>
 * Operators are grouped into distinct functional domains:
 * <ul>
 *     <li><b>Scalar Operators</b> - Numeric, boolean, and nullability assertions.</li>
 *     <li><b>String Operators</b> - String content, regex, casing, format, and emptiness checks.</li>
 *     <li><b>Date &amp; Datetime Operators</b> - Chronological order, equality, relative time windows, and date ranges.</li>
 *     <li><b>Duration Operators</b> - Temporal delta and elapsed duration between timestamps.</li>
 *     <li><b>Structural Operators</b> - JSONPath presence, absence, and collection length checks.</li>
 *     <li><b>Array / Collection Operators</b> - Membership, element matching, unique constraints, and sorting.</li>
 *     <li><b>Object Operators</b> - Field matching, key presence, map emptiness, and cross-field comparisons.</li>
 *     <li><b>Money Operators</b> - Currency and monetary value comparisons with scale and tolerance.</li>
 *     <li><b>Logical Operators</b> - Composite boolean logic (AND, OR, NOT) for grouping rules.</li>
 * </ul>
 */
public enum AssertionOperator {

    /*
     * =========================
     * SCALAR OPERATORS
     * =========================
     */

    /**
     * Validates strict equality between extracted actual value and expected value.
     */
    EQUALS,

    /**
     * Validates that actual value does not equal expected value.
     */
    NOT_EQUALS,

    /**
     * Validates that actual numeric value is strictly greater than expected threshold ({@code actual > expected}).
     */
    GREATER_THAN,

    /**
     * Validates that actual numeric value is greater than or equal to expected threshold ({@code actual >= expected}).
     */
    GREATER_THAN_OR_EQUALS,

    /**
     * Validates that actual numeric value is strictly less than expected threshold ({@code actual < expected}).
     */
    LESS_THAN,

    /**
     * Validates that actual numeric value is less than or equal to expected threshold ({@code actual <= expected}).
     */
    LESS_THAN_OR_EQUALS,

    /**
     * Validates that actual numeric value is inclusively between min and max bounds ({@code min <= actual <= max}).
     */
    BETWEEN,

    /**
     * Validates that actual value is {@code null}.
     */
    IS_NULL,

    /**
     * Validates that actual value is not {@code null}.
     */
    IS_NOT_NULL,

    /**
     * Validates that actual value is a boolean {@code true} (or string {@code "true"}).
     */
    IS_TRUE,

    /**
     * Validates that actual value is a boolean {@code false} (or string {@code "false"}).
     */
    IS_FALSE,

    /**
     * Validates that actual value represents a valid numeric number.
     */
    IS_NUMBER,

    /**
     * Validates that actual value represents a whole number/integer with no fractional part.
     */
    IS_INTEGER,

    /**
     * Validates that actual value represents a decimal number with a fractional component.
     */
    IS_DECIMAL,

    /**
     * Validates that actual numeric value is strictly positive ({@code actual > 0}).
     */
    IS_POSITIVE,

    /**
     * Validates that actual numeric value is strictly negative ({@code actual < 0}).
     */
    IS_NEGATIVE,

    /**
     * Validates that actual numeric value is equal to zero ({@code actual == 0}).
     */
    IS_ZERO,

    /**
     * Validates that actual numeric value equals expected numeric value within a specified delta tolerance.
     */
    EQUALS_WITH_TOLERANCE,

    /*
     * =========================
     * STRING OPERATORS
     * =========================
     */

    /**
     * Validates that string value matches the specified regular expression pattern.
     */
    REGEX_MATCH,

    /**
     * Validates that string value starts with the specified prefix (case-sensitive).
     */
    STARTS_WITH,

    /**
     * Validates that string value ends with the specified suffix (case-sensitive).
     */
    ENDS_WITH,

    /**
     * Validates that string value contains the specified substring (case-sensitive).
     */
    STRING_CONTAINS,

    /**
     * Validates that string value contains the specified substring ignoring character casing.
     */
    STRING_CONTAINS_IGNORE_CASE,

    /**
     * Validates that string value does not contain the specified substring (case-sensitive).
     */
    STRING_DOES_NOT_CONTAIN,

    /**
     * Validates that string value does not contain the specified substring ignoring character casing.
     */
    STRING_DOES_NOT_CONTAIN_IGNORE_CASE,

    /**
     * Validates that string value is equal to expected string ignoring character casing.
     */
    STRING_EQUALS_IGNORE_CASE,

    /**
     * Validates that string value starts with the specified prefix ignoring character casing.
     */
    STRING_STARTS_WITH_IGNORE_CASE,

    /**
     * Validates that string value starts with at least one prefix from the expected list.
     */
    STRING_STARTS_WITH_ANY,

    /**
     * Validates that string value ends with at least one suffix from the expected list.
     */
    STRING_ENDS_WITH_ANY,

    /**
     * Validates that string value ends with at least one suffix from the expected list ignoring character casing.
     */
    STRING_ENDS_WITH_ANY_IGNORE_CASE,

    /**
     * Validates that string value contains at least one whitespace character.
     */
    STRING_CONTAINS_WHITESPACE,

    /**
     * Validates that string value equals at least one candidate string in the expected list (case-sensitive).
     */
    STRING_EQUALS_ANY,

    /**
     * Validates that string value equals at least one candidate string in the expected list ignoring character casing.
     */
    STRING_EQUALS_ANY_IGNORE_CASE,

    /**
     * Validates that character length of string is exactly equal to expected integer length.
     */
    STRING_LENGTH_EQUALS,

    /**
     * Validates that character length of string is inclusively between min and max integer bounds.
     */
    STRING_LENGTH_BETWEEN,

    /**
     * Validates that character length of string is strictly greater than expected integer length.
     */
    STRING_LENGTH_GREATER_THAN,

    /**
     * Validates that character length of string is strictly less than expected integer length.
     */
    STRING_LENGTH_LESS_THAN,

    /**
     * Validates that string contains both uppercase and lowercase characters.
     */
    IS_STRING_MIXED_CASE,

    /**
     * Validates that string contains only lowercase characters.
     */
    IS_STRING_LOWER_CASE,

    /**
     * Validates that string contains only uppercase characters.
     */
    IS_STRING_UPPER_CASE,

    /**
     * Validates that string contains only unicode letters or digits.
     */
    IS_STRING_ALPHA_NUMERIC,

    /**
     * Validates that string contains only unicode letters or space characters.
     */
    IS_STRING_ALPHA_SPACE,

    /**
     * Validates that string contains only unicode letters.
     */
    IS_STRING_ALPHA,

    /**
     * Validates that string contains only numeric digits.
     */
    IS_STRING_NUMERIC,

    /**
     * Validates that string is {@code null}, empty ({@code ""}), or contains only whitespace characters.
     */
    IS_STRING_BLANK,

    /**
     * Validates that string is not {@code null} and contains at least one non-whitespace character.
     */
    IS_STRING_NOT_BLANK,

    /**
     * Validates that string is {@code null} or empty ({@code ""}).
     */
    IS_STRING_EMPTY,

    /**
     * Validates that string is not {@code null} and has length greater than 0.
     */
    IS_STRING_NOT_EMPTY,

    /**
     * Validates that none of the strings in the list/array are empty.
     */
    IS_STRING_NONE_EMPTY,

    /**
     * Validates that at least one string in the list/array is blank.
     */
    IS_ANY_STRING_BLANK,

    /**
     * Validates that none of the strings in the list/array are blank.
     */
    IS_NONE_STRING_BLANK,

    /**
     * Validates that all strings in the list/array are blank.
     */
    IS_ALL_STRING_BLANK,

    /**
     * Validates that string conforms to a standard UUID format (8-4-4-4-12 hexadecimal representation).
     */
    IS_UUID,

    /**
     * Validates that string conforms to a valid email address format.
     */
    IS_EMAIL,

    /**
     * Validates that string is a valid URI/URL format with scheme and host.
     */
    IS_URL,

    /**
     * Validates that string is a valid IPv4 or IPv6 address.
     */
    IS_IP_ADDRESS,

    /*
     * =========================
     * DATE OPERATORS
     * =========================
     */

    /**
     * Validates that actual date is strictly before expected date.
     */
    DATE_BEFORE,

    /**
     * Validates that actual date is strictly after expected date.
     */
    DATE_AFTER,

    /**
     * Validates that actual date is equal to expected date.
     */
    DATE_EQUALS,

    /**
     * Validates that year component of actual date or datetime equals expected integer year.
     */
    DATE_YEAR_EQUALS,

    /**
     * Validates that month component of actual date or datetime equals expected month (number or name).
     */
    DATE_MONTH_EQUALS,

    /**
     * Validates that day-of-month component of actual date or datetime equals expected integer day (1-31).
     */
    DATE_DAY_EQUALS,

    /**
     * Validates that day-of-week component of actual date or datetime equals expected day of week (name or 1-7).
     */
    DATE_DAY_OF_WEEK_EQUALS,


    /**
     * Validates that actual datetime timestamp is strictly before expected timestamp.
     */
    DATETIME_BEFORE,

    /**
     * Validates that actual datetime timestamp is strictly after expected timestamp.
     */
    DATETIME_AFTER,

    /**
     * Validates that actual datetime timestamp is equal to expected timestamp.
     */
    DATETIME_EQUALS,

    /**
     * Validates that actual datetime timestamp is within a specified duration tolerance of expected timestamp.
     */
    DATETIME_EQUALS_WITH_TOLERANCE,

    /**
     * Validates that actual date is in the past relative to current system time.
     */
    IS_PAST_DATE,

    /**
     * Validates that actual date is in the future relative to current system time.
     */
    IS_FUTURE_DATE,

    /**
     * Validates that actual datetime timestamp is in the past relative to current system time.
     */
    IS_PAST_DATETIME,

    /**
     * Validates that actual datetime timestamp is in the future relative to current system time.
     */
    IS_FUTURE_DATETIME,

    /**
     * Validates that actual date represents today's calendar date relative to current system time.
     */
    IS_TODAY,

    /**
     * Validates that actual date falls inclusively between start and end date bounds.
     */
    DATE_BETWEEN,

    /**
     * Validates that actual datetime timestamp falls inclusively between start and end datetime bounds.
     */
    DATETIME_BETWEEN,

    /**
     * Validates that actual date falls within the preceding ISO-8601 duration window from current system time.
     */
    DATE_WITHIN_LAST,

    /**
     * Validates that actual date falls within the upcoming ISO-8601 duration window from current system time.
     */
    DATE_WITHIN_NEXT,

    /**
     * Validates that actual datetime timestamp falls within the preceding ISO-8601 duration window from current system time.
     */
    DATETIME_WITHIN_LAST,

    /**
     * Validates that actual datetime timestamp falls within the upcoming ISO-8601 duration window from current system time.
     */
    DATETIME_WITHIN_NEXT,

    /**
     * Validates that time component of actual date/datetime or time equals expected time (HH:mm or HH:mm:ss).
     */
    TIME_EQUALS,

    /**
     * Validates that time component of actual date/datetime or time is strictly before expected time.
     */
    TIME_BEFORE,

    /**
     * Validates that time component of actual date/datetime or time is strictly after expected time.
     */
    TIME_AFTER,

    /**
     * Validates that time component of actual date/datetime or time is inclusively between min and max time bounds.
     */
    TIME_BETWEEN,

    /**
     * Validates that hour component of actual date/datetime or time equals expected integer hour (0-23).
     */
    DATE_HOUR_EQUALS,

    /**
     * Validates that minute component of actual date/datetime or time equals expected integer minute (0-59).
     */
    DATE_MINUTE_EQUALS,

    /**
     * Validates that second component of actual date/datetime or time equals expected integer second (0-59).
     */
    DATE_SECOND_EQUALS,

    /*
     * =========================
     * DURATION OPERATORS
     * =========================
     */

    /**
     * Validates that elapsed duration between two dates is inclusively within min and max duration bounds.
     */
    DURATION_BETWEEN,

    /**
     * Validates that elapsed duration between two dates is exactly equal to expected duration.
     */
    DURATION_EQUALS,

    /**
     * Validates that elapsed duration between two dates is strictly greater than expected duration threshold.
     */
    DURATION_GREATER_THAN,

    /**
     * Validates that elapsed duration between two dates is strictly less than expected duration threshold.
     */
    DURATION_LESS_THAN,

    /**
     * Validates that actual date is at least a specified duration after a reference date.
     */
    DATE_AFTER_DURATION,

    /**
     * Validates that actual date is at least a specified duration before a reference date.
     */
    DATE_BEFORE_DURATION,

    /*
     * =========================
     * STRUCTURAL OPERATORS
     * =========================
     */

    /**
     * Validates that the specified JSONPath exists and resolves to a node in the payload.
     */
    PATH_EXISTS,

    /**
     * Validates that the specified JSONPath does not exist or yields no matching nodes.
     */
    PATH_NOT_EXISTS,

    /**
     * Validates that collection size is exactly equal to expected integer size.
     */
    LIST_SIZE_EQUALS,

    /**
     * Validates that collection size is strictly greater than expected integer threshold.
     */
    LIST_SIZE_GREATER_THAN,

    /**
     * Validates that collection size is strictly less than expected integer threshold.
     */
    LIST_SIZE_LESS_THAN,

    /**
     * Validates that collection size is inclusively between min and max integer bounds.
     */
    LIST_SIZE_BETWEEN,

    /*
     * =========================
     * ARRAY OPERATORS
     * =========================
     */

    /**
     * Validates that collection contains the specified element value.
     */
    LIST_CONTAINS,

    /**
     * Validates that collection does not contain the specified element value.
     */
    LIST_DOES_NOT_CONTAIN,

    /**
     * Validates that collection contains exactly the specified elements (ignoring order).
     */
    LIST_CONTAINS_ONLY_VALUES,

    /**
     * Validates that collection contains exactly one occurrence of the expected element.
     */
    LIST_CONTAINS_ONLY_ONE_VALUE,

    /**
     * Validates that collection contains at least one object matching all specified key-value field pairs.
     */
    LIST_CONTAINS_OBJECT_WITH_FIELDS,

    /**
     * Validates that collection contains at least one object matching all specified key-value field pairs recursively (including nested partial list/object matches).
     */
    LIST_CONTAINS_PARTIAL_OBJECT_WITH_FIELDS,

    /**
     * Validates that all elements in collection satisfy the specified matching condition or value.
     */
    ALL_MATCH,

    /**
     * Validates that at least one element in collection satisfies the specified matching condition or value.
     */
    ANY_MATCH,

    /**
     * Validates that no elements in collection satisfy the specified matching condition or value.
     */
    NONE_MATCH,

    /**
     * Validates that collection contains at least one of the elements in the expected list.
     */
    CONTAINS_ANY,

    /**
     * Validates that collection contains all elements in the expected list.
     */
    CONTAINS_ALL,

    /**
     * Validates that collection contains none of the elements in the expected list.
     */
    DOES_NOT_CONTAIN_ANY,

    /**
     * Validates that collection does not contain all of the elements in the expected list.
     */
    DOES_NOT_CONTAIN_ALL,

    /**
     * Validates that collection is empty (size 0).
     */
    IS_EMPTY_LIST,

    /**
     * Validates that collection is not empty (size &gt; 0).
     */
    IS_NOT_EMPTY_LIST,

    /**
     * Validates that all elements in collection are unique (no duplicates).
     */
    UNIQUE_ELEMENTS,

    /**
     * Validates that collection elements are sorted in ascending natural order.
     */
    LIST_IS_SORTED_ASC,

    /**
     * Validates that collection elements are sorted in descending natural order.
     */
    LIST_IS_SORTED_DESC,

    /**
     * Validates that extracted scalar value is present within expected candidate list.
     */
    VALUE_IN,

    /**
     * Validates that extracted scalar value is not present within expected candidate list.
     */
    VALUE_NOT_IN,

    /*
     * =========================
     * OBJECT OPERATORS
     * =========================
     */

    /**
     * Validates that target JSON object contains all specified key-value field pairs.
     */
    OBJECT_CONTAINS_FIELDS,

    /**
     * Validates that target JSON object contains all specified key-value field pairs recursively (including nested partial list/object matches).
     */
    OBJECT_CONTAINS_PARTIAL_FIELDS,

    /**
     * Validates that target JSON object contains all specified expected non-null fields and values.
     */
    OBJECT_CONTAINS_FIELDS_IGNORE_NULLS,

    /**
     * Validates that target JSON object contains all specified key names.
     */
    HAS_KEYS,

    /**
     * Validates that target JSON object contains none of the specified key names.
     */
    DOES_NOT_HAVE_KEYS,

    /**
     * Validates that target JSON object is an empty map ({@code {}}).
     */
    IS_EMPTY_OBJECT,

    /**
     * Validates that target JSON object is a non-empty map.
     */
    IS_NOT_EMPTY_OBJECT,

    /**
     * Validates that value at target JSONPath equals value extracted at another JSONPath in the document.
     */
    FIELD_EQUALS_OTHER_FIELD,

    /**
     * Validates that value at target JSONPath does not equal value extracted at another JSONPath in the document.
     */
    FIELD_NOT_EQUALS_OTHER_FIELD,

    /**
     * Validates that numeric value at target JSONPath is strictly greater than value at another JSONPath.
     */
    FIELD_GREATER_THAN_OTHER_FIELD,

    /**
     * Validates that numeric value at target JSONPath is strictly less than value at another JSONPath.
     */
    FIELD_LESS_THAN_OTHER_FIELD,

    /*
     * =========================
     * MONEY OPERATORS
     * =========================
     */

    /**
     * Validates financial monetary value equality taking currency and scale into account.
     */
    MONEY_EQUALS,

    /**
     * Validates financial monetary value equality within a specified delta tolerance.
     */
    MONEY_EQUALS_WITH_TOLERANCE,

    /**
     * Validates that monetary value is strictly greater than expected monetary threshold.
     */
    MONEY_GREATER_THAN,

    /**
     * Validates that monetary value is greater than or equal to expected monetary threshold.
     */
    MONEY_GREATER_THAN_OR_EQUALS,

    /**
     * Validates that monetary value is strictly less than expected monetary threshold.
     */
    MONEY_LESS_THAN,

    /**
     * Validates that monetary value is less than or equal to expected monetary threshold.
     */
    MONEY_LESS_THAN_OR_EQUALS,

    /**
     * Validates that monetary value falls inclusively between min and max monetary bounds.
     */
    MONEY_BETWEEN,

    /*
     * =========================
     * LOGICAL OPERATORS
     * =========================
     */

    /**
     * Logical AND operator requiring all child assertion rules to pass.
     */
    AND,

    /**
     * Logical OR operator requiring at least one child assertion rule to pass.
     */
    OR,

    /**
     * Logical NOT operator inverting the outcome of child assertion rule.
     */
    NOT
}


