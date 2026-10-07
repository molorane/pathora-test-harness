package com.example.demo.traditional;

import com.example.demo.dto.PolicyResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ListOperatorsTest extends TraditionalTestSupport {

    @Test
    @DisplayName("List Size & Sorting Order Test")
    void listSizeAndSortingOrderTest() throws Exception {
        PolicyResponse response = evaluatePolicy();

        assertAll(
                () -> assertEquals(2, response.approvedClauses().size(), "Approved clauses array size is exactly 2"),
                () -> assertTrue(response.approvedClauses().size() > 1, "Approved clauses array size is greater than 1"),
                () -> assertTrue(response.approvedClauses().size() < 5, "Approved clauses array size is less than 5"),
                () -> assertTrue(response.approvedClauses().size() >= 1 && response.approvedClauses().size() <= 5, "Approved clauses array size falls between 1 and 5"),
                () -> assertFalse(response.approvedClauses().isEmpty(), "Approved clauses is a non-empty list"),
                () -> assertTrue(isSortedAscending(response.uniqueReferenceCodes()), "Reference codes REF-101, REF-102, REF-103 are sorted ascending"),
                () -> assertTrue(isSortedDescending(response.descendingCodes()), "Reference codes REF-999, REF-888, REF-777 are sorted descending")
        );
    }

    @Test
    @DisplayName("List Membership & Element Matching Test")
    void listMembershipAndElementMatchingTest() throws Exception {
        PolicyResponse response = evaluatePolicy();

        assertAll(
                () -> assertTrue(response.requiredDocs().contains("AUDIT_REPORT"), "Required docs contains AUDIT_REPORT"),
                () -> assertFalse(response.requiredDocs().contains("INVOICE_COPY"), "Required docs does not contain INVOICE_COPY"),
                () -> assertTrue(response.approvedClauses().stream().map(PolicyResponse.ApprovedClause::status).anyMatch("APPROVED"::equals), "At least one clause status matches APPROVED"),
                () -> assertTrue(response.approvedClauses().stream().map(PolicyResponse.ApprovedClause::status).noneMatch("REJECTED"::equals), "No clause status matches REJECTED"),
                () -> assertTrue(response.approvedClauses().stream().map(PolicyResponse.ApprovedClause::status).collect(Collectors.toList()).contains("APPROVED"), "Array contains an object matching partial field status=APPROVED")
        );
    }
}

