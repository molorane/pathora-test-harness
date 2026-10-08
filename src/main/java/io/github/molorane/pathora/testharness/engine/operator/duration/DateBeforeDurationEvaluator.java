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
 * Operator: {@code DATE_BEFORE_DURATION}
 *
 * <p>Validates that the datetime at {@code comparePath} is before {@code basePath} plus {@code amount} {@code unit}.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "Operator": "DATE_BEFORE_DURATION",
 *   "Value": {
 *     "basePath": "$.outputData.submissionDate",
 *     "comparePath": "$.outputData.approvalDate",
 *     "amount": 7,
 *     "unit": "DAYS"
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATE_BEFORE_DURATION
 */
public class DateBeforeDurationEvaluator implements DocumentContextAwareEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATE_BEFORE_DURATION;
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

        if (!compareDt.isBefore(threshold)) {
            throw new HarnessAssertionException(
                AssertionOperator.DATE_BEFORE_DURATION,
                basePath + " + " + amount + " " + unit,
                "before " + threshold,
                compareDt,
                "DATE_BEFORE_DURATION failed. " +
                    comparePath + " (" + compareDt + ") is not before " +
                    basePath + " (" + baseDt + ") + " +
                    amount + " " + unit + " (threshold: " + threshold + ")");
        }
    }
}
