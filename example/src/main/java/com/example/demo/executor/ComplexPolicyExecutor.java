package com.example.demo.executor;

import com.example.demo.dto.policy.PolicyRequest;
import com.example.demo.dto.policy.PolicyResponse;
import com.example.demo.service.ComplexPolicyService;
import org.springframework.stereotype.Component;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;

@Component
public class ComplexPolicyExecutor implements EntryPointExecutor<PolicyRequest, PolicyResponse> {

    private final ComplexPolicyService complexPolicyService;

    public ComplexPolicyExecutor(ComplexPolicyService complexPolicyService) {
        this.complexPolicyService = complexPolicyService;
    }

    @Override
    public String getEntryPointName() {
        return "policy-evaluation-service";
    }

    @Override
    public Class<PolicyRequest> getRequestType() {
        return PolicyRequest.class;
    }

    @Override
    public PolicyResponse execute(PolicyRequest req) {
        return complexPolicyService.evaluatePolicy(req);
    }
}



