package com.example.demo.executor;

import com.example.demo.dto.AllOperatorsRequest;
import com.example.demo.dto.AllOperatorsResponse;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AllOperatorsExecutor implements EntryPointExecutor {

    @Override
    public String getEntryPointName() {
        return "all-operators-service";
    }

    @Override
    public Class<?> getRequestType() {
        return AllOperatorsRequest.class;
    }

    @Override
    public Object execute(Object request) {
        AllOperatorsRequest req = (AllOperatorsRequest) request;
        String categoryVal = req.category() != null ? req.category() : "TECH";

        Map<String, Object> expectedWithNull = new HashMap<>();
        expectedWithNull.put("id", 101);
        expectedWithNull.put("optionalNote", null);
        expectedWithNull.put("name", "Jane Doe");

        return new AllOperatorsResponse(
                // Scalar
                85,
                -10,
                "APPROVED",
                36.6,
                4.5,

                // String
                "PRD-2026-990",
                "developer@pathora.io",
                "v2.1.0-RELEASE",

                // Date
                "2000-01-01",
                "2099-12-31",
                "2020-01-01T00:00:00",
                "2099-01-01T00:00:00",
                "2026-01-01T00:00:00",
                "2026-01-05T00:00:00",
                "2026-02-15T00:00:00",
                "2026-08-18T08:00:00",
                "2026-08-18T18:00:00",

                // Duration
                "2026-08-18T10:00:00",
                "2026-08-18T12:00:00",

                // Structural
                "present",
                true,

                // Array
                List.of(10, 20, 30),
                List.of(1, 2, 3, 4, 5),
                List.of("ONLY_ONE"),
                List.of(),
                List.of("apple", "banana", "cherry"),
                List.of("banana"),
                List.of(Map.of("id", 1, "name", "Alice"), Map.of("id", 2, "name", "Bob")),
                categoryVal,

                // Object
                expectedWithNull,
                Map.of("total", 100.0, "calculatedTotal", 100.0),

                // Money
                100.50,
                Map.of("amount", 250.75, "currency", "USD"),
                50000.00,
                5.25,
                1000.00,
                100.005
        );
    }
}
