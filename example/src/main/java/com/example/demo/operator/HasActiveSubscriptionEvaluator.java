package com.example.demo.operator;

import com.example.demo.service.CustomerEligibilityService;
import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

public class HasActiveSubscriptionEvaluator implements AssertionEvaluator {

    private final CustomerEligibilityService customerEligibilityService;

    public HasActiveSubscriptionEvaluator(CustomerEligibilityService customerEligibilityService) {
        this.customerEligibilityService = customerEligibilityService;
    }

    @Override
    public String operatorName() {
        return "HAS_ACTIVE_SUBSCRIPTION";
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String customerId = actual == null ? null : String.valueOf(actual);
        String currentStatus = customerEligibilityService.getSubscriptionStatus(customerId);
        String requiredState = expected == null ? "ACTIVE" : String.valueOf(expected).trim().toUpperCase();

        if (!"ACTIVE".equalsIgnoreCase(currentStatus) || !requiredState.equalsIgnoreCase(currentStatus)) {
            throw new HarnessAssertionException(
                null,
                path,
                expected,
                actual,
                "HAS_ACTIVE_SUBSCRIPTION failed at " + path + ". Expected active subscription for customer "
                    + customerId + ", but status was " + currentStatus
            );
        }
    }
}


