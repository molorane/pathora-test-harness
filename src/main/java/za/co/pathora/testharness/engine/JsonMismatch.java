package za.co.pathora.testharness.engine;

import com.fasterxml.jackson.databind.JsonNode;

record JsonMismatch(String path, JsonNode expected, JsonNode actual) {
}
