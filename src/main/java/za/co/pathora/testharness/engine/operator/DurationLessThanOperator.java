package za.co.pathora.testharness.engine.operator;

import com.jayway.jsonpath.DocumentContext;
import za.co.pathora.testharness.exception.HarnessAssertionException;
import za.co.pathora.testharness.model.AssertionOperator;
import za.co.pathora.testharness.util.AssertionUtils;

import java.util.Map;

public class DurationLessThanOperator implements DocumentContextAwareOperator {

    @Override
    public void apply(DocumentContext context, Object expected) {

        Map<String, Object> config = AssertionUtils.toMap(expected);

        String startPath = String.valueOf(config.get("startPath"));
        String endPath = String.valueOf(config.get("endPath"));
        long threshold = DurationHelper.toLong(config.get("value"));
        var unit = DurationHelper.parseUnit(String.valueOf(config.get("unit")));

        long actual = DurationHelper.calculateDuration(
                String.valueOf((Object) context.read(startPath)),
                String.valueOf((Object) context.read(endPath)),
                unit, startPath);

        if (actual >= threshold) {
            throw new HarnessAssertionException(
                    AssertionOperator.DURATION_LESS_THAN,
                    startPath + " → " + endPath,
                    "< " + threshold + " " + unit,
                    actual + " " + unit,
                    "DURATION_LESS_THAN failed. Expected < " + threshold +
                            " " + unit + " but was " + actual + " " + unit);
        }
    }
}
