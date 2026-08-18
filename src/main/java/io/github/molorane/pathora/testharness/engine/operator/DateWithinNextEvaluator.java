package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

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

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.plus(amount, unit);
        LocalDateTime actualDt = DurationHelper.parseDateTime(String.valueOf(normalizedActual), path);

        if (actualDt.isBefore(now) || actualDt.isAfter(threshold)) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATE_WITHIN_NEXT,
                    path,
                    "within next " + amount + " " + unit,
                    actualDt,
                    "DATE_WITHIN_NEXT failed at " + path +
                            ". Value " + actualDt +
                            " is not within the next " + amount + " " + unit +
                            " (window: " + now + " to " + threshold + ")");
        }
    }
}
