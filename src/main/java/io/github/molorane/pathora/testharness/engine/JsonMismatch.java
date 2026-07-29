package io.github.molorane.pathora.testharness.engine;

import tools.jackson.databind.JsonNode;

record JsonMismatch(String path, JsonNode expected, JsonNode actual) {
}
