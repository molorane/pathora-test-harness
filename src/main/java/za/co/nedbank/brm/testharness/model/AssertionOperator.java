package za.co.nedbank.brm.testharness.model;

public enum AssertionOperator {

    /*
     * =========================
     * SCALAR OPERATORS
     * =========================
     */

    EQUALS,
    NOT_EQUALS,
    GREATER_THAN,
    LESS_THAN,


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

    ARRAY_CONTAINS,                    // Array contains a value
    ARRAY_CONTAINS_ONLY_VALUES,        // Array contains exactly these values (order ignored)
    ARRAY_CONTAINS_ONLY_ONE_VALUE,     // Array contains exactly one element equal to expected
    ARRAY_CONTAINS_OBJECT_WITH_FIELDS, // Array contains at least one object matching expected fields


    /*
     * =========================
     * OBJECT OPERATORS
     * =========================
     */

    OBJECT_CONTAINS_FIELDS,                // Actual object contains expected fields
    OBJECT_CONTAINS_FIELDS_IGNORE_NULLS    // Same as above but ignores null expected fields
}
