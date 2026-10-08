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

class FieldGreaterThanOtherFieldEvaluatorTest {

    private FieldGreaterThanOtherFieldEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new FieldGreaterThanOtherFieldEvaluator();
    }

    private DocumentContext parse(String json) {
        return JsonPath.parse(json);
    }

    @Test
    @DisplayName("PASS: left field > right field")
    void shouldPassWhenLeftGreaterThanRight() {
        DocumentContext ctx = parse("""
            {"outputData": {"maxScore": 100, "minScore": 50}}
            """);
        Object value = TestJsonHelper.parse("""
            {
              "leftPath": "$.outputData.maxScore",
              "rightPath": "$.outputData.minScore"
            }
            """);
        assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
    }

    @Test
    @DisplayName("FAIL: left field <= right field")
    void shouldFailWhenLeftNotGreaterThanRight() {
        DocumentContext ctx = parse("""
            {"outputData": {"maxScore": 50, "minScore": 100}}
            """);
        Object value = TestJsonHelper.parse("""
            {
              "leftPath": "$.outputData.maxScore",
              "rightPath": "$.outputData.minScore"
            }
            """);
        assertThatThrownBy(() -> operator.apply(ctx, value))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("FIELD_GREATER_THAN_OTHER_FIELD failed");
    }
}

