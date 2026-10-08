package com.example.demo.config;

import com.example.demo.operator.HasActiveSubscriptionEvaluator;
import com.example.demo.operator.InRegionEvaluator;
import com.example.demo.service.CustomerEligibilityService;
import io.github.molorane.pathora.testharness.engine.AssertionEngine;

/**
 * Internal registrar responsible for registering the custom assertion evaluators.
 */
public class CustomOperatorRegistrar {

    private final AssertionEngine assertionEngine;
    private final CustomerEligibilityService customerEligibilityService;

    /**
     * Constructs the registrar.
     *
     * @param assertionEngine the harness engine instance
     * @param customerEligibilityService the service used to resolve business facts
     */
    public CustomOperatorRegistrar(
            AssertionEngine assertionEngine,
            CustomerEligibilityService customerEligibilityService) {
        this.assertionEngine = assertionEngine;
        this.customerEligibilityService = customerEligibilityService;
        registerOperators();
    }

    private void registerOperators() {
        assertionEngine.registerOperator("HAS_ACTIVE_SUBSCRIPTION",
                new HasActiveSubscriptionEvaluator(customerEligibilityService));
        assertionEngine.registerOperator("IN_REGION",
                new InRegionEvaluator(customerEligibilityService));
    }
}

