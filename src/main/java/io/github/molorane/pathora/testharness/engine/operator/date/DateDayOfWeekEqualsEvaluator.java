package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.DateUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Operator: {@code DATE_DAY_OF_WEEK_EQUALS}
 *
 * <p>Validates that the day of the week (1=Monday ... 7=Sunday or "MONDAY", "Mon") of the extracted date matches expected.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.executionDate",
 *   "Operator": "DATE_DAY_OF_WEEK_EQUALS",
 *   "Value": "FRIDAY"
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATE_DAY_OF_WEEK_EQUALS
 */
public class DateDayOfWeekEqualsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATE_DAY_OF_WEEK_EQUALS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDate actualDate = DateUtils.parseDateOrDateTime(normalizedActual, path);
        DayOfWeek expectedDow = DateUtils.parseDayOfWeek(expected, path + " (expected)");
        DayOfWeek actualDow = actualDate.getDayOfWeek();

        if (actualDow != expectedDow) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATE_DAY_OF_WEEK_EQUALS,
                    path,
                    expected,
                    actualDow.name(),
                    "DATE_DAY_OF_WEEK_EQUALS failed at " + path +
                            ". Expected day of week: " + expected + " (" + expectedDow.name() + ")" +
                            ", Actual day of week: " + actualDow.name() + " from " + normalizedActual);
        }
    }
}

