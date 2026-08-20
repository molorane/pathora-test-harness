package io.github.molorane.pathora.testharness.engine;

import io.github.molorane.pathora.testharness.registry.EntryPointRegistry;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.xml.XmlMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EntryPointDispatcherTest {

    private EntryPointDispatcher dispatcher;
    private EntryPointRegistry registry;

    public record DummyRequest(String name) { }

    public record DummyResponse(String message) { }

    static class TestEntryPointExecutor implements EntryPointExecutor {
        @Override
        public String getEntryPointName() {
            return "ProcessUser";
        }

        @Override
        public Class<?> getRequestType() {
            return DummyRequest.class;
        }

        @Override
        public Object execute(Object request) {
            DummyRequest req = (DummyRequest) request;
            return new DummyResponse("Hello " + req.name());
        }
    }

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        XmlMapper xmlMapper = new XmlMapper();
        registry = new EntryPointRegistry(List.of(new TestEntryPointExecutor()));
        dispatcher = new EntryPointDispatcher(registry, objectMapper, xmlMapper);
    }

    @Test
    @DisplayName("PASS: dispatch 2-arg overload defaults isXml to false")
    void shouldDispatchTwoArgOverloadSuccessfully() throws Exception {
        String requestJson = "{\"name\":\"Alice\"}";

        String responseJson = dispatcher.dispatch("ProcessUser", requestJson);

        assertThat(responseJson).contains("\"message\":\"Hello Alice\"");
    }

    @Test
    @DisplayName("PASS: dispatch XML request successfully when isXml=true")
    void shouldDispatchXmlRequestSuccessfully() throws Exception {
        String requestXml = "<DummyRequest><name>Bob</name></DummyRequest>";

        String responseJson = dispatcher.dispatch("ProcessUser", requestXml, true);

        assertThat(responseJson).contains("\"message\":\"Hello Bob\"");
    }

    @Test
    @DisplayName("PASS: dispatch JSON request when isXml=true but payload is non-XML JSON")
    void shouldFallbackToJsonWhenIsXmlTrueButPayloadIsJson() throws Exception {
        String requestJson = "{\"name\":\"Charlie\"}";

        String responseJson = dispatcher.dispatch("ProcessUser", requestJson, true);

        assertThat(responseJson).contains("\"message\":\"Hello Charlie\"");
    }

    @Test
    @DisplayName("FAIL: dispatch throws IllegalArgumentException when entry point not registered")
    void shouldThrowExceptionWhenEntryPointNotFound() {
        assertThatThrownBy(() -> dispatcher.dispatch("UnknownEntryPoint", "{}"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No executor for entry point: UnknownEntryPoint");
    }
}
