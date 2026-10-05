package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDate;
import java.util.Map;

import static io.github.molorane.pathora.testharness.util.DateUtils.parseDate;

/**
 * Operator: {@code DATE_BETWEEN}
 *
 * <p>Validates that the extracted date is chronologically between the specified {@code startDate} and {@code endDate} (inclusive).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.effectiveDate",
 *   "Operator": "DATE_BETWEEN",
 *   "Value": {
 *     "startDate": "2025-01-01",
 *     "endDate": "2025-12-31"
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATE_BETWEEN
 */
public class DateBetweenEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATE_BETWEEN;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDate actualDate = parseDate(String.valueOf(normalizedActual), path);

        Map<String, Object> range = AssertionUtils.toMap(expected);
        Object minObj = range.get("min");
        Object maxObj = range.get("max");

        if (minObj == null || maxObj == null) {
            throw new IllegalArgumentException(
                    "DATE_BETWEEN operator requires 'min' and 'max' in Value at " + path);
        }

        LocalDate minDate = parseDate(String.valueOf(minObj), path + " (min)");
        LocalDate maxDate = parseDate(String.valueOf(maxObj), path + " (max)");

        if (actualDate.isBefore(minDate) || actualDate.isAfter(maxDate)) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATE_BETWEEN,
                    path,
                    "date between " + minDate + " and " + maxDate,
                    actualDate,
                    "DATE_BETWEEN failed at " + path +
                            ". Expected date between " + minDate + " and " + maxDate +
                            ", Actual: " + actualDate);
        }
    }
}

