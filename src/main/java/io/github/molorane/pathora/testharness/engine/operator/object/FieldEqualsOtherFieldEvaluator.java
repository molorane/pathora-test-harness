package io.github.molorane.pathora.testharness.engine.operator.object;

import com.jayway.jsonpath.DocumentContext;
import io.github.molorane.pathora.testharness.engine.operator.DocumentContextAwareEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.Map;
import java.util.Objects;

/**
 * Operator: {@code FIELD_EQUALS_OTHER_FIELD}
 *
 * <p>Validates that the value at {@code leftPath} is equal to the value at {@code rightPath} within the response document.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "Operator": "FIELD_EQUALS_OTHER_FIELD",
 *   "Value": {
 *     "leftPath": "$.outputData.shippingAddress.zip",
 *     "rightPath": "$.outputData.billingAddress.zip"
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#FIELD_EQUALS_OTHER_FIELD
 */
public class FieldEqualsOtherFieldEvaluator implements DocumentContextAwareEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.FIELD_EQUALS_OTHER_FIELD;
    }

    @Override
    public void apply(DocumentContext context, Object expected) {

        Map<String, Object> config = AssertionUtils.toMap(expected);

        String leftPath = String.valueOf(config.get("leftPath"));
        String rightPath = String.valueOf(config.get("rightPath"));

        if (leftPath == null || rightPath == null) {
            throw new IllegalArgumentException(
                    "FIELD_EQUALS_OTHER_FIELD requires 'leftPath' and 'rightPath' in Value");
        }

        Object leftValue = context.read(leftPath);
        Object rightValue = context.read(rightPath);

        Object[] normalized = AssertionUtils.normalizeTypes(leftValue, rightValue);

        if (!Objects.equals(normalized[0], normalized[1])) {
            throw new HarnessAssertionException(
                    AssertionOperator.FIELD_EQUALS_OTHER_FIELD,
                    leftPath + " vs " + rightPath,
                    rightValue,
                    leftValue,
                    "FIELD_EQUALS_OTHER_FIELD failed. " +
                            leftPath + " = " + leftValue +
                            ", " + rightPath + " = " + rightValue +
                            ". Expected them to be equal.");
        }
    }
}
