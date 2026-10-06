package com.example.demo.traditional;

import com.example.demo.dto.LoanRequest;
import com.example.demo.dto.LoanResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoanApplicationTest extends TraditionalTestSupport {

    @Test
    @DisplayName("High Credit Score Loan Approval Test")
    void highCreditScoreLoanApprovalTest() throws Exception {
        LoanResponse response = processLoan(request -> new LoanRequest("APP-998877", 75000.00, 780, request.termMonths()));

        assertAll(
                () -> assertTrue(response.applicationId().startsWith("APP-"), "Application ID must start with APP-"),
                () -> assertEquals("APP-998877", response.applicantId(), "Applicant ID must match request parameter"),
                () -> assertEquals(75000.00, response.approvedAmount(), 0.0001, "Approved amount must equal requested amount for high credit score"),
                () -> assertTrue(response.interestRate() <= 7.0, "Interest rate must be prime (<= 7.0%)"),
                () -> assertEquals("APPROVED", response.decisionStatus(), "Decision status must be APPROVED")
        );
    }
}

