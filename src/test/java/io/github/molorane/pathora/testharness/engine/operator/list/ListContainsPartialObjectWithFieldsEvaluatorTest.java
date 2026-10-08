package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class ListContainsPartialObjectWithFieldsEvaluatorTest {

    private ListContainsPartialObjectWithFieldsEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new ListContainsPartialObjectWithFieldsEvaluator();
    }

    @Test
    @DisplayName("OPERATOR: returns LIST_CONTAINS_PARTIAL_OBJECT_WITH_FIELDS")
    void shouldReturnCorrectOperator() {
        assertThat(evaluator.operator()).isEqualTo(AssertionOperator.LIST_CONTAINS_PARTIAL_OBJECT_WITH_FIELDS);
    }

    @Test
    @DisplayName("PASS: matches object containing nested partial array of objects")
    void shouldPassWhenNestedArrayPartiallyMatches() {
        List<Map<String, Object>> actual = List.of(
            Map.of("type", "1202"),
            Map.of(
                "type", "1111",
                "details", List.of(
                    Map.of("name", "remarkType", "value", "1028"),
                    Map.of("name", "reviewDueDate", "value", "2023-07-14T23:59:59.999")
                )
            )
        );

        Map<String, Object> expected = Map.of(
            "type", "1111",
            "details", List.of(
                Map.of("name", "remarkType", "value", "1028")
            )
        );

        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.actions", actual, expected, true));
    }

    @Test
    @DisplayName("PASS: matches simple flat object fields")
    void shouldPassWhenFlatObjectMatches() {
        List<Map<String, Object>> actual = List.of(
            Map.of("id", "A1", "status", "ACTIVE", "extra", "ignore"),
            Map.of("id", "B2", "status", "PENDING")
        );

        Map<String, Object> expected = Map.of("id", "A1", "status", "ACTIVE");

        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.items", actual, expected, true));
    }

    @Test
    @DisplayName("FAIL: no element matches nested partial array")
    void shouldFailWhenNestedArrayHasNoMatch() {
        List<Map<String, Object>> actual = List.of(
            Map.of(
                "type", "1111",
                "details", List.of(
                    Map.of("name", "otherType", "value", "9999")
                )
            )
        );

        Map<String, Object> expected = Map.of(
            "type", "1111",
            "details", List.of(
                Map.of("name", "remarkType", "value", "1028")
            )
        );

        assertThatThrownBy(() -> evaluator.apply("$.actions", actual, expected, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("LIST_CONTAINS_PARTIAL_OBJECT_WITH_FIELDS failed");
    }

    @Test
    @DisplayName("FAIL: actual value is not a list")
    void shouldFailWhenActualIsNotList() {
        assertThatThrownBy(() -> evaluator.apply("$.notAList", "stringValue", Map.of("id", "1"), true))
            .isInstanceOf(AssertionError.class);
    }
}

