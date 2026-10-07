package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.PathoraClock;

import java.time.LocalDate;

import static io.github.molorane.pathora.testharness.util.DateUtils.parseDate;

/**
 * Operator: {@code IS_FUTURE_DATE}
 *
 * <p>Validates that the extracted date is strictly in the future relative to current date (today).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.maturityDate",
 *   "Operator": "IS_FUTURE_DATE"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_FUTURE_DATE
 */
public class IsFutureDateEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_FUTURE_DATE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDate actualDate = parseDate(String.valueOf(normalizedActual), path);
        LocalDate today = PathoraClock.today();

        if (!actualDate.isAfter(today)) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_FUTURE_DATE,
                path,
                "after " + today,
                actualDate,
                "IS_FUTURE_DATE failed at " + path +
                    ". Value " + actualDate + " is not after today (" + today + ")");
        }
    }
}

