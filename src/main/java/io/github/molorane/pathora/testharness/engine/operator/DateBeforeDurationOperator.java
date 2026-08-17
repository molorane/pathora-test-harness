package io.github.molorane.pathora.testharness.engine.operator;

import com.jayway.jsonpath.DocumentContext;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class DateBeforeDurationOperator implements DocumentContextAwareOperator {

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
