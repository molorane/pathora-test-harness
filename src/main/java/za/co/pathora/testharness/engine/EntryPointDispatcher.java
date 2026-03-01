package za.co.pathora.testharness.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.pathora.testharness.registry.EntryPointRegistry;
import za.co.pathora.testharness.spi.EntryPointExecutor;

public class EntryPointDispatcher {

        private final EntryPointRegistry registry;
        private final ObjectMapper objectMapper;

        public EntryPointDispatcher(
                        EntryPointRegistry registry,
                        ObjectMapper objectMapper) {
                this.registry = registry;
                this.objectMapper = objectMapper;
        }

        public String dispatch(String entryPointName, String requestJson)
                        throws Exception {

                EntryPointExecutor executor = registry.get(entryPointName);

                Object request = objectMapper.readValue(
                                requestJson,
                                executor.getRequestType());

                Object response = executor.execute(request);

                return objectMapper.writeValueAsString(response);
        }
}