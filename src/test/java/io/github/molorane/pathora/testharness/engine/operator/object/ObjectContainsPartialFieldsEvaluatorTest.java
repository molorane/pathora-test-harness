package io.github.molorane.pathora.testharness.engine.operator.object;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class ObjectContainsPartialFieldsEvaluatorTest {

    private ObjectContainsPartialFieldsEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new ObjectContainsPartialFieldsEvaluator();
    }

    @Test
    @DisplayName("OPERATOR: returns OBJECT_CONTAINS_PARTIAL_FIELDS")
    void shouldReturnCorrectOperator() {
        assertThat(evaluator.operator()).isEqualTo(AssertionOperator.OBJECT_CONTAINS_PARTIAL_FIELDS);
    }

    @Test
    @DisplayName("PASS: matches object containing nested partial array of objects")
    void shouldPassWhenNestedArrayPartiallyMatches() {
        Map<String, Object> actual = Map.of(
                "displayName", "MR JACK DON",
                "actions", List.of(
                        Map.of("type", "1202"),
                        Map.of(
                                "type", "1111",
                                "details", List.of(
                                        Map.of("name", "remarkType", "value", "1028"),
                                        Map.of("name", "reviewDueDate", "value", "2023-07-14T23:59:59.999")
                                )
                        )
                )
        );

        Map<String, Object> expected = Map.of(
                "displayName", "MR JACK DON",
                "actions", List.of(
                        Map.of(
                                "type", "1111",
                                "details", List.of(
                                        Map.of("name", "remarkType", "value", "1028")
                                )
                        )
                )
        );

        assertThatNoException().isThrownBy(() ->
                evaluator.apply("$.partyInContextResult", actual, expected, true));
    }

    @Test
    @DisplayName("FAIL: nested array has mismatching item")
    void shouldFailWhenNestedArrayMismatches() {
        Map<String, Object> actual = Map.of(
                "displayName", "MR JACK DON",
                "actions", List.of(
                        Map.of("type", "1202")
                )
        );

        Map<String, Object> expected = Map.of(
                "actions", List.of(
                        Map.of("type", "1111")
                )
        );

        assertThatThrownBy(() -> evaluator.apply("$.partyInContextResult", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("OBJECT_CONTAINS_PARTIAL_FIELDS failed");
    }
}

