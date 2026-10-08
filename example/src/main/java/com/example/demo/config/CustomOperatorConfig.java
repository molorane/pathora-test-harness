package com.example.demo.config;

import com.example.demo.service.CustomerEligibilityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.molorane.pathora.testharness.engine.AssertionEngine;

/**
 * Spring configuration that registers custom assertion operators for the demo.
 *
 * <p>The operators registered here are intentionally not part of the core enum. They demonstrate
 * how a project can extend the harness by adding domain-specific logic, while leaving the built-in
 * operators and assertion engine unchanged.</p>
 */
@Configuration
public class CustomOperatorConfig {

    /**
     * Creates a dedicated registrar that binds the extension logic to the shared assertion engine.
     *
     * @param assertionEngine the existing engine used by the harness
     * @param customerEligibilityService the business service used to evaluate customer state
     * @return a registrar bean that wires the custom operators
     */
    @Bean
    public CustomOperatorRegistrar customOperatorRegistrar(
            AssertionEngine assertionEngine,
            CustomerEligibilityService customerEligibilityService) {
        return new CustomOperatorRegistrar(assertionEngine, customerEligibilityService);
    }
}
