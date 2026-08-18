package io.github.molorane.pathora.testharness.model;

public enum AssertionOperator {

    /*
     * =========================
     * SCALAR OPERATORS
     * =========================
     */

    EQUALS,
    NOT_EQUALS,
    GREATER_THAN,
    GREATER_THAN_OR_EQUALS,
    LESS_THAN,
    LESS_THAN_OR_EQUALS,
    BETWEEN,

    /*
     * =========================
     * STRING OPERATORS
     * =========================
     */

    REGEX_MATCH,
    STARTS_WITH,
    ENDS_WITH,

    /*
     * =========================
     * DATE OPERATORS
     * =========================
     */

    DATE_BEFORE,
    DATE_AFTER,
    DATETIME_BEFORE,
    DATETIME_AFTER,
    DATE_BEFORE_NOW,
    DATE_AFTER_NOW,
    DATE_WITHIN_LAST,
    DATE_WITHIN_NEXT,

    /*
     * =========================
     * DURATION OPERATORS
     * =========================
     */

    DURATION_BETWEEN,
    DURATION_EQUALS,
    DURATION_GREATER_THAN,
    DURATION_LESS_THAN,
    DATE_AFTER_DURATION,
    DATE_BEFORE_DURATION,

    /*
     * =========================
     * STRUCTURAL OPERATORS
     * =========================
     */

    PATH_EXISTS,       // Path exists in the response (replaces EXISTS)
    PATH_NOT_EXISTS,   // Path is absent or filter returns no matches
    EXISTS,            // @deprecated — use PATH_EXISTS
    ARRAY_SIZE_EQUALS,

    /*
     * =========================
     * ARRAY OPERATORS
     * =========================
     */

    ARRAY_CONTAINS, // Array contains a value
    ARRAY_CONTAINS_ONLY_VALUES, // Array contains exactly these values (order ignored)
    ARRAY_CONTAINS_ONLY_ONE_VALUE, // Array contains exactly one element equal to expected
    ARRAY_CONTAINS_OBJECT_WITH_FIELDS, // Array contains at least one object matching expected fields
    ALL_MATCH, // All array elements match a condition
    CONTAINS_ANY, // Array contains at least one of the expected values
    CONTAINS_ALL, // Array contains all the expected values
    DOES_NOT_CONTAIN_ANY, // Array contains none of the expected values
    DOES_NOT_CONTAIN_ALL, // Array does not contain all the expected values
    ARRAY_IS_EMPTY, // Array is empty
    UNIQUE_ELEMENTS, // Array has no duplicate elements
    VALUE_IN, // Scalar value is in a list of expected values
    VALUE_NOT_IN, // Scalar value is not in a list of expected values

    /*
     * =========================
     * OBJECT OPERATORS
     * =========================
     */

    OBJECT_CONTAINS_FIELDS, // Actual object contains expected fields
    OBJECT_CONTAINS_FIELDS_IGNORE_NULLS, // Same as above but ignores null expected fields
    HAS_KEYS, // Object contains the specified keys (values don't matter)
    FIELD_EQUALS_OTHER_FIELD, // Two fields in the response must be equal

    /*
     * =========================
     * MONEY OPERATORS
     * =========================
     */

    MONEY_EQUALS,
    MONEY_GREATER_THAN,
    MONEY_GREATER_THAN_OR_EQUALS,
    MONEY_LESS_THAN,
    MONEY_LESS_THAN_OR_EQUALS,
    MONEY_BETWEEN,

    /*
     * =========================
     * LOGICAL OPERATORS
     * =========================
     */

    AND,
    OR,
    NOT
}
