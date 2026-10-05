package io.github.molorane.pathora.testharness.engine.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;

class ListOperatorsIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("INTEGRATION: LIST OPERATORS (SIZE_EQUALS, CONTAINS, CONTAINS_ONLY_VALUES, CONTAINS_ONLY_ONE_VALUE, CONTAINS_OBJECT, ALL_MATCH, CONTAINS_ANY, CONTAINS_ALL, DOES_NOT_CONTAIN_ANY, DOES_NOT_CONTAIN_ALL, IS_EMPTY, UNIQUE, VALUE_IN, VALUE_NOT_IN)")
    void shouldTestAllListOperatorsWithJsonFile() {
        String jsonPayload = loadJson("list_operators.json");

        List<JsonAssertion> assertions = List.of(
                assertion("$.numbers", AssertionOperator.LIST_SIZE_EQUALS, 3),
                assertion("$.numbers", AssertionOperator.LIST_CONTAINS, 20),
                assertion("$.singleFruitList", AssertionOperator.LIST_CONTAINS_ONLY_VALUES, List.of("banana")),
                assertion("$.singleElementList", AssertionOperator.LIST_CONTAINS_ONLY_ONE_VALUE, "ONLY_ONE"),
                assertion("$.users", AssertionOperator.LIST_CONTAINS_OBJECT_WITH_FIELDS, Map.of("name", "Alice")),
                assertion("$.numbers", AssertionOperator.ALL_MATCH, Map.of("greaterThan", 5)),
                assertion("$.fruits", AssertionOperator.CONTAINS_ANY, List.of("banana", "mango")),
                assertion("$.fruits", AssertionOperator.CONTAINS_ALL, List.of("apple", "cherry")),
                assertion("$.fruits", AssertionOperator.DOES_NOT_CONTAIN_ANY, List.of("grape", "orange")),
                assertion("$.fruits", AssertionOperator.DOES_NOT_CONTAIN_ALL, List.of("apple", "grape")),
                assertion("$.emptyList", AssertionOperator.IS_EMPTY_LIST, null),
                assertion("$.uniqueNumbers", AssertionOperator.UNIQUE_ELEMENTS, null),
                assertion("$.category", AssertionOperator.VALUE_IN, List.of("TECH", "FINANCE", "HEALTH")),
                assertion("$.category", AssertionOperator.VALUE_NOT_IN, List.of("SPORTS", "MUSIC"))
        );

        RuleTestCase ruleTestCase = testCase("List Operators Test", assertions);

        assertThatNoException().isThrownBy(() -> engine.assertResponse(jsonPayload, ruleTestCase));
    }
}

