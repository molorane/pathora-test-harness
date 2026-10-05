package io.github.molorane.pathora.testharness.engine.operator.duration;

import com.jayway.jsonpath.DocumentContext;
import io.github.molorane.pathora.testharness.engine.operator.DocumentContextAwareEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * Operator: {@code DATE_AFTER_DURATION}
 *
 * <p>Validates that the datetime at {@code comparePath} is at least {@code amount} {@code unit} after the datetime at {@code basePath}.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "Operator": "DATE_AFTER_DURATION",
 *   "Value": {
 *     "basePath": "$.outputData.createdDate",
 *     "comparePath": "$.outputData.expiryDate",
 *     "amount": 30,
 *     "unit": "DAYS"
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATE_AFTER_DURATION
 */
public class DateAfterDurationEvaluator implements DocumentContextAwareEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATE_AFTER_DURATION;
    }

    @Override
    public void apply(DocumentContext context, Object expected) {

        Map<String, Object> config = AssertionUtils.toMap(expected);

        String basePath = String.valueOf(config.get("basePath"));
        String comparePath = String.valueOf(config.get("comparePath"));
        long amount = DurationHelper.toLong(config.get("amount"));
        ChronoUnit unit = DurationHelper.parseUnit(String.valueOf(config.get("unit")));

        LocalDateTime baseDt = DurationHelper.parseDateTime(
                String.valueOf((Object) context.read(basePath)), basePath);
        LocalDateTime compareDt = DurationHelper.parseDateTime(
                String.valueOf((Object) context.read(comparePath)), comparePath);
        LocalDateTime threshold = baseDt.plus(amount, unit);

        if (compareDt.isBefore(threshold)) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATE_AFTER_DURATION,
                    basePath + " + " + amount + " " + unit,
                    "after " + threshold,
                    compareDt,
                    "DATE_AFTER_DURATION failed. " +
                            comparePath + " (" + compareDt + ") is not at least " +
                            amount + " " + unit + " after " +
                            basePath + " (" + baseDt + "). " +
                            "Threshold: " + threshold);
        }
    }
}
