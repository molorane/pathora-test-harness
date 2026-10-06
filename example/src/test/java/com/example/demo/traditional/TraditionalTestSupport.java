package com.example.demo.traditional;

import com.example.demo.dto.InventoryRequest;
import com.example.demo.dto.InventoryResponse;
import com.example.demo.dto.LoanRequest;
import com.example.demo.dto.LoanResponse;
import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.dto.PolicyRequest;
import com.example.demo.dto.PolicyResponse;
import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.service.ComplexPolicyService;
import com.example.demo.service.InventoryUpdateService;
import com.example.demo.service.LoanApplicationService;
import com.example.demo.service.OrderProcessingService;
import com.example.demo.service.PaymentGatewayService;
import com.example.demo.service.UserRegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

abstract class TraditionalTestSupport {

    protected static final Path POLICY_REQUEST_PATH = Paths.get("templates/requests/complex-policy-request.json");
    protected static final Path USER_REQUEST_PATH = Paths.get("templates/requests/user-create-request.json");
    protected static final Path PAYMENT_REQUEST_PATH = Paths.get("templates/requests/payment-process-request.json");
    protected static final Path ORDER_REQUEST_PATH = Paths.get("templates/requests/order-checkout-request.json");
    protected static final Path INVENTORY_REQUEST_PATH = Paths.get("templates/requests/inventory-update-request.json");
    protected static final Path LOAN_REQUEST_PATH = Paths.get("templates/requests/loan-application-request.json");

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}$");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("\\d+");
    private static final Pattern ALPHA_PATTERN = Pattern.compile("[A-Za-z]+");
    private static final Pattern ALPHA_NUMERIC_PATTERN = Pattern.compile("[A-Za-z0-9]+");
    private static final Pattern ALPHA_SPACE_PATTERN = Pattern.compile("[A-Za-z ]+");

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ComplexPolicyService complexPolicyService;

    @Autowired
    protected UserRegistrationService userRegistrationService;

    @Autowired
    protected PaymentGatewayService paymentGatewayService;

    @Autowired
    protected OrderProcessingService orderProcessingService;

    @Autowired
    protected InventoryUpdateService inventoryUpdateService;

    @Autowired
    protected LoanApplicationService loanApplicationService;

    protected PolicyResponse evaluatePolicy() throws Exception {
        return evaluatePolicy(UnaryOperator.identity());
    }

    protected PolicyResponse evaluatePolicy(UnaryOperator<PolicyRequest> mutator) throws Exception {
        PolicyRequest request = objectMapper.readValue(Files.readString(POLICY_REQUEST_PATH), PolicyRequest.class);
        return complexPolicyService.evaluatePolicy(mutator.apply(request));
    }

    protected UserResponse registerUser() throws Exception {
        return registerUser(UnaryOperator.identity());
    }

    protected UserResponse registerUser(UnaryOperator<UserRequest> mutator) throws Exception {
        UserRequest request = objectMapper.readValue(Files.readString(USER_REQUEST_PATH), UserRequest.class);
        return userRegistrationService.registerUser(mutator.apply(request));
    }

    protected PaymentResponse processPayment(UnaryOperator<PaymentRequest> mutator) throws Exception {
        PaymentRequest request = objectMapper.readValue(Files.readString(PAYMENT_REQUEST_PATH), PaymentRequest.class);
        return paymentGatewayService.processPayment(mutator.apply(request));
    }

    protected OrderResponse processOrder(UnaryOperator<OrderRequest> mutator) throws Exception {
        OrderRequest request = objectMapper.readValue(Files.readString(ORDER_REQUEST_PATH), OrderRequest.class);
        return orderProcessingService.processOrder(mutator.apply(request));
    }

    protected InventoryResponse updateInventory(UnaryOperator<InventoryRequest> mutator) throws Exception {
        InventoryRequest request = objectMapper.readValue(Files.readString(INVENTORY_REQUEST_PATH), InventoryRequest.class);
        return inventoryUpdateService.updateInventory(mutator.apply(request));
    }

    protected LoanResponse processLoan(UnaryOperator<LoanRequest> mutator) throws Exception {
        LoanRequest request = objectMapper.readValue(Files.readString(LOAN_REQUEST_PATH), LoanRequest.class);
        return loanApplicationService.processApplication(mutator.apply(request));
    }

    protected static boolean isSortedAscending(List<String> values) {
        return values.equals(values.stream().sorted().toList());
    }

    protected static boolean isSortedDescending(List<String> values) {
        return values.equals(values.stream().sorted(Comparator.reverseOrder()).toList());
    }

    protected static boolean isBlank(String value) {
        return value != null && value.isBlank();
    }

    protected static boolean isNumeric(String value) {
        return value != null && NUMERIC_PATTERN.matcher(value).matches();
    }

    protected static boolean isAlpha(String value) {
        return value != null && ALPHA_PATTERN.matcher(value).matches();
    }

    protected static boolean isAlphaNumeric(String value) {
        return value != null && ALPHA_NUMERIC_PATTERN.matcher(value).matches();
    }

    protected static boolean isAlphaSpace(String value) {
        return value != null && ALPHA_SPACE_PATTERN.matcher(value).matches();
    }

    protected static boolean isMixedCase(String value) {
        return value != null
                && value.chars().anyMatch(Character::isUpperCase)
                && value.chars().anyMatch(Character::isLowerCase);
    }

    protected static boolean isValidEmail(String value) {
        return value != null && EMAIL_PATTERN.matcher(value).matches();
    }

    protected static boolean isValidIpv4(String value) {
        return value != null && IPV4_PATTERN.matcher(value).matches();
    }

    protected static boolean isValidUrl(String value) {
        try {
            URI uri = URI.create(value);
            return uri.getScheme() != null && uri.getHost() != null;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    protected static boolean isValidUuid(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}

