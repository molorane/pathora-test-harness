package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.molorane.pathora.testharness.engine.AssertionEngine;
import io.github.molorane.pathora.testharness.engine.EntryPointDispatcher;
import io.github.molorane.pathora.testharness.engine.JsonMutationEngine;
import io.github.molorane.pathora.testharness.engine.ResponseAssertionExecutor;
import io.github.molorane.pathora.testharness.loader.RequestLoader;
import io.github.molorane.pathora.testharness.loader.RequestTemplateLoader;
import io.github.molorane.pathora.testharness.loader.TestSuiteLoader;
import io.github.molorane.pathora.testharness.registry.EntryPointRegistry;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TestHarnessConfig {

    @Bean
    public EntryPointRegistry entryPointRegistry(List<EntryPointExecutor> executors) {
        return new EntryPointRegistry(executors);
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public XmlMapper xmlMapper() {
        return new XmlMapper();
    }

    @Bean
    public EntryPointDispatcher entryPointDispatcher(
            EntryPointRegistry registry,
            ObjectMapper objectMapper,
            XmlMapper xmlMapper) {
        return new EntryPointDispatcher(registry, objectMapper, xmlMapper);
    }

    @Bean
    public TestSuiteLoader testSuiteLoader(ObjectMapper objectMapper) {
        return new TestSuiteLoader(objectMapper);
    }

    @Bean
    public RequestLoader requestLoader() {
        return new RequestLoader();
    }

    @Bean
    public RequestTemplateLoader requestTemplateLoader() {
        return new RequestTemplateLoader();
    }

    @Bean
    public JsonMutationEngine jsonMutationEngine(ObjectMapper objectMapper, XmlMapper xmlMapper) {
        return new JsonMutationEngine(objectMapper, xmlMapper);
    }

    @Bean
    public AssertionEngine assertionEngine() {
        return new AssertionEngine();
    }

    @Bean
    public ResponseAssertionExecutor responseAssertionExecutor(AssertionEngine assertionEngine) {
        return new ResponseAssertionExecutor(assertionEngine);
    }
}
