package io.github.molorane.pathora.testharness.engine.operator.object;

import com.jayway.jsonpath.DocumentContext;
import io.github.molorane.pathora.testharness.engine.operator.DocumentContextAwareEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.Map;

/**
 * Operator: {@code FIELD_LESS_THAN_OTHER_FIELD}
 *
 * <p>Validates that the numeric value at {@code leftPath} is strictly less than the numeric value at {@code rightPath}.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "Operator": "FIELD_LESS_THAN_OTHER_FIELD",
 *   "Value": {
 *     "leftPath": "$.outputData.usedStorage",
 *     "rightPath": "$.outputData.maxStorage"
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#FIELD_LESS_THAN_OTHER_FIELD
 */
public class FieldLessThanOtherFieldEvaluator implements DocumentContextAwareEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.FIELD_LESS_THAN_OTHER_FIELD;
    }

    @Override
    public void apply(DocumentContext context, Object expected) {
        Map<String, Object> config = AssertionUtils.toMap(expected);

        String leftPath = String.valueOf(config.get("leftPath"));
        String rightPath = String.valueOf(config.get("rightPath"));

        if (leftPath == null || rightPath == null) {
            throw new IllegalArgumentException(
                "FIELD_LESS_THAN_OTHER_FIELD requires 'leftPath' and 'rightPath' in Value");
        }

        Object leftValue = context.read(leftPath);
        Object rightValue = context.read(rightPath);

        Object[] normalized = AssertionUtils.normalizeTypes(leftValue, rightValue);

        if (!(normalized[0] instanceof Number) || !(normalized[1] instanceof Number)) {
            throw new IllegalArgumentException(
                "FIELD_LESS_THAN_OTHER_FIELD requires numeric values. Left: " + leftValue + ", Right: " + rightValue);
        }

        double leftNum = ((Number) normalized[0]).doubleValue();
        double rightNum = ((Number) normalized[1]).doubleValue();

        if (leftNum >= rightNum) {
            throw new HarnessAssertionException(
                AssertionOperator.FIELD_LESS_THAN_OTHER_FIELD,
                leftPath + " vs " + rightPath,
                "< " + rightValue,
                leftValue,
                "FIELD_LESS_THAN_OTHER_FIELD failed. " +
                    leftPath + " = " + leftValue +
                    ", " + rightPath + " = " + rightValue +
                    ". Expected left to be less than right.");
        }
    }
}

