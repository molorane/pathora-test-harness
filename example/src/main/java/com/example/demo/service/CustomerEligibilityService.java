package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Domain service that models a simple customer eligibility decision.
 *
 * <p>This service is intentionally focused on the custom operator demo. It does not change the
 * harness itself; instead, it exposes business facts that a custom evaluator can validate.
 * The operators in the example are:</p>
 * <ul>
 *   <li>{@code HAS_ACTIVE_SUBSCRIPTION} - customer subscription is active</li>
 *   <li>{@code IN_REGION} - customer region matches the allowed region list</li>
 * </ul>
 */
@Service
public class CustomerEligibilityService {

    /**
     * Returns the active subscription state of the given customer.
     *
     * @param customerId the customer identifier
     * @return the subscription state, for example {@code ACTIVE}, {@code INACTIVE}, or {@code CANCELLED}
     */
    public String getSubscriptionStatus(String customerId) {
        return switch (customerId == null ? "" : customerId.trim()) {
            case "CUST-1001" -> "ACTIVE";
            case "CUST-1002" -> "PENDING";
            case "CUST-1003" -> "ACTIVE";
            default -> "INACTIVE";
        };
    }

    /**
     * Returns the business region for the customer.
     *
     * @param customerId the customer identifier
     * @return the region code, such as {@code EU}, {@code US}, or {@code APAC}
     */
    public String getRegion(String customerId) {
        return switch (customerId == null ? "" : customerId.trim()) {
            case "CUST-1001" -> "EU";
            case "CUST-1002" -> "US";
            case "CUST-1003" -> "APAC";
            default -> "UNKNOWN";
        };
    }

    /**
     * Resolves whether the customer is active in a specific region.
     *
     * @param customerId     the customer identifier
     * @param allowedRegions the regions allowed for this rule
     * @return {@code true} when the customer's region is included in the allowed region list
     */
    public boolean isInAllowedRegion(String customerId, List<String> allowedRegions) {
        if (customerId == null || allowedRegions == null || allowedRegions.isEmpty()) {
            return false;
        }
        String region = getRegion(customerId);
        Set<String> normalized = allowedRegions.stream()
            .filter(value -> value != null && !value.isBlank())
            .map(value -> value.trim().toUpperCase())
            .collect(java.util.stream.Collectors.toSet());
        return normalized.contains(region.toUpperCase());
    }
}