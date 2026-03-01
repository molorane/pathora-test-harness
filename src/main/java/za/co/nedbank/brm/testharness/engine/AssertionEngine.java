package za.co.nedbank.brm.testharness.engine;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import za.co.nedbank.brm.testharness.exception.HarnessAssertionException;
import za.co.nedbank.brm.testharness.model.AssertionOperator;
import za.co.nedbank.brm.testharness.model.JsonAssertion;
import za.co.nedbank.brm.testharness.model.RuleTestCase;
import za.co.nedbank.brm.testharness.util.FailureLogger;

import java.nio.file.Path;
import java.util.*;

import static za.co.nedbank.brm.testharness.model.AssertionOperator.*;

public class AssertionEngine {

    public void assertResponse(
            Path testFileName,
            String mutatedRequest,
            String response,
            RuleTestCase testCase) {

        var assertions = testCase.responseAssertions();
        DocumentContext context = JsonPath.parse(response);

        try {

            for (JsonAssertion assertion : assertions) {

                Object actual = null;
                boolean pathExists = true;

                try {
                    actual = context.read(assertion.jsonPath());

                } catch (com.jayway.jsonpath.PathNotFoundException e) {

                    pathExists = false;

                    if (assertion.operator() != AssertionOperator.EXISTS) {
                        throw new AssertionError(
                                """
                                        JSON_PATH_EVALUATION_FAILED
                                        -----------------------------------------
                                        JsonPath: %s
                                        Operator: %s
                                        Test File: %s
                                        Entry Point: %s

                                        Path does not exist in response.

                                        Response:
                                        %s
                                        """.formatted(
                                        assertion.jsonPath(),
                                        assertion.operator(),
                                        testFileName,
                                        testCase.entryPointName(),
                                        response),
                                e);
                    }

                } catch (Exception e) {

                    throw new AssertionError(
                            """
                                    JSON_PATH_RUNTIME_ERROR
                                    -----------------------------------------
                                    JsonPath: %s
                                    Operator: %s
                                    Test File: %s
                                    Entry Point: %s

                                    Error: %s

                                    Response:
                                    %s
                                    """.formatted(
                                    assertion.jsonPath(),
                                    assertion.operator(),
                                    testFileName,
                                    testCase.entryPointName(),
                                    e.getMessage(),
                                    response),
                            e);
                }

                applyAssertion(assertion, actual, pathExists);
            }

        } catch (AssertionError ex) {

            FailureLogger.logFailure(
                    testCase,
                    testFileName,
                    mutatedRequest,
                    response,
                    ex);

            throw ex; // VERY IMPORTANT — let JUnit fail
        }
    }

    public void applyAssertion(
            JsonAssertion assertion,
            Object actual,
            boolean pathExists) {

        String path = assertion.jsonPath();
        Object expected = assertion.value();
        AssertionOperator operator = assertion.operator();

        switch (operator) {

            /*
             * =========================
             * STRUCTURAL OPERATORS
             * =========================
             */

            case EXISTS -> {
                if (!pathExists) {
                    throw new AssertionError(
                            "Expected path to exist: " + path);
                }
            }

            case ARRAY_SIZE_EQUALS -> {

                List<?> list;

                if (actual == null) {
                    list = Collections.emptyList();
                } else if (actual instanceof List<?>) {
                    list = (List<?>) actual;
                } else {
                    throw new HarnessAssertionException(
                            ARRAY_SIZE_EQUALS,
                            path,
                            expected,
                            actual,
                            "Expected array at path " + path +
                                    " but got: " + actual);
                }

                int expectedSize = ((Number) normalizeExpected(expected)).intValue();

                if (list.size() != expectedSize) {
                    throw new HarnessAssertionException(
                            ARRAY_SIZE_EQUALS,
                            path,
                            expectedSize,
                            list.size(),
                            "ARRAY_SIZE_EQUALS failed at " + path +
                                    ". Expected size: " + expectedSize +
                                    ", Actual size: " + list.size());
                }
            }

            /*
             * =========================
             * ARRAY OPERATORS
             * =========================
             */

            case ARRAY_CONTAINS -> {
                List<?> list = requireList(actual, path);

                Object normalizedExpected = normalizeTypes(normalizeResult(expected, path), expected)[1];

                if (!list.contains(normalizedExpected)) {
                    throw new HarnessAssertionException(
                            ARRAY_CONTAINS,
                            path,
                            normalizedExpected,
                            list,
                            "ARRAY_CONTAINS failed at " + path +
                                    ". Expected array to contain: " + normalizedExpected +
                                    ", Actual: " + list);
                }
            }

            case ARRAY_CONTAINS_ONLY_VALUES -> {
                List<?> list = requireList(actual, path);
                List<?> expectedList = requireList(expected, path);

                if (list.size() != expectedList.size() ||
                        !list.containsAll(expectedList)) {

                    throw new HarnessAssertionException(
                            ARRAY_CONTAINS_ONLY_VALUES,
                            path,
                            expectedList,
                            list,
                            "ARRAY_CONTAINS_ONLY_VALUES failed at " + path +
                                    ". Expected: " + expectedList +
                                    ", Actual: " + list);
                }
            }

            case ARRAY_CONTAINS_ONLY_ONE_VALUE -> {
                List<?> list = requireList(actual, path);

                if (list.size() != 1) {
                    throw new HarnessAssertionException(
                            ARRAY_CONTAINS_ONLY_ONE_VALUE,
                            path,
                            expected,
                            list,
                            "ARRAY_CONTAINS_ONLY_ONE_VALUE failed at " + path +
                                    ". Expected exactly one element, Actual: " + list);
                }

                Object actualValue = normalizeResult(list.get(0), path);
                Object[] normalized = normalizeTypes(actualValue, expected);

                if (!Objects.equals(normalized[0], normalized[1])) {
                    throw new HarnessAssertionException(
                            ARRAY_CONTAINS_ONLY_ONE_VALUE,
                            path,
                            normalized[1],
                            normalized[0],
                            "ARRAY_CONTAINS_ONLY_ONE_VALUE failed at " + path +
                                    ". Expected: " + normalized[1] +
                                    ", Actual: " + normalized[0]);
                }
            }

            case ARRAY_CONTAINS_OBJECT_WITH_FIELDS -> {
                List<?> list = requireList(actual, path);

                boolean found = list.stream()
                        .anyMatch(item -> objectContainsFields(item, expected, false));

                if (!found) {
                    throw new HarnessAssertionException(
                            ARRAY_CONTAINS_OBJECT_WITH_FIELDS,
                            path,
                            expected,
                            list,
                            "ARRAY_CONTAINS_OBJECT_WITH_FIELDS failed at " + path +
                                    ". Expected object fields: " + expected +
                                    ", Actual: " + list);
                }
            }

            /*
             * =========================
             * OBJECT OPERATORS
             * =========================
             */

            case OBJECT_CONTAINS_FIELDS -> {
                Object normalizedActual = normalizeResult(actual, path);

                if (!objectContainsFields(normalizedActual, expected, false)) {
                    throw new HarnessAssertionException(
                            OBJECT_CONTAINS_FIELDS,
                            path,
                            expected,
                            normalizedActual,
                            "OBJECT_CONTAINS_FIELDS failed at " + path +
                                    ". Expected fields: " + expected +
                                    ", Actual: " + normalizedActual);
                }
            }

            case OBJECT_CONTAINS_FIELDS_IGNORE_NULLS -> {
                Object normalizedActual = normalizeResult(actual, path);

                if (!objectContainsFields(normalizedActual, expected, true)) {
                    throw new HarnessAssertionException(
                            OBJECT_CONTAINS_FIELDS_IGNORE_NULLS,
                            path,
                            expected,
                            normalizedActual,
                            "OBJECT_CONTAINS_FIELDS_IGNORE_NULLS failed at " + path +
                                    ". Expected fields: " + expected +
                                    ", Actual: " + normalizedActual);
                }
            }

            /*
             * =========================
             * SCALAR OPERATORS
             * =========================
             */

            case EQUALS,
                    NOT_EQUALS,
                    GREATER_THAN,
                    LESS_THAN -> {

                Object normalizedActual = normalizeResult(actual, path);
                Object[] normalized = normalizeTypes(normalizedActual, expected);

                Object finalActual = normalized[0];
                Object finalExpected = normalized[1];

                switch (operator) {

                    case EQUALS -> {
                        if (!Objects.equals(finalActual, finalExpected)) {
                            throw new HarnessAssertionException(
                                    EQUALS,
                                    path,
                                    finalExpected,
                                    finalActual,
                                    "EQUALS failed at " + path +
                                            ". Expected: " + finalExpected +
                                            ", Actual: " + finalActual);
                        }
                    }

                    case NOT_EQUALS -> {
                        if (Objects.equals(finalActual, finalExpected)) {
                            throw new HarnessAssertionException(
                                    NOT_EQUALS,
                                    path,
                                    finalExpected,
                                    finalActual,
                                    "NOT_EQUALS failed at " + path +
                                            ". Value: " + finalActual);
                        }
                    }

                    case GREATER_THAN -> {
                        double a = ((Number) finalActual).doubleValue();
                        double e = ((Number) finalExpected).doubleValue();

                        if (!(a > e)) {
                            throw new HarnessAssertionException(
                                    GREATER_THAN,
                                    path,
                                    e,
                                    a,
                                    "GREATER_THAN failed at " + path +
                                            ". Expected > " + e +
                                            ", Actual: " + a);
                        }
                    }

                    case LESS_THAN -> {
                        double a = ((Number) finalActual).doubleValue();
                        double e = ((Number) finalExpected).doubleValue();

                        if (!(a < e)) {
                            throw new HarnessAssertionException(
                                    LESS_THAN,
                                    path,
                                    e,
                                    a,
                                    "LESS_THAN failed at " + path +
                                            ". Expected < " + e +
                                            ", Actual: " + a);
                        }
                    }
                }
            }
        }
    }

    private boolean deepEquals(Object actual, Object expected) {

        if (actual == null || expected == null) {
            return actual == expected;
        }

        if (actual instanceof Map && expected instanceof Map) {
            return objectContainsFields(actual, expected, false);
        }

        if (actual instanceof List && expected instanceof List) {
            return Objects.equals(actual, expected);
        }

        Object[] normalized = normalizeTypes(actual, expected);
        return Objects.equals(normalized[0], normalized[1]);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> toMap(Object value) {
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        throw new IllegalArgumentException(
                "Expected object but got: " + value);
    }

    private boolean objectContainsFields(Object actual,
            Object expected,
            boolean ignoreNulls) {

        Map<String, Object> actualMap = toMap(actual);
        Map<String, Object> expectedMap = toMap(expected);

        for (Map.Entry<String, Object> entry : expectedMap.entrySet()) {

            String key = entry.getKey();
            Object expectedValue = entry.getValue();

            if (ignoreNulls && expectedValue == null) {
                continue;
            }

            if (!actualMap.containsKey(key)) {
                return false;
            }

            Object actualValue = actualMap.get(key);

            if (!deepEquals(actualValue, expectedValue)) {
                return false;
            }
        }

        return true;
    }

    private Object[] normalizeTypes(Object actual, Object expected) {

        if (actual == null || expected == null) {
            return new Object[] { actual, expected };
        }

        if (actual instanceof Number && expected instanceof Number) {
            return new Object[] {
                    ((Number) actual).doubleValue(),
                    ((Number) expected).doubleValue()
            };
        }

        if (actual instanceof Number && expected instanceof String str) {
            try {
                return new Object[] {
                        ((Number) actual).doubleValue(),
                        Double.parseDouble(str)
                };
            } catch (NumberFormatException ignored) {
            }
        }

        if (expected instanceof Number && actual instanceof String str) {
            try {
                return new Object[] {
                        Double.parseDouble(str),
                        ((Number) expected).doubleValue()
                };
            } catch (NumberFormatException ignored) {
            }
        }

        return new Object[] { actual, expected };
    }

    private Object normalizeResult(Object result, String path) {
        if (result instanceof List<?> list) {

            if (list.isEmpty()) {
                throw new AssertionError(
                        "No match found for path: " + path);
            }

            if (list.size() > 1) {
                throw new AssertionError(
                        "Multiple matches found for path: " + path);
            }

            return list.get(0);
        }

        return result;
    }

    private List<?> requireList(Object value, String path) {
        if (!(value instanceof List<?> list)) {
            throw new AssertionError(
                    "Expected array at path " + path +
                            " but got: " + value);
        }
        return list;
    }

    private Object normalizeExpected(Object expected) {

        if (expected == null) {
            return null;
        }

        if (!(expected instanceof String str)) {
            return expected;
        }

        str = str.trim();

        // null literal
        if ("null".equalsIgnoreCase(str)) {
            return null;
        }

        // boolean
        if ("true".equalsIgnoreCase(str)) {
            return true;
        }
        if ("false".equalsIgnoreCase(str)) {
            return false;
        }

        // integer
        try {
            if (str.matches("-?\\d+")) {
                return Integer.parseInt(str);
            }
        } catch (NumberFormatException ignored) {
        }

        // decimal
        try {
            if (str.matches("-?\\d+\\.\\d+")) {
                return Double.parseDouble(str);
            }
        } catch (NumberFormatException ignored) {
        }

        // fallback → keep as string
        return str;
    }
}