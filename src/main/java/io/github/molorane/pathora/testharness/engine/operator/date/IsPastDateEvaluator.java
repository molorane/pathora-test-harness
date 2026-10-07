package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.PathoraClock;

import java.time.LocalDate;

import static io.github.molorane.pathora.testharness.util.DateUtils.parseDate;

/**
 * Operator: {@code IS_PAST_DATE}
 *
 * <p>Validates that the extracted date is strictly in the past relative to current date (today).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.dateOfBirth",
 *   "Operator": "IS_PAST_DATE"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_PAST_DATE
 */
public class IsPastDateEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_PAST_DATE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDate actualDate = parseDate(String.valueOf(normalizedActual), path);
        LocalDate today = PathoraClock.today();

        if (!actualDate.isBefore(today)) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_PAST_DATE,
                path,
                "before " + today,
                actualDate,
                "IS_PAST_DATE failed at " + path +
                    ". Value " + actualDate + " is not before today (" + today + ")");
        }
    }
}

