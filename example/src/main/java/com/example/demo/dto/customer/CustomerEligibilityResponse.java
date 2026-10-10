package com.example.demo.dto.customer;

/**
 * Response object returned by the custom customer eligibility executor.
 *
 * <p>The response contains enough information for JSONPath-based assertions and custom plugin
 * operators to evaluate business rules without modifying the core harness.</p>
 *
 * @param customerId              the customer identifier
 * @param subscriptionStatus      the computed subscription state
 * @param region                  the customer region
 * @param eligibleInPrimaryRegion whether the customer falls within the allowed region set
 */
public record CustomerEligibilityResponse(
    String customerId,
    String subscriptionStatus,
    String region,
    boolean eligibleInPrimaryRegion
) {
}

