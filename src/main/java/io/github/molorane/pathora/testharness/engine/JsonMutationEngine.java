package io.github.molorane.pathora.testharness.engine;

import tools.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.github.molorane.pathora.testharness.model.JsonMutation;
import io.github.molorane.pathora.testharness.util.XmlHelper;

import java.util.List;
import java.util.Map;

public class JsonMutationEngine {

    private final ObjectMapper objectMapper;
    private final ObjectMapper xmlMapper;

    public JsonMutationEngine() {
        this(new ObjectMapper(), null);
    }

    public JsonMutationEngine(ObjectMapper objectMapper) {
        this(objectMapper, null);
    }

    public JsonMutationEngine(ObjectMapper objectMapper, ObjectMapper xmlMapper) {
        this.objectMapper = objectMapper;
        this.xmlMapper = xmlMapper;
    }

    public String apply(String payload,
                        List<JsonMutation> mutations,
                        String testFileName,
                        String entryPoint) {

        // If no mutations provided, return original payload unchanged
        if (mutations == null || mutations.isEmpty()) {
            return payload;
        }

        String jsonPayload = XmlHelper.isXml(payload) ? convertXmlToJson(payload) : payload;

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
                                Entry Point : %s
                                JsonPath    : %s
                                Value       : %s

                                Reason:
                                %s

                                Original Payload:
                                %s
                                """.formatted(
                                testFileName,
                                entryPoint,
                                mutation.jsonPath(),
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
            ObjectMapper mapper = (xmlMapper != null) ? xmlMapper : XmlHelper.getXmlMapper();
            Object node = mapper.readValue(xml, Object.class);
            return objectMapper.writeValueAsString(node);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Failed to convert XML template to JSON representation: " + e.getMessage(), e
            );
        }
    }

    private void applySingleMutation(DocumentContext context,
                                     JsonMutation mutation) {

        String fullPath = mutation.jsonPath();

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

                targetMap.put(leafProperty, mutation.value());
            }
            return;
        }

        // CASE 2: Direct object path (like [0])
        if (parentResult instanceof Map<?, ?> map) {

            @SuppressWarnings("unchecked")
            Map<String, Object> targetMap = (Map<String, Object>) map;

            targetMap.put(leafProperty, mutation.value());
            return;
        }

        throw new IllegalStateException(
                "Unsupported parent result type for path: "
                        + parentPath
                        + " -> " + parentResult.getClass()
        );
    }
}
