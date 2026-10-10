package io.github.molorane.pathora.testharness.engine;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.xml.XmlMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.github.molorane.pathora.testharness.model.JsonMutation;
import io.github.molorane.pathora.testharness.engine.expression.ExpressionResolver;

import java.util.List;
import java.util.Map;

/**
 * Engine that modifies base request payloads by applying a list of {@link JsonMutation} definitions.
 */
public class JsonMutationEngine {

    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;

    /**
     * Constructs a {@code JsonMutationEngine} with default JSON and XML mappers.
     */
    public JsonMutationEngine() {
        this(new ObjectMapper(), new XmlMapper());
    }

    /**
     * Constructs a {@code JsonMutationEngine} with custom mappers.
     *
     * @param objectMapper the JSON object mapper
     * @param xmlMapper    the XML object mapper
     */
    public JsonMutationEngine(ObjectMapper objectMapper, XmlMapper xmlMapper) {
        this.objectMapper = objectMapper;
        this.xmlMapper = xmlMapper;
    }

    /**
     * Applies a list of mutations to a JSON request payload.
     *
     * @param payload      the original payload string
     * @param mutations    the mutations to apply
     * @param testFileName the test file context for error reporting
     * @param entryPoint   the operation/entry point name
     * @return the mutated JSON payload string
     */
    public String apply(String payload,
                        List<JsonMutation> mutations,
                        String testFileName,
                        String entryPoint) {
        return apply(payload, mutations, testFileName, entryPoint, false);
    }

    /**
     * Applies a list of mutations to a JSON or XML request payload.
     *
     * @param payload      the original payload string
     * @param mutations    the mutations to apply
     * @param testFileName the test file context for error reporting
     * @param entryPoint   the operation/entry point name
     * @param isXml        whether the incoming payload is XML
     * @return the mutated JSON payload string
     */
    public String apply(String payload,
                        List<JsonMutation> mutations,
                        String testFileName,
                        String entryPoint,
                        boolean isXml) {

        // If no mutations provided, return original payload unchanged
        if (mutations == null || mutations.isEmpty()) {
            return payload;
        }

        String jsonPayload = isXml ? convertXmlToJson(payload) : payload;

        DocumentContext context = JsonPath.parse(jsonPayload);

        for (JsonMutation mutation : mutations) {

            try {
                applySingleMutation(context, mutation);

            } catch (Exception e) {
                throw new AssertionError(
                    """
                        ==========================
                        MUTATION_FAILED
                        ==========================
                        Test File   : %s
                        Operation   : %s
                        JsonPath    : %s
                        Value       : %s
                        
                        Reason:
                        %s
                        
                        Original Payload:
                        %s
                        """.formatted(
                        testFileName,
                        entryPoint,
                        mutation.path(),
                        mutation.value(),
                        e.getMessage(),
                        payload
                    ),
                    e
                );
            }
        }

        return context.jsonString();
    }

    private String convertXmlToJson(String xml) {
        try {
            Object node = xmlMapper.readValue(xml, Object.class);
            return objectMapper.writeValueAsString(node);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Failed to convert XML template to JSON representation: " + e.getMessage(), e
            );
        }
    }

    private void applySingleMutation(DocumentContext context,
                                     JsonMutation mutation) {

        String fullPath = mutation.path();

        int lastDot = fullPath.lastIndexOf('.');
        if (lastDot == -1) {
            throw new IllegalArgumentException(
                "Invalid mutation path (no leaf property): " + fullPath
            );
        }

        String parentPath = fullPath.substring(0, lastDot);
        String leafProperty = fullPath.substring(lastDot + 1);

        Object parentResult = context.read(parentPath);

        if (parentResult == null) {
            throw new IllegalStateException(
                "Parent path returned null: " + parentPath
            );
        }

        Object resolvedValue = ExpressionResolver.resolve(mutation.value());

        // CASE 1: Filter path (returns List)
        if (parentResult instanceof List<?> list) {

            if (list.isEmpty()) {
                throw new IllegalStateException(
                    "No element matched filter for path: " + parentPath
                );
            }

            for (Object target : list) {

                if (!(target instanceof Map<?, ?> map)) {
                    throw new IllegalStateException(
                        "Target element is not JSON object: " + target
                    );
                }

                @SuppressWarnings("unchecked")
                Map<String, Object> targetMap = (Map<String, Object>) map;

                targetMap.put(leafProperty, resolvedValue);
            }
            return;
        }

        // CASE 2: Direct object path (like [0])
        if (parentResult instanceof Map<?, ?> map) {

            @SuppressWarnings("unchecked")
            Map<String, Object> targetMap = (Map<String, Object>) map;

            targetMap.put(leafProperty, resolvedValue);
            return;
        }

        throw new IllegalStateException(
            "Unsupported parent result type for path: "
                + parentPath
                + " -> " + parentResult.getClass()
        );
    }
}


