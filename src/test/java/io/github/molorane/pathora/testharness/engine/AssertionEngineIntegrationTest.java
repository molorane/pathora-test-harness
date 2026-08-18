package io.github.molorane.pathora.testharness.engine;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AssertionEngineIntegrationTest {

    private AssertionEngine engine;

    @BeforeEach
    void setUp() {
        engine = new AssertionEngine();
    }

    private String loadJsonResource(String resourcePath) {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IllegalArgumentException("Resource file not found: " + resourcePath);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read test JSON resource: " + resourcePath, e);
        }
    }

    private JsonAssertion assertion(String jsonPath, AssertionOperator operator, Object value) {
        return new JsonAssertion(jsonPath, operator, value, null, null);
    }

    private JsonAssertion composite(AssertionOperator operator, List<JsonAssertion> assertions) {
        return new JsonAssertion(null, operator, null, null, assertions);
    }

    private RuleTestCase testCase(String name, List<JsonAssertion> assertions) {
        return new RuleTestCase(name, "Integration Test Description", "EntryPoint", List.of(), assertions);
    }

    @Test
    @DisplayName("INTEGRATION: Banking JSON response file assertions (MONEY, arrays, dates)")
    void shouldAssertBankingResponseJsonFileSuccessfully() {
        String jsonPayload = loadJsonResource("/integration/banking_response.json");

        List<JsonAssertion> assertions = List.of(
                assertion("$.status", AssertionOperator.EQUALS, "ACTIVE"),
                assertion("$.accountType", AssertionOperator.VALUE_IN, List.of("SAVINGS", "CHECKING")),
                assertion("$.balance", AssertionOperator.MONEY_EQUALS, Map.of("amount", 15420.50, "currency", "USD")),
                assertion("$.balance", AssertionOperator.MONEY_GREATER_THAN, 10000.00),
                assertion("$.balance", AssertionOperator.MONEY_BETWEEN, Map.of("min", 10000.00, "max", 20000.00)),
                assertion("$.transactions", AssertionOperator.ARRAY_SIZE_EQUALS, 3),
                assertion("$.transactions[*].id", AssertionOperator.UNIQUE_ELEMENTS, null),
                assertion("$.openingDate", AssertionOperator.DATE_BEFORE, "2025-01-01")
        );

        RuleTestCase ruleTestCase = testCase("Banking Test", assertions);

        assertThatNoException().isThrownBy(() -> engine.assertResponse(jsonPayload, ruleTestCase));
    }

    @Test
    @DisplayName("INTEGRATION: E-commerce Order JSON file assertions (field comparison, logical compose)")
    void shouldAssertEcommerceOrderJsonFileSuccessfully() {
        String jsonPayload = loadJsonResource("/integration/ecommerce_order.json");

        List<JsonAssertion> assertions = List.of(
                assertion("$.orderId", AssertionOperator.STARTS_WITH, "ORD-"),
                assertion("$.payment.status", AssertionOperator.EQUALS, "COMPLETED"),
                assertion("$.totals.grandTotal", AssertionOperator.MONEY_EQUALS, 258.39),
                assertion(null, AssertionOperator.FIELD_EQUALS_OTHER_FIELD, Map.of("leftPath", "$.totals.subtotal", "rightPath", "$.totals.calculatedSubtotal")),
                composite(AssertionOperator.AND, List.of(
                        assertion("$.customer.membershipLevel", AssertionOperator.EQUALS, "GOLD"),
                        assertion("$.shippingAddress.country", AssertionOperator.EQUALS, "USA")
                ))
        );

        RuleTestCase ruleTestCase = testCase("E-commerce Order Test", assertions);

        assertThatNoException().isThrownBy(() -> engine.assertResponse(jsonPayload, ruleTestCase));
    }

    @Test
    @DisplayName("INTEGRATION: User Profile JSON file assertions (existence, arrays, collections)")
    void shouldAssertUserProfileJsonFileSuccessfully() {
        String jsonPayload = loadJsonResource("/integration/user_profile.json");

        List<JsonAssertion> assertions = List.of(
                assertion("$.userId", AssertionOperator.EXISTS, null),
                assertion("$.profile.securityFlags.twoFactorEnabled", AssertionOperator.EQUALS, true),
                assertion("$.roles", AssertionOperator.CONTAINS_ALL, List.of("USER", "ADMIN")),
                assertion("$.profile.age", AssertionOperator.GREATER_THAN, 18),
                assertion("$.preferences.theme", AssertionOperator.VALUE_IN, List.of("DARK", "LIGHT"))
        );

        RuleTestCase ruleTestCase = testCase("User Profile Test", assertions);

        assertThatNoException().isThrownBy(() -> engine.assertResponse(jsonPayload, ruleTestCase));
    }

    @Test
    @DisplayName("INTEGRATION FAIL: Money currency mismatch on JSON file payload")
    void shouldFailWhenMonetaryCurrencyMismatchesInJsonFile() {
        String jsonPayload = loadJsonResource("/integration/banking_response.json");

        List<JsonAssertion> assertions = List.of(
                assertion("$.balance", AssertionOperator.MONEY_EQUALS, Map.of("amount", 15420.50, "currency", "EUR"))
        );

        RuleTestCase ruleTestCase = testCase("Banking Currency Mismatch", assertions);

        assertThatThrownBy(() -> engine.assertResponse(jsonPayload, ruleTestCase))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("Currency mismatch");
    }
}
