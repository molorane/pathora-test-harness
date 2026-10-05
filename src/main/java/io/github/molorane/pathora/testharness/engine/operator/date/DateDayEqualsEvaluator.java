package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.DateUtils;

import java.time.LocalDate;

/**
 * Operator: {@code DATE_DAY_EQUALS}
 *
 * <p>Validates that the day of the month (1-31) of the extracted date matches the expected day.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.statementDate",
 *   "Operator": "DATE_DAY_EQUALS",
 *   "Value": 15
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATE_DAY_EQUALS
 */
public class DateDayEqualsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATE_DAY_EQUALS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDate actualDate = DateUtils.parseDateOrDateTime(normalizedActual, path);
        int expectedDay = DateUtils.parseDayOfMonth(expected, path + " (expected)");
        int actualDay = actualDate.getDayOfMonth();

        if (actualDay != expectedDay) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATE_DAY_EQUALS,
                    path,
                    expectedDay,
                    actualDay,
                    "DATE_DAY_EQUALS failed at " + path +
                            ". Expected day: " + expectedDay +
                            ", Actual day: " + actualDay + " (from " + normalizedActual + ")");
        }
    }
}

