package com.example.demo;

import io.github.molorane.pathora.testharness.engine.AssertionEngine;
import io.github.molorane.pathora.testharness.engine.EntryPointDispatcher;
import io.github.molorane.pathora.testharness.engine.JsonMutationEngine;
import io.github.molorane.pathora.testharness.loader.TestSuiteLoader;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import io.github.molorane.pathora.testharness.model.TestSuite;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.example.demo.config.TestHarnessConfig;
import org.springframework.context.annotation.Import;

/**
 * Demonstrates testing test suite files individually (one file at a time).
 */
@SpringBootTest
@Import(TestHarnessConfig.class)
class SingleTestSuiteDemoTest {

    @Autowired
    private List<EntryPointExecutor> executorBeans;

    @Autowired
    private EntryPointDispatcher dispatcher;

    @Autowired
    private TestSuiteLoader testSuiteLoader;

    @Autowired
    private JsonMutationEngine mutationEngine;

    @Autowired
    private AssertionEngine assertionEngine;

    @Test
    @DisplayName("Verify Spring Boot Context Loads Executors")
    void contextLoads() {
        assertThat(executorBeans).hasSize(7);
    }

    @Test
    @DisplayName("Execute All Operators Comprehensive Demonstration Test Suite (JSON)")
    void testAllOperatorsSuite() throws Exception {
        runTestSuite("templates/tests/all-operators-test.json");
    }

    @Test
    @DisplayName("Execute Scalar Operators Test Suite (JSON)")
    void testScalarOperatorsSuite() throws Exception {
        runTestSuite("templates/tests/scalar-operators-test.json");
    }

    @Test
    @DisplayName("Execute String Operators Test Suite (JSON)")
    void testStringOperatorsSuite() throws Exception {
        runTestSuite("templates/tests/string-operators-test.json");
    }

    @Test
    @DisplayName("Execute Date Operators Test Suite (JSON)")
    void testDateOperatorsSuite() throws Exception {
        runTestSuite("templates/tests/date-operators-test.json");
    }

    @Test
    @DisplayName("Execute Duration Operators Test Suite (JSON)")
    void testDurationOperatorsSuite() throws Exception {
        runTestSuite("templates/tests/duration-operators-test.json");
    }

    @Test
    @DisplayName("Execute Structural Operators Test Suite (JSON)")
    void testStructuralOperatorsSuite() throws Exception {
        runTestSuite("templates/tests/structural-operators-test.json");
    }

    @Test
    @DisplayName("Execute Array Operators Test Suite (JSON)")
    void testArrayOperatorsSuite() throws Exception {
        runTestSuite("templates/tests/array-operators-test.json");
    }

    @Test
    @DisplayName("Execute Object Operators Test Suite (JSON)")
    void testObjectOperatorsSuite() throws Exception {
        runTestSuite("templates/tests/object-operators-test.json");
    }

    @Test
    @DisplayName("Execute Money Operators Test Suite (JSON)")
    void testMoneyOperatorsSuite() throws Exception {
        runTestSuite("templates/tests/money-operators-test.json");
    }

    @Test
    @DisplayName("Execute User Registration Test Suite (JSON)")
    void testUserRegistrationSuite() throws Exception {
        runTestSuite("templates/tests/user-create-test.json");
    }

    @Test
    @DisplayName("Execute User Registration Test Suite (XML)")
    void testUserRegistrationXmlSuite() throws Exception {
        runTestSuite("templates/tests/user-create-xml-test.json");
    }

    @Test
    @DisplayName("Execute Order Checkout Test Suite")
    void testOrderCheckoutSuite() throws Exception {
        runTestSuite("templates/tests/order-checkout-test.json");
    }

    @Test
    @DisplayName("Execute Payment Gateway Test Suite")
    void testPaymentGatewaySuite() throws Exception {
        runTestSuite("templates/tests/payment-process-test.json");
    }

    @Test
    @DisplayName("Execute Inventory Update Test Suite")
    void testInventoryUpdateSuite() throws Exception {
        runTestSuite("templates/tests/inventory-update-test.json");
    }

    @Test
    @DisplayName("Execute Loan Application Test Suite")
    void testLoanApplicationSuite() throws Exception {
        runTestSuite("templates/tests/loan-application-test.json");
    }

    @Test
    @DisplayName("Execute Deeply Nested Policy Evaluation Test Suite")
    void testPolicyEvaluationSuite() throws Exception {
        runTestSuite("templates/tests/policy-evaluation-test.json");
    }

    @Test
    @DisplayName("Execute Deeply Nested Policy Risk Assessment Test Suite")
    void testPolicyRiskAssessmentSuite() throws Exception {
        runTestSuite("templates/tests/policy-risk-assessment-test.json");
    }

    private void runTestSuite(String testSuiteRelativePath) throws Exception {
        Path suitePath = Paths.get(testSuiteRelativePath);
        assertThat(Files.exists(suitePath))
                .withFailMessage("Test suite file not found: " + suitePath.toAbsolutePath())
                .isTrue();

        TestSuite suite = testSuiteLoader.load(suitePath);
        assertThat(suite).isNotNull();

        String defaultRequestPath = suite.defaultRequestPath();
        Path requestPath = suitePath.getParent().resolve(defaultRequestPath).normalize();
        assertThat(Files.exists(requestPath))
                .withFailMessage("Request template file not found: " + requestPath.toAbsolutePath())
                .isTrue();

        String rawRequest = Files.readString(requestPath);

        for (RuleTestCase testCase : suite.tests()) {
            assertDoesNotThrow(() -> {
                String mutatedRequest = mutationEngine.apply(
                        rawRequest,
                        testCase.testCaseParameterValues(),
                        suitePath.getFileName().toString(),
                        testCase.entryPointName(),
                        suite.isXmlRequest()
                );

                String responseJson = dispatcher.dispatch(testCase.entryPointName(), mutatedRequest, suite.isXmlRequest());
                assertThat(responseJson).isNotNull().isNotEmpty();

                assertionEngine.assertResponse(responseJson, testCase, mutatedRequest);
            }, "Test case '" + testCase.testName() + "' failed execution or assertions");
        }
    }
}
