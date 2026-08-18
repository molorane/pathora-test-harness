package io.github.molorane.pathora.testharness.engine.integration;

import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;

class ObjectOperatorsIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("INTEGRATION: OBJECT OPERATORS (OBJECT_CONTAINS_FIELDS, OBJECT_CONTAINS_FIELDS_IGNORE_NULLS, HAS_KEYS, FIELD_EQUALS_OTHER_FIELD)")
    void shouldTestAllObjectOperatorsWithJsonFile() {
        String jsonPayload = loadJson("object_operators.json");

        Map<String, Object> expectedWithNull = new HashMap<>();
        expectedWithNull.put("id", 101);
        expectedWithNull.put("optionalNote", null);

        List<JsonAssertion> assertions = List.of(
                assertion("$.user", AssertionOperator.OBJECT_CONTAINS_FIELDS, Map.of("id", 101, "name", "Jane Doe")),
                assertion("$.user", AssertionOperator.OBJECT_CONTAINS_FIELDS_IGNORE_NULLS, expectedWithNull),
                assertion("$.user", AssertionOperator.HAS_KEYS, List.of("id", "name", "email")),
                assertion(null, AssertionOperator.FIELD_EQUALS_OTHER_FIELD, Map.of("leftPath", "$.metrics.total", "rightPath", "$.metrics.calculatedTotal"))
        );

        RuleTestCase ruleTestCase = testCase("Object Operators Test", assertions);

        assertThatNoException().isThrownBy(() -> engine.assertResponse(jsonPayload, ruleTestCase));
    }
}
