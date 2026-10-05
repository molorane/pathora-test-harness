package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.engine.operator.duration.DurationHelper;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static io.github.molorane.pathora.testharness.util.DateUtils.parseDate;

/**
 * Operator: {@code DATE_WITHIN_NEXT}
 *
 * <p>Validates that the extracted date falls within the next specified temporal interval relative to current date (today).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.dueDate",
 *   "Operator": "DATE_WITHIN_NEXT",
 *   "Value": {
 *     "amount": 14,
 *     "unit": "DAYS"
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATE_WITHIN_NEXT
 */
public class DateWithinNextEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATE_WITHIN_NEXT;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        Map<String, Object> config = AssertionUtils.toMap(expected);

        long amount = DurationHelper.toLong(config.get("amount"));
        ChronoUnit unit = DurationHelper.parseUnit(String.valueOf(config.get("unit")));

        LocalDate today = LocalDate.now();
        LocalDate threshold = today.plus(amount, unit);
        LocalDate actualDate = parseDate(String.valueOf(normalizedActual), path);

        if (actualDate.isBefore(today) || actualDate.isAfter(threshold)) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATE_WITHIN_NEXT,
                    path,
                    "within next " + amount + " " + unit,
                    actualDate,
                    "DATE_WITHIN_NEXT failed at " + path +
                            ". Value " + actualDate +
                            " is not within the next " + amount + " " + unit +
                            " (window: " + today + " to " + threshold + ")");
        }
    }
}

