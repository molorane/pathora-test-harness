package io.github.molorane.pathora.testharness.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.molorane.pathora.testharness.registry.EntryPointRegistry;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;

/**
 * Dispatches test requests to the appropriate {@link EntryPointExecutor} based on entry point name and payload format.
 */
public class EntryPointDispatcher {

    private final EntryPointRegistry registry;
    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;

    /**
     * Constructs a new {@code EntryPointDispatcher}.
     *
     * @param registry     the entry point executor registry
     * @param objectMapper the JSON object mapper
     * @param xmlMapper    the XML object mapper
     */
    public EntryPointDispatcher(
        EntryPointRegistry registry,
        ObjectMapper objectMapper,
        XmlMapper xmlMapper) {
        this.registry = registry;
        this.objectMapper = objectMapper;
        this.xmlMapper = xmlMapper;
    }

    /**
     * Dispatches a JSON request payload to the target entry point.
     *
     * @param entryPointName the registered entry point name
     * @param requestPayload the raw JSON request payload
     * @return the serialized JSON response
     * @throws Exception if execution or serialization fails
     */
    public String dispatch(String entryPointName, String requestPayload) throws Exception {
        return dispatch(entryPointName, requestPayload, false);
    }

    /**
     * Dispatches a JSON or XML request payload to the target entry point.
     *
     * @param entryPointName the registered entry point name
     * @param requestPayload the raw request payload
     * @param isXml          whether the request payload is XML
     * @return the serialized JSON response
     * @throws Exception if execution or serialization fails
     */
    @SuppressWarnings("unchecked")
    public String dispatch(String entryPointName, String requestPayload, boolean isXml) throws Exception {
        EntryPointExecutor<Object, Object> executor =
            (EntryPointExecutor<Object, Object>) registry.get(entryPointName);

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





