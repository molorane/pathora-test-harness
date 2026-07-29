package io.github.molorane.pathora.testharness.engine;

import tools.jackson.databind.ObjectMapper;
import io.github.molorane.pathora.testharness.registry.EntryPointRegistry;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import io.github.molorane.pathora.testharness.util.XmlHelper;

public class EntryPointDispatcher {

    private final EntryPointRegistry registry;
    private final ObjectMapper objectMapper;
    private final ObjectMapper xmlMapper;

    public EntryPointDispatcher(
            EntryPointRegistry registry,
            ObjectMapper objectMapper) {
        this(registry, objectMapper, null);
    }

    public EntryPointDispatcher(
            EntryPointRegistry registry,
            ObjectMapper objectMapper,
            ObjectMapper xmlMapper) {
        this.registry = registry;
        this.objectMapper = objectMapper;
        this.xmlMapper = xmlMapper;
    }

    public String dispatch(String entryPointName, String requestPayload)
            throws Exception {
        EntryPointExecutor executor = registry.get(entryPointName);

        Object request;
        if (XmlHelper.isXml(requestPayload)) {
            ObjectMapper mapper = (xmlMapper != null) ? xmlMapper : XmlHelper.getXmlMapper();
            request = mapper.readValue(requestPayload, executor.getRequestType());
        } else {
            request = objectMapper.readValue(requestPayload, executor.getRequestType());
        }

        Object response = executor.execute(request);

        return objectMapper.writeValueAsString(response);
    }
}