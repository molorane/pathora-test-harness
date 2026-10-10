package com.example.demo.dto.user;

public record UserRequest(
    String username,
    String email,
    String role,
    String status,
    String requestId,
    Integer pinCode,
    String promoRef,
    String contactEmail,
    Boolean emailNotifications,
    String registrationTime,
    String shiftStartTime,
    String shiftEndTime,
    String billingCycleStartDate,
    String billingCycleEndDate,
    String nextBillingCycleDate,
    String fiscalYearStartDate,
    String fiscalYearEndDate,
    String authToken,
    String decodedAuthToken,
    String encodedRedirectUrl,
    String payloadSignature,
    String passwordHash,
    String uppercaseCode,
    String lowercaseCode,
    Double creditLimit,
    Double taxedAmount,
    String envPath,
    String sysJavaVersion,
    String pathoraConfigEnv,
    String pathoraAppName,
    String pathoraAppRegion,
    String customTenantCode
) {
    public UserRequest(String username, String email, String role, String status) {
        this(username, email, role, status, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
}
