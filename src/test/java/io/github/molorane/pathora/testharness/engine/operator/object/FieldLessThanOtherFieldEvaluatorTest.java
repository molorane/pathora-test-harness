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

class FieldLessThanOtherFieldEvaluatorTest {

    private FieldLessThanOtherFieldEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new FieldLessThanOtherFieldEvaluator();
    }

    private DocumentContext parse(String json) {
        return JsonPath.parse(json);
    }

    @Test
    @DisplayName("PASS: left field < right field")
    void shouldPassWhenLeftLessThanRight() {
        DocumentContext ctx = parse("""
            {"outputData": {"minScore": 50, "maxScore": 100}}
            """);
        Object value = TestJsonHelper.parse("""
            {
              "leftPath": "$.outputData.minScore",
              "rightPath": "$.outputData.maxScore"
            }
            """);
        assertThatNoException().isThrownBy(() -> operator.apply(ctx, value));
    }

    @Test
    @DisplayName("FAIL: left field >= right field")
    void shouldFailWhenLeftNotLessThanRight() {
        DocumentContext ctx = parse("""
            {"outputData": {"minScore": 100, "maxScore": 50}}
            """);
        Object value = TestJsonHelper.parse("""
            {
              "leftPath": "$.outputData.minScore",
              "rightPath": "$.outputData.maxScore"
            }
            """);
        assertThatThrownBy(() -> operator.apply(ctx, value))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("FIELD_LESS_THAN_OTHER_FIELD failed");
    }
}

