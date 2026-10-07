package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.DateUtils;

import java.time.LocalDate;
import java.time.Month;

/**
 * Operator: {@code DATE_MONTH_EQUALS}
 *
 * <p>Validates that the month component (1-12 or month name) of the extracted date matches the expected month.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.billingDate",
 *   "Operator": "DATE_MONTH_EQUALS",
 *   "Value": "JUNE"
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATE_MONTH_EQUALS
 */
public class DateMonthEqualsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATE_MONTH_EQUALS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDate actualDate = DateUtils.parseDateOrDateTime(normalizedActual, path);
        int expectedMonth = DateUtils.parseMonth(expected, path + " (expected)");
        int actualMonth = actualDate.getMonthValue();

        if (actualMonth != expectedMonth) {
            Month expMonthEnum = Month.of(expectedMonth);
            Month actMonthEnum = actualDate.getMonth();
            throw new HarnessAssertionException(
                AssertionOperator.DATE_MONTH_EQUALS,
                path,
                expected,
                actMonthEnum.name(),
                "DATE_MONTH_EQUALS failed at " + path +
                    ". Expected month: " + expected + " (" + expMonthEnum.name() + ")" +
                    ", Actual month: " + actMonthEnum.name() + " (" + actualMonth + ") from " + normalizedActual);
        }
    }
}

