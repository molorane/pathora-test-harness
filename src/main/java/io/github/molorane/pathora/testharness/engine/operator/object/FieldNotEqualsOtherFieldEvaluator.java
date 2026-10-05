package io.github.molorane.pathora.testharness.engine.operator.object;

import com.jayway.jsonpath.DocumentContext;
import io.github.molorane.pathora.testharness.engine.operator.DocumentContextAwareEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.Map;
import java.util.Objects;

/**
 * Operator: {@code FIELD_NOT_EQUALS_OTHER_FIELD}
 *
 * <p>Validates that the value at {@code leftPath} does not equal the value at {@code rightPath}.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "Operator": "FIELD_NOT_EQUALS_OTHER_FIELD",
 *   "Value": {
 *     "leftPath": "$.outputData.sourceAccount",
 *     "rightPath": "$.outputData.destinationAccount"
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#FIELD_NOT_EQUALS_OTHER_FIELD
 */
public class FieldNotEqualsOtherFieldEvaluator implements DocumentContextAwareEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.FIELD_NOT_EQUALS_OTHER_FIELD;
    }

    @Override
    public void apply(DocumentContext context, Object expected) {
        Map<String, Object> config = AssertionUtils.toMap(expected);

        String leftPath = String.valueOf(config.get("leftPath"));
        String rightPath = String.valueOf(config.get("rightPath"));

        if (leftPath == null || rightPath == null) {
            throw new IllegalArgumentException(
                    "FIELD_NOT_EQUALS_OTHER_FIELD requires 'leftPath' and 'rightPath' in Value");
        }

        Object leftValue = context.read(leftPath);
        Object rightValue = context.read(rightPath);

        Object[] normalized = AssertionUtils.normalizeTypes(leftValue, rightValue);

        if (Objects.equals(normalized[0], normalized[1])) {
            throw new HarnessAssertionException(
                    AssertionOperator.FIELD_NOT_EQUALS_OTHER_FIELD,
                    leftPath + " vs " + rightPath,
                    "not equal to " + rightValue,
                    leftValue,
                    "FIELD_NOT_EQUALS_OTHER_FIELD failed. " +
                            leftPath + " = " + leftValue +
                            ", " + rightPath + " = " + rightValue +
                            ". Expected them NOT to be equal.");
        }
    }
}

