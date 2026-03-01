package za.co.pathora.testharness.model;

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
     * STRUCTURAL OPERATORS
     * =========================
     */

    EXISTS,
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
    CONTAINS_ALL, // Array contains all of the expected values
    ARRAY_IS_EMPTY, // Array is empty
    UNIQUE_ELEMENTS, // Array has no duplicate elements

    /*
     * =========================
     * OBJECT OPERATORS
     * =========================
     */

    OBJECT_CONTAINS_FIELDS, // Actual object contains expected fields
    OBJECT_CONTAINS_FIELDS_IGNORE_NULLS, // Same as above but ignores null expected fields
    HAS_KEYS // Object contains the specified keys (values don't matter)
}
