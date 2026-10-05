package com.example.demo.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.github.molorane.pathora.testharness.engine.ResponseAssertionExecutor;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import io.github.molorane.pathora.testharness.util.FailureLogger;

import java.nio.file.Path;

@Component
class AssertionFailureReporter {

    private static final Logger log = LoggerFactory.getLogger(AssertionFailureReporter.class);

    private final ResponseAssertionExecutor assertionExecutor;

    AssertionFailureReporter(ResponseAssertionExecutor assertionExecutor) {
        this.assertionExecutor = assertionExecutor;
    }

    void assertAndReport(
            String testFileName,
            String mutatedRequest,
            String response,
            Path reportDirectory,
            RuleTestCase testCase
    ) {
        Path reportFile = reportDirectory.resolve(
                testFileName.replace(".json", "") + "__" + sanitizeForFileName(testCase.name()) + ".json"
        );

        try {
            assertionExecutor.execute(mutatedRequest, response, testCase);
            log.info("Passed: {}", testCase.name());
        } catch (HarnessAssertionException ex) {
            FailureLogger.logFailure(testCase, reportFile, mutatedRequest, response, ex);
            log.error("Failed: {}", testCase.name());
            throw ex;
        } catch (Exception ex) {
            FailureLogger.logFailure(
                    testCase,
                    reportFile,
                    mutatedRequest,
                    response,
                    new AssertionError("SYSTEM_FAILURE\n" + ex.getMessage(), ex)
            );
            log.error("System Failure: {}", testCase.name());
            throw ex;
        }
    }

    private static String sanitizeForFileName(String value) {
        return value.replaceAll("[^A-Za-z0-9._-]", "_");
    }
}

