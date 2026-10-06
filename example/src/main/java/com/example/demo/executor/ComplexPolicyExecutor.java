package com.example.demo.executor;

import com.example.demo.dto.PolicyRequest;
import com.example.demo.dto.PolicyResponse;
import com.example.demo.services.ComplexPolicyService;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.springframework.stereotype.Component;

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
        return complexPolicyService.execute(req);
    }
}


