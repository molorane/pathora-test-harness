package com.example.demo.dto.customer;

/**
 * Request payload used to demonstrate a custom operator extension scenario.
 *
 * <p>The request models a customer profile that is evaluated against business rules related
 * to subscription eligibility and region-based access. The structure intentionally stays small
 * so the custom assertion operators can remain easy to follow in the demo.</p>
 *
 * @param customerId         the customer identifier to evaluate
 * @param subscriptionStatus the current subscription state supplied by the calling system
 * @param region             the operating or business region for the customer
 */
public record CustomerEligibilityRequest(
        String customerId,
        String subscriptionStatus,
        String region
) {
}


