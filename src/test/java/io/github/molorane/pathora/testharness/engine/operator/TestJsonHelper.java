package io.github.molorane.pathora.testharness.engine.operator;

import tools.jackson.databind.ObjectMapper;

public class TestJsonHelper {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Object parse(String json) {
        try {
            return MAPPER.readValue(json, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON: " + json, e);
        }
    }
}
