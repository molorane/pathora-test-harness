package za.co.pathora.testharness.engine.operator;

import com.jayway.jsonpath.DocumentContext;
import za.co.pathora.testharness.exception.HarnessAssertionException;
import za.co.pathora.testharness.model.AssertionOperator;
import za.co.pathora.testharness.util.AssertionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class DateAfterDurationOperator implements DocumentContextAwareOperator {

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
