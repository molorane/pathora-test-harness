package com.example.demo.operator;

import com.example.demo.service.CustomerEligibilityService;
import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.List;

public class InRegionEvaluator implements AssertionEvaluator {

    private final CustomerEligibilityService customerEligibilityService;

    public InRegionEvaluator(CustomerEligibilityService customerEligibilityService) {
        this.customerEligibilityService = customerEligibilityService;
    }

    @Override
    public String operatorName() {
        return "IN_REGION";
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String customerId = actual == null ? null : String.valueOf(actual);
        List<String> regionList = expected instanceof List<?> values
                ? values.stream().map(String::valueOf).toList()
                : List.of(String.valueOf(expected));

        String region = customerEligibilityService.getRegion(customerId);
        boolean matchesAllowedRegion = regionList.stream()
                .map(value -> value == null ? "" : value.trim().toUpperCase())
                .anyMatch(region::equalsIgnoreCase);

        if (!matchesAllowedRegion) {
            throw new HarnessAssertionException(
                    null,
                    path,
                    expected,
                    actual,
                    "IN_REGION failed at " + path + ". Customer " + customerId + " is in " + region
                            + " but expected one of " + regionList
            );
        }
    }
}


