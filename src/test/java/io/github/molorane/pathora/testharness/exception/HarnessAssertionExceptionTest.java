package io.github.molorane.pathora.testharness.exception;

import io.github.molorane.pathora.testharness.engine.AssertionEngine;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HarnessAssertionExceptionTest {

    @Test
    @DisplayName("Constructor without test details sets fields and formats message")
    void constructorWithoutTestDetails() {
        HarnessAssertionException exception = new HarnessAssertionException(
            AssertionOperator.EQUALS,
            "$.status",
            "APPROVED",
            "PENDING",
            "Values did not match"
        );

        assertThat(exception.testName()).isNull();
        assertThat(exception.testDescription()).isNull();
        assertThat(exception.operator()).isEqualTo(AssertionOperator.EQUALS);
        assertThat(exception.path()).isEqualTo("$.status");
        assertThat(exception.expected()).isEqualTo("APPROVED");
        assertThat(exception.actual()).isEqualTo("PENDING");
        assertThat(exception.detailMessage()).isEqualTo("Values did not match");

        String message = exception.getMessage();
        assertThat(message)
            .contains("TestName:        null")
            .contains("TestDescription: null")
            .contains("Assertion:       EQUALS")
            .contains("Path:            $.status")
            .contains("Expected:        APPROVED")
            .contains("Actual:          PENDING")
            .contains("Values did not match");
    }

    @Test
    @DisplayName("Constructor with test details sets all fields and formats message")
    void constructorWithTestDetails() {
        HarnessAssertionException exception = new HarnessAssertionException(
            "Verify Status Code",
            "Ensures the order status is APPROVED",
            AssertionOperator.EQUALS,
            "$.order.status",
            "APPROVED",
            "REJECTED",
            "Order status should be APPROVED"
        );

        assertThat(exception.testName()).isEqualTo("Verify Status Code");
        assertThat(exception.testDescription()).isEqualTo("Ensures the order status is APPROVED");
        assertThat(exception.operator()).isEqualTo(AssertionOperator.EQUALS);
        assertThat(exception.path()).isEqualTo("$.order.status");
        assertThat(exception.expected()).isEqualTo("APPROVED");
        assertThat(exception.actual()).isEqualTo("REJECTED");
        assertThat(exception.detailMessage()).isEqualTo("Order status should be APPROVED");

        String message = exception.getMessage();
        assertThat(message)
            .contains("TestName:        Verify Status Code")
            .contains("TestDescription: Ensures the order status is APPROVED")
            .contains("Assertion:       EQUALS")
            .contains("Path:            $.order.status")
            .contains("Expected:        APPROVED")
            .contains("Actual:          REJECTED")
            .contains("Order status should be APPROVED");
    }

    @Test
    @DisplayName("withTestDetails enriches exception with testName and testDescription")
    void withTestDetailsEnrichesException() {
        HarnessAssertionException original = new HarnessAssertionException(
            AssertionOperator.GREATER_THAN,
            "$.amount",
            100,
            50,
            "Amount too low"
        );

        HarnessAssertionException enriched = original.withTestDetails("Amount Test", "Checks minimum amount");

        assertThat(enriched.testName()).isEqualTo("Amount Test");
        assertThat(enriched.testDescription()).isEqualTo("Checks minimum amount");
        assertThat(enriched.operator()).isEqualTo(AssertionOperator.GREATER_THAN);
        assertThat(enriched.path()).isEqualTo("$.amount");
        assertThat(enriched.expected()).isEqualTo(100);
        assertThat(enriched.actual()).isEqualTo(50);
        assertThat(enriched.detailMessage()).isEqualTo("Amount too low");

        assertThat(enriched.getMessage())
            .contains("TestName:        Amount Test")
            .contains("TestDescription: Checks minimum amount")
            .contains("Assertion:       GREATER_THAN")
            .contains("Path:            $.amount")
            .contains("Expected:        100")
            .contains("Actual:          50")
            .contains("Amount too low");
    }

    @Test
    @DisplayName("AssertionEngine populates testName and testDescription upon failure")
    void assertionEnginePopulatesTestDetailsOnFailure() {
        AssertionEngine engine = new AssertionEngine();
        RuleTestCase testCase = new RuleTestCase(
            "User Age Verification",
            "Verifies that user is an adult",
            "UserEntryPoint",
            null,
            List.of(new JsonAssertion("$.user.age", AssertionOperator.GREATER_THAN, 18, null, null))
        );

        String response = "{\"user\": {\"age\": 16}}";

        assertThatThrownBy(() -> engine.assertResponse(response, testCase))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("TestName:        User Age Verification")
            .hasMessageContaining("TestDescription: Verifies that user is an adult")
            .hasMessageContaining("Assertion:       GREATER_THAN")
            .hasMessageContaining("Path:            $.user.age")
            .satisfies(e -> {
                HarnessAssertionException ex = (HarnessAssertionException) e;
                assertThat(ex.testName()).isEqualTo("User Age Verification");
                assertThat(ex.testDescription()).isEqualTo("Verifies that user is an adult");
            });
    }
}

