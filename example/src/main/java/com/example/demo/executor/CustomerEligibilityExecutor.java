package com.example.demo.executor;

import com.example.demo.dto.customer.CustomerEligibilityRequest;
import com.example.demo.dto.customer.CustomerEligibilityResponse;
import com.example.demo.service.CustomerEligibilityService;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.springframework.stereotype.Component;

/**
 * Entry point executor that exposes the customer eligibility domain service through the harness.
 *
 * <p>This executor is intentionally separate from the built-in demonstration services, so
 * the custom operator extension remains isolated and easy to understand as a plugin example.</p>
 */
@Component
public class CustomerEligibilityExecutor implements EntryPointExecutor<CustomerEligibilityRequest, CustomerEligibilityResponse> {

    private final CustomerEligibilityService customerEligibilityService;

    /**
     * Creates the executor with the backing customer eligibility service.
     *
     * @param customerEligibilityService the business service used to resolve customer eligibility
     */
    public CustomerEligibilityExecutor(CustomerEligibilityService customerEligibilityService) {
        this.customerEligibilityService = customerEligibilityService;
    }

    @Override
    public String getEntryPointName() {
        return "customer-eligibility-service";
    }

    @Override
    public Class<CustomerEligibilityRequest> getRequestType() {
        return CustomerEligibilityRequest.class;
    }

    @Override
    public CustomerEligibilityResponse execute(CustomerEligibilityRequest request) {
        String customerId = request.customerId();
        return new CustomerEligibilityResponse(
            customerId,
            customerEligibilityService.getSubscriptionStatus(customerId),
            customerEligibilityService.getRegion(customerId),
            customerEligibilityService.isInAllowedRegion(customerId, java.util.List.of("EU", "APAC"))
        );
    }
}


