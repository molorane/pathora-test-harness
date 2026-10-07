package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.DateUtils;

import java.time.LocalDate;

/**
 * Operator: {@code DATE_YEAR_EQUALS}
 *
 * <p>Validates that the year component (e.g. 2025) of the extracted date matches expected.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.taxYear",
 *   "Operator": "DATE_YEAR_EQUALS",
 *   "Value": 2025
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATE_YEAR_EQUALS
 */
public class DateYearEqualsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATE_YEAR_EQUALS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDate actualDate = DateUtils.parseDateOrDateTime(normalizedActual, path);
        int expectedYear = DateUtils.parseYear(expected, path + " (expected)");
        int actualYear = actualDate.getYear();

        if (actualYear != expectedYear) {
            throw new HarnessAssertionException(
                AssertionOperator.DATE_YEAR_EQUALS,
                path,
                expectedYear,
                actualYear,
                "DATE_YEAR_EQUALS failed at " + path +
                    ". Expected year: " + expectedYear +
                    ", Actual year: " + actualYear + " (from " + normalizedActual + ")");
        }
    }
}

