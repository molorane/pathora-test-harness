package io.github.molorane.pathora.testharness.engine;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Record holding details of a JSON node mismatch between expected and actual values.
 *
 * @param path     the JSONPath where mismatch occurred
 * @param expected the expected JSON node
 * @param actual   the actual JSON node
 */
record JsonMismatch(String path, JsonNode expected, JsonNode actual) {
}


