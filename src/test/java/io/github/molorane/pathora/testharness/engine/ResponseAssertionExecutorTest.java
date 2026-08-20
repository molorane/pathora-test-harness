package io.github.molorane.pathora.testharness.engine;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResponseAssertionExecutorTest {

    private ResponseAssertionExecutor executor;
    private AssertionEngine assertionEngine;

    @BeforeEach
    void setUp() {
        assertionEngine = new AssertionEngine();
        executor = new ResponseAssertionExecutor(assertionEngine);
    }

    private JsonAssertion assertion(String jsonPath, AssertionOperator operator, Object value) {
        return new JsonAssertion(jsonPath, operator, value, null, null);
    }

    private RuleTestCase testCase(List<JsonAssertion> assertions) {
        return new RuleTestCase("Test Execution", "Description", "EntryPoint1", List.of(), assertions);
    }

    @Test
    @DisplayName("PASS: execute delegates to AssertionEngine and passes successfully")
    void shouldExecuteResponseAssertionsSuccessfully() {
        String mutatedRequest = "{\"input\":\"data\"}";
        String response = "{\"status\":\"SUCCESS\", \"code\":200}";

        RuleTestCase ruleTestCase = testCase(List.of(
                assertion("$.status", AssertionOperator.EQUALS, "SUCCESS"),
                assertion("$.code", AssertionOperator.EQUALS, 200)
        ));

        assertThatNoException().isThrownBy(() -> executor.execute(mutatedRequest, response, ruleTestCase));
    }

    @Test
    @DisplayName("FAIL: execute throws HarnessAssertionException when response assertion fails")
    void shouldThrowExceptionWhenAssertionFails() {
        String mutatedRequest = "{\"input\":\"data\"}";
        String response = "{\"status\":\"FAILURE\", \"code\":500}";

        RuleTestCase ruleTestCase = testCase(List.of(
                assertion("$.status", AssertionOperator.EQUALS, "SUCCESS")
        ));

        assertThatThrownBy(() -> executor.execute(mutatedRequest, response, ruleTestCase))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("EQUALS failed");
    }
}
