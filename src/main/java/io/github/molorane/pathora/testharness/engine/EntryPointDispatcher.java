package io.github.molorane.pathora.testharness.engine;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.xml.XmlMapper;
import io.github.molorane.pathora.testharness.registry.EntryPointRegistry;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;

public class EntryPointDispatcher {

    private final EntryPointRegistry registry;
    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;

    public EntryPointDispatcher(
            EntryPointRegistry registry,
            ObjectMapper objectMapper,
            XmlMapper xmlMapper) {
        this.registry = registry;
        this.objectMapper = objectMapper;
        this.xmlMapper = xmlMapper;
    }

    public String dispatch(String entryPointName, String requestPayload) throws Exception {
        return dispatch(entryPointName, requestPayload, false);
    }

    public String dispatch(String entryPointName, String requestPayload, boolean isXml) throws Exception {
        EntryPointExecutor executor = registry.get(entryPointName);

        Object request;
        if (isXml && requestPayload != null && requestPayload.trim().startsWith("<")) {
            request = xmlMapper.readValue(requestPayload, executor.getRequestType());
        } else {
            request = objectMapper.readValue(requestPayload, executor.getRequestType());
        }

        Object response = executor.execute(request);

        return objectMapper.writeValueAsString(response);
    }
}