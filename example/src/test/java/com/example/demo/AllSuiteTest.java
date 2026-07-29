package com.example.demo;

import com.example.demo.adapter.DynamicTestAdapter;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

/**
 * Demonstrates executing all test suite files in a directory dynamically using @TestFactory.
 */
@SpringBootTest
class AllSuiteTest {

    private static final Logger log = LoggerFactory.getLogger(AllSuiteTest.class);

    @Autowired
    private DynamicTestAdapter adapter;

    @TestFactory
    Stream<DynamicNode> executeTestSuite() {
        log.info("Generating dynamic test suite from templates/tests");
        return adapter.generate("templates/tests");
    }
}
