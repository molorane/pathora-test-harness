package za.co.nedbank.brm.testharness.engine;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import za.co.nedbank.brm.testharness.model.JsonMutation;

import java.util.List;
import java.util.Map;

public class JsonMutationEngine {

    public String apply(String json,
                        List<JsonMutation> mutations,
                        String testFileName,
                        String entryPoint) {

        DocumentContext context = JsonPath.parse(json);

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
                        
                        Original JSON:
                        %s
                        """.formatted(
                                testFileName,
                                entryPoint,
                                mutation.jsonPath(),
                                mutation.value(),
                                e.getMessage(),
                                json
                        ),
                        e
                );
            }
        }

        return context.jsonString();
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

            if (list.size() > 1) {
                throw new IllegalStateException(
                        "Multiple elements matched filter for path: " + parentPath
                );
            }

            Object target = list.get(0);

            if (!(target instanceof Map<?, ?> map)) {
                throw new IllegalStateException(
                        "Target element is not JSON object: " + target
                );
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> targetMap = (Map<String, Object>) map;

            targetMap.put(leafProperty, mutation.value());
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
