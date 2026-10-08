package com.example.demo.adapter;

import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.springframework.stereotype.Component;
import io.github.molorane.pathora.testharness.loader.TestSuiteLoader;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import io.github.molorane.pathora.testharness.model.TestSuite;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Component
public class DynamicTestAdapter {

    private final TestSuiteLoader loader;
    private final SuiteFileResolver suiteFileResolver;
    private final ReportDirectoryManager reportDirectoryManager;
    private final RuleTestCaseExecutor ruleTestCaseExecutor;

    public DynamicTestAdapter(
            TestSuiteLoader loader,
            SuiteFileResolver suiteFileResolver,
            ReportDirectoryManager reportDirectoryManager,
            RuleTestCaseExecutor ruleTestCaseExecutor
    ) {
        this.loader = loader;
        this.suiteFileResolver = suiteFileResolver;
        this.reportDirectoryManager = reportDirectoryManager;
        this.ruleTestCaseExecutor = ruleTestCaseExecutor;
    }

    public Stream<DynamicNode> generate(String suitesDirectoryPath) {
        Path reportDirectory = reportDirectoryManager.prepareReportDirectory();
        AtomicInteger counter = new AtomicInteger(0);
        List<Path> suiteFiles = suiteFileResolver.resolve(suitesDirectoryPath);

        return suiteFiles.stream()
                .map(suitePath -> {
                    try {
                        TestSuite suite = loader.load(suitePath);

                        Stream<DynamicTest> testCases =
                                suite.tests().stream()
                                        .map(testCase ->
                                                DynamicTest.dynamicTest(
                                                        counter.incrementAndGet() + " " + testCase.name(),
                                                        () -> executeTestCase(
                                                                suitePath,
                                                                suite,
                                                                testCase,
                                                                reportDirectory
                                                        )
                                                )
                                        );

                        return DynamicContainer.dynamicContainer(
                                suitePath.getFileName().toString(),
                                testCases
                        );
                    } catch (Exception ex) {
                        throw new IllegalStateException("Failed to prepare dynamic tests for suite: " + suitePath, ex);
                    }
                });
    }

    private void executeTestCase(
            Path suitePath,
            TestSuite suite,
            RuleTestCase testCase,
            Path reportDirectory
    ) {
        ruleTestCaseExecutor.execute(suitePath, suite, testCase, reportDirectory);
    }
}

