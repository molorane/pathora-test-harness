package io.github.molorane.pathora.testharness.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.engine.AssertionEngine;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AssertionEngineLogicalTest {

    private AssertionEngine engine;

    @BeforeEach
    void setUp() throws Exception {
        engine = new AssertionEngine();
    }

    @Test
    @DisplayName("AND PASS: all assertions pass")
    void testANDPass() {
        var base = new JsonAssertion(null, AssertionOperator.AND, null, null, List.of(
                new JsonAssertion("$.score", AssertionOperator.GREATER_THAN, 50, "Check score", null),
                new JsonAssertion("$.status", AssertionOperator.EQUALS, "APPROVED", "Check status",
                        null)));
        var testCase = new RuleTestCase("test", "req", "test", null, List.of(base));
        String response = """
                {"score": 75, "status": "APPROVED"}
                """;
        assertThatNoException().isThrownBy(() -> engine.assertResponse(response, testCase));
    }

    @Test
    @DisplayName("AND FAIL: one assertion fails")
    void testANDFail() {
        var base = new JsonAssertion(null, AssertionOperator.AND, null, null, List.of(
                new JsonAssertion("$.score", AssertionOperator.GREATER_THAN, 50, "Check score", null),
                new JsonAssertion("$.status", AssertionOperator.EQUALS, "APPROVED", "Check status",
                        null)));
        var testCase = new RuleTestCase("test", "req", "test", null, List.of(base));
        String response = """
                {"score": 75, "status": "PENDING"}
                """;
        assertThatThrownBy(() -> engine.assertResponse(response, testCase))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("EQUALS failed");
    }

    @Test
    @DisplayName("OR PASS: one assertion passes")
    void testORPass() {
        var base = new JsonAssertion(null, AssertionOperator.OR, null, null, List.of(
                new JsonAssertion("$.status", AssertionOperator.EQUALS, "APPROVED", null, null),
                new JsonAssertion("$.status", AssertionOperator.EQUALS, "PENDING", null, null)));
        var testCase = new RuleTestCase("test", "req", "test", null, List.of(base));
        String response = """
                {"status": "PENDING"}
                """;
        assertThatNoException().isThrownBy(() -> engine.assertResponse(response, testCase));
    }

    @Test
    @DisplayName("OR FAIL: all assertions fail")
    void testORFail() {
        var base = new JsonAssertion(null, AssertionOperator.OR, null, null, List.of(
                new JsonAssertion("$.status", AssertionOperator.EQUALS, "APPROVED", null, null),
                new JsonAssertion("$.status", AssertionOperator.EQUALS, "PENDING", null, null)));
        var testCase = new RuleTestCase("test", "req", "test", null, List.of(base));
        String response = """
                {"status": "DECLINED"}
                """;
        assertThatThrownBy(() -> engine.assertResponse(response, testCase))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("LOGICAL_OR_FAILED");
    }

    @Test
    @DisplayName("NOT PASS: nested assertion fails")
    void testNOTPass() {
        var base = new JsonAssertion(null, AssertionOperator.NOT, null, null, List.of(
                new JsonAssertion("$.status", AssertionOperator.EQUALS, "DECLINED", null, null)));
        var testCase = new RuleTestCase("test", "req", "test", null, List.of(base));
        String response = """
                {"status": "APPROVED"}
                """;
        assertThatNoException().isThrownBy(() -> engine.assertResponse(response, testCase));
    }

    @Test
    @DisplayName("NOT FAIL: nested assertion passes")
    void testNOTFail() {
        var base = new JsonAssertion(null, AssertionOperator.NOT, null, null, List.of(
                new JsonAssertion("$.status", AssertionOperator.EQUALS, "DECLINED", null, null)));
        var testCase = new RuleTestCase("test", "req", "test", null, List.of(base));
        String response = """
                {"status": "DECLINED"}
                """;

        assertThatThrownBy(() -> engine.assertResponse(response, testCase))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("LOGICAL_NOT_FAILED");
    }
}
