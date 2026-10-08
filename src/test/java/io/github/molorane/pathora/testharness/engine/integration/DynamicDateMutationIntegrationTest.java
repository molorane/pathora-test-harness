package io.github.molorane.pathora.testharness.engine.integration;

import com.jayway.jsonpath.JsonPath;
import io.github.molorane.pathora.testharness.engine.JsonMutationEngine;
import io.github.molorane.pathora.testharness.model.JsonMutation;
import io.github.molorane.pathora.testharness.util.PathoraClock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DynamicDateMutationIntegrationTest {

    private JsonMutationEngine mutationEngine;

    @BeforeEach
    void setUp() {
        mutationEngine = new JsonMutationEngine();
        PathoraClock.freeze(Instant.parse("2026-10-07T12:00:00Z"));
    }

    @AfterEach
    void tearDown() {
        PathoraClock.reset();
    }

    @Test
    void testMutateDateWithDynamicTokens() {
        String basePayload = """
            {
                "applicationId": "APP-100",
                "applicationDate": "2020-01-01",
                "dob": "1990-01-01",
                "createdAt": "2020-01-01T00:00:00Z"
            }
            """;

        List<JsonMutation> mutations = List.of(
            new JsonMutation("$.applicationDate", "{{$CURRENT_DATE}}"),
            new JsonMutation("$.dob", "{{$CURRENT_DATE - 25y}}"),
            new JsonMutation("$.createdAt", "{{$CURRENT_DATETIME}}")
        );

        String mutated = mutationEngine.apply(basePayload, mutations, "loan-test.json", "applyLoan");

        assertEquals("2026-10-07", JsonPath.read(mutated, "$.applicationDate"));
        assertEquals("2001-10-07", JsonPath.read(mutated, "$.dob"));
        assertEquals("2026-10-07T12:00:00Z", JsonPath.read(mutated, "$.createdAt"));
    }
}

