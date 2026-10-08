package com.example.demo.executor;

import com.example.demo.dto.loan.LoanRequest;
import com.example.demo.dto.loan.LoanResponse;
import com.example.demo.service.LoanApplicationService;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.springframework.stereotype.Component;

@Component
public class LoanApplicationExecutor implements EntryPointExecutor<LoanRequest, LoanResponse> {

    private final LoanApplicationService loanApplicationService;

    public LoanApplicationExecutor(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    @Override
    public String getEntryPointName() {
        return "loan-application-service";
    }

    @Override
    public Class<LoanRequest> getRequestType() {
        return LoanRequest.class;
    }

    @Override
    public LoanResponse execute(LoanRequest loanRequest) {
        return loanApplicationService.processApplication(loanRequest);
    }
}



