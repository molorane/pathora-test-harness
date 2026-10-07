package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.PathoraClock;

import java.time.LocalDate;

import static io.github.molorane.pathora.testharness.util.DateUtils.parseDate;

/**
 * Operator: {@code IS_TODAY}
 *
 * <p>Validates that the extracted date is equal to current system date (today).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.processDate",
 *   "Operator": "IS_TODAY"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_TODAY
 */
public class IsDateTodayEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_TODAY;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDate actualDate = parseDate(String.valueOf(normalizedActual), path);
        LocalDate today = PathoraClock.today();

        if (!actualDate.isEqual(today)) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_TODAY,
                path,
                "today (" + today + ")",
                actualDate,
                "IS_TODAY failed at " + path +
                    ". Expected today (" + today + "), Actual: " + actualDate);
        }
    }
}

