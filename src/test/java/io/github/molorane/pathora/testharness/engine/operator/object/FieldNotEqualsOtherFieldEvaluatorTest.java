package io.github.molorane.pathora.testharness.engine.operator.object;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.github.molorane.pathora.testharness.engine.operator.TestJsonHelper;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FieldNotEqualsOtherFieldEvaluatorTest {

    private FieldNotEqualsOtherFieldEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new FieldNotEqualsOtherFieldEvaluator();
    }

    private DocumentContext parse(String json) {
        return JsonPath.parse(json);
    }

    @Test
    @DisplayName("PASS: left field != right field")
    void shouldPassWhenFieldsDiffer() {
        DocumentContext ctx = parse("""
            {"outputData": {"status": "APPROVED", "previousStatus": "PENDING"}}
            """);
        Object value = TestJsonHelper.parse("""
            {
              "leftPath": "$.outputData.status",
              "rightPath": "$.outputData.previousStatus"
            }
            """);
        assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
    }

    @Test
    @DisplayName("FAIL: left field == right field")
    void shouldFailWhenFieldsEqual() {
        DocumentContext ctx = parse("""
            {"outputData": {"status": "APPROVED", "previousStatus": "APPROVED"}}
            """);
        Object value = TestJsonHelper.parse("""
            {
              "leftPath": "$.outputData.status",
              "rightPath": "$.outputData.previousStatus"
            }
            """);
        assertThatThrownBy(() -> operator.apply(ctx, value))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("FIELD_NOT_EQUALS_OTHER_FIELD failed");
    }
}

