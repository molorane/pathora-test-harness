package io.github.molorane.pathora.testharness.engine;

import io.github.molorane.pathora.testharness.model.JsonMutation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.xml.XmlMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonMutationEngineTest {

    private JsonMutationEngine mutationEngine;

    @BeforeEach
    void setUp() {
        mutationEngine = new JsonMutationEngine();
    }

    private JsonMutation mutation(String path, Object value) {
        return new JsonMutation(path, value);
    }

    @Test
    @DisplayName("PASS: constructor with custom ObjectMapper initializes successfully")
    void shouldInitializeWithCustomMapper() {
        JsonMutationEngine customEngine = new JsonMutationEngine(new ObjectMapper(), new XmlMapper());
        String result = customEngine.apply("{\"name\":\"Alice\"}", List.of(mutation("$.name", "Bob")), "test.json", "EP");
        assertThat(result).contains("\"name\":\"Bob\"");
    }

    @Test
    @DisplayName("PASS: apply returns original payload unchanged when mutations list is null or empty")
    void shouldReturnOriginalPayloadWhenMutationsEmpty() {
        String payload = "{\"user\":{\"name\":\"Alice\"}}";

        String resultNull = mutationEngine.apply(payload, null, "test.json", "EP");
        String resultEmpty = mutationEngine.apply(payload, List.of(), "test.json", "EP");

        assertThat(resultNull).isEqualTo(payload);
        assertThat(resultEmpty).isEqualTo(payload);
    }

    @Test
    @DisplayName("PASS: apply mutates direct object leaf properties")
    void shouldMutateDirectObjectProperties() {
        String payload = "{\"user\":{\"name\":\"Alice\", \"age\":30}}";
        List<JsonMutation> mutations = List.of(
                mutation("$.user.name", "Bob"),
                mutation("$.user.age", 35)
        );

        String result = mutationEngine.apply(payload, mutations, "test.json", "EP");

        assertThat(result).contains("\"name\":\"Bob\"");
        assertThat(result).contains("\"age\":35");
    }

    @Test
    @DisplayName("PASS: apply mutates array elements via filter path")
    void shouldMutateArrayElementsViaFilterPath() {
        String payload = "{\"items\":[{\"id\":101, \"price\":10.0}, {\"id\":102, \"price\":20.0}]}";
        List<JsonMutation> mutations = List.of(
                mutation("$.items[?(@.id == 101)].price", 15.5)
        );

        String result = mutationEngine.apply(payload, mutations, "test.json", "EP");

        assertThat(result).contains("\"price\":15.5");
        assertThat(result).contains("\"price\":20.0");
    }

    @Test
    @DisplayName("PASS: apply mutates direct index element in array")
    void shouldMutateDirectIndexElementInArray() {
        String payload = "{\"items\":[{\"name\":\"ItemA\"}, {\"name\":\"ItemB\"}]}";
        List<JsonMutation> mutations = List.of(
                mutation("$.items[0].name", "UpdatedA")
        );

        String result = mutationEngine.apply(payload, mutations, "test.json", "EP");

        assertThat(result).contains("\"name\":\"UpdatedA\"");
        assertThat(result).contains("\"name\":\"ItemB\"");
    }

    @Test
    @DisplayName("PASS: apply mutates XML payload when isXml=true")
    void shouldMutateXmlPayloadSuccessfully() {
        String xmlPayload = "<user><name>Alice</name><age>25</age></user>";
        List<JsonMutation> mutations = List.of(
                mutation("$.name", "Charlie")
        );

        String result = mutationEngine.apply(xmlPayload, mutations, "test.xml", "EP", true);

        assertThat(result).contains("\"name\":\"Charlie\"");
    }

    @Test
    @DisplayName("FAIL: apply throws AssertionError on invalid XML conversion")
    void shouldFailWhenXmlConversionFails() {
        String invalidXml = "<user><unclosedTag></user>";
        List<JsonMutation> mutations = List.of(
                mutation("$.name", "Value")
        );

        assertThatThrownBy(() -> mutationEngine.apply(invalidXml, mutations, "test.xml", "EP", true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Failed to convert XML template");
    }

    @Test
    @DisplayName("FAIL: apply throws AssertionError when mutation path has no leaf property")
    void shouldThrowAssertionErrorWhenPathHasNoLeafProperty() {
        String payload = "{\"name\":\"Alice\"}";
        List<JsonMutation> mutations = List.of(
                mutation("invalidPathNoDot", "Value")
        );

        assertThatThrownBy(() -> mutationEngine.apply(payload, mutations, "test.json", "EP"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("MUTATION_FAILED")
                .hasMessageContaining("Invalid mutation path");
    }

    @Test
    @DisplayName("FAIL: apply throws AssertionError when parent path returns null")
    void shouldThrowAssertionErrorWhenParentPathIsNull() {
        String payload = "{\"user\":null}";
        List<JsonMutation> mutations = List.of(
                mutation("$.user.name", "Value")
        );

        assertThatThrownBy(() -> mutationEngine.apply(payload, mutations, "test.json", "EP"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("MUTATION_FAILED")
                .hasMessageContaining("Parent path returned null");
    }

    @Test
    @DisplayName("FAIL: apply throws AssertionError when filter matches no elements in array")
    void shouldThrowAssertionErrorWhenFilterMatchesNoElements() {
        String payload = "{\"items\":[{\"id\":101}]}";
        List<JsonMutation> mutations = List.of(
                mutation("$.items[?(@.id == 999)].price", 10.0)
        );

        assertThatThrownBy(() -> mutationEngine.apply(payload, mutations, "test.json", "EP"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("MUTATION_FAILED")
                .hasMessageContaining("No element matched filter");
    }

    @Test
    @DisplayName("FAIL: apply throws AssertionError when array elements are primitive strings instead of JSON objects")
    void shouldThrowAssertionErrorWhenArrayElementsAreNotObjects() {
        String payload = "{\"tags\":[\"tag1\", \"tag2\"]}";
        List<JsonMutation> mutations = List.of(
                mutation("$.tags[?(@ == 'tag1')].val", "newVal")
        );

        assertThatThrownBy(() -> mutationEngine.apply(payload, mutations, "test.json", "EP"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("MUTATION_FAILED")
                .hasMessageContaining("Target element is not JSON object");
    }

    @Test
    @DisplayName("FAIL: apply throws AssertionError when parent path result type is primitive string")
    void shouldThrowAssertionErrorWhenParentResultTypeIsUnsupported() {
        String payload = "{\"user\":{\"name\":\"Alice\"}}";
        List<JsonMutation> mutations = List.of(
                mutation("$.user.name.first", "Bob")
        );

        assertThatThrownBy(() -> mutationEngine.apply(payload, mutations, "test.json", "EP"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("MUTATION_FAILED")
                .hasMessageContaining("Unsupported parent result type");
    }
}
