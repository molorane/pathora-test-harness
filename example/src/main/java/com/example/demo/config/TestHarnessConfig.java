package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.molorane.pathora.testharness.engine.AssertionEngine;
import io.github.molorane.pathora.testharness.engine.EntryPointDispatcher;
import io.github.molorane.pathora.testharness.engine.JsonMutationEngine;
import io.github.molorane.pathora.testharness.engine.ResponseAssertionExecutor;
import io.github.molorane.pathora.testharness.loader.RequestLoader;
import io.github.molorane.pathora.testharness.loader.RequestTemplateLoader;
import io.github.molorane.pathora.testharness.loader.TestSuiteLoader;
import io.github.molorane.pathora.testharness.registry.EntryPointRegistry;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import io.github.molorane.pathora.testharness.util.PathoraClock;

import java.util.List;

@Configuration
public class TestHarnessConfig {

    @Value("${pathora.timezone:}")
    private String configuredTimezone;

    @PostConstruct
    public void initTimezone() {
        if (configuredTimezone != null && !configuredTimezone.isBlank()) {
            PathoraClock.setTimezone(configuredTimezone);
        }
    }

    @Bean
    public EntryPointRegistry entryPointRegistry(List<EntryPointExecutor<?, ?>> executors) {
        return new EntryPointRegistry(executors);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    @Bean
    public XmlMapper xmlMapper() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
        xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return xmlMapper;
    }

    @Bean
    public EntryPointDispatcher entryPointDispatcher(
        EntryPointRegistry registry,
        ObjectMapper objectMapper,
        XmlMapper xmlMapper
    ) {
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
    public JsonMutationEngine jsonMutationEngine(
        ObjectMapper objectMapper,
        XmlMapper xmlMapper) {
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
