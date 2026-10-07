package com.example.demo;

import com.example.demo.config.TestHarnessConfig;
import io.github.molorane.pathora.testharness.util.PathoraClock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import io.github.molorane.pathora.testharness.engine.AssertionEngine;
import io.github.molorane.pathora.testharness.engine.EntryPointDispatcher;
import io.github.molorane.pathora.testharness.engine.JsonMutationEngine;
import io.github.molorane.pathora.testharness.loader.TestSuiteLoader;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import io.github.molorane.pathora.testharness.model.TestSuite;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
        assertThat(executorBeans).hasSize(6);
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

    @Test
    @DisplayName("Execute Dynamic Date & Timezone Test Suite")
    void testDynamicDateTimezoneSuite() throws Exception {
        runTestSuite("templates/tests/dynamic-date-timezone-test.json");
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
                String effectiveTimezone = (testCase.timezone() != null && !testCase.timezone().isBlank())
                    ? testCase.timezone()
                    : suite.timezone();
                boolean customTimezone = effectiveTimezone != null && !effectiveTimezone.isBlank();
                RuleTestCase effectiveTestCase = customTimezone && (testCase.timezone() == null || testCase.timezone().isBlank())
                    ? new RuleTestCase(testCase.name(), testCase.description(), testCase.operation(), testCase.mutations(), testCase.assertions(), effectiveTimezone)
                    : testCase;

                if (customTimezone) {
                    PathoraClock.setThreadClock(Clock.system(ZoneId.of(effectiveTimezone.trim())));
                }
                try {
                    String mutatedRequest = mutationEngine.apply(
                        rawRequest,
                        effectiveTestCase.mutations(),
                        suitePath.getFileName().toString(),
                        effectiveTestCase.operation(),
                        suite.isXmlRequest()
                    );

                    String responseJson = dispatcher.dispatch(effectiveTestCase.operation(), mutatedRequest, suite.isXmlRequest());
                    assertThat(responseJson).isNotNull().isNotEmpty();

                    assertionEngine.assertResponse(responseJson, effectiveTestCase, mutatedRequest);
                } finally {
                    if (customTimezone) {
                        PathoraClock.clearThreadClock();
                    }
                }
            }, "Test case '" + testCase.name() + "' failed execution or assertions");
        }
    }
}
