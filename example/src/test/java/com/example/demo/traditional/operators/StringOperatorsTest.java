package com.example.demo.traditional.operators;

import com.example.demo.dto.user.UserRequest;
import com.example.demo.dto.user.UserResponse;
import com.example.demo.traditional.TraditionalTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StringOperatorsTest extends TraditionalTestSupport {

    @Test
    @DisplayName("String Content & Case Sensitivity Test")
    void stringContentAndCaseSensitivityTest() throws Exception {
        UserResponse response = registerUser(request -> new UserRequest("johndoe", "john.doe@enterprise.org", "ADMINISTRATOR", request.status()));

        assertAll(
                () -> assertTrue(response.username().contains("john"), "Username contains substring 'john'"),
                () -> assertTrue(response.username().toLowerCase().contains("doe"), "Username contains 'DOE' ignoring case"),
                () -> assertTrue(response.role().equalsIgnoreCase("administrator"), "Role matches ignoring case"),
                () -> assertTrue(response.role().equalsIgnoreCase("administrator") || response.role().equalsIgnoreCase("manager"), "Role matches any candidate ignoring case"),
                () -> assertEquals(response.role(), response.role().toUpperCase(), "Role is fully uppercase"),
                () -> assertEquals(response.username(), response.username().toLowerCase(), "Username is fully lowercase"),
                () -> assertFalse(response.email().contains("spam"), "Email does not contain 'spam'"),
                () -> assertFalse(response.email().toLowerCase().contains("spam"), "Email does not contain 'SPAM' ignoring case"),
                () -> assertTrue(response.userId().startsWith("USR-") || response.userId().startsWith("ACC-"), "User ID starts with one of the allowed prefixes"),
                () -> assertTrue(response.userId().toLowerCase().startsWith("usr-"), "User ID starts with usr- ignoring case"),
                () -> assertTrue(response.email().endsWith(".org") || response.email().endsWith(".com"), "Email ends with one of allowed domain suffixes"),
                () -> assertTrue(response.email().toLowerCase().endsWith(".org") || response.email().toLowerCase().endsWith(".net"), "Email ends with allowed domain suffix ignoring case")
        );
    }

    @Test
    @DisplayName("String Length & Format Validation Test")
    void stringLengthAndFormatValidationTest() throws Exception {
        UserResponse response = registerUser(request -> new UserRequest("alice99", "alice@security.com", request.role(), request.status()));

        assertAll(
                () -> assertTrue(response.username().length() > 5, "Username length is greater than 5"),
                () -> assertTrue(response.postalCode().length() < 10, "Postal code length is less than 10"),
                () -> assertEquals(5, response.postalCode().length(), "Postal code length is exactly 5"),
                () -> assertTrue(response.username().length() >= 4 && response.username().length() <= 15, "Username length is between 4 and 15"),
                () -> assertTrue(isAlphaNumeric(response.username()), "Username contains only alphanumeric characters"),
                () -> assertTrue(isAlpha(response.firstName()), "First name contains only alphabetic characters"),
                () -> assertTrue(isAlphaSpace(response.fullName()), "Full name contains only alphabetic characters and spaces"),
                () -> assertTrue(response.fullName().contains(" "), "Full name contains whitespace"),
                () -> assertTrue(isMixedCase(response.mixedCaseNotes()), "Mixed case notes contain upper and lower case"),
                () -> assertTrue(isBlank(response.blankBio()), "Blank bio contains only whitespace"),
                () -> assertTrue(response.emptyNotes().isEmpty(), "Empty notes is an empty string"),
                () -> assertFalse(response.userId().isEmpty(), "User ID is not empty"),
                () -> assertTrue(isNumeric(response.postalCode()), "Postal code is strictly numeric"),
                () -> assertTrue(isValidEmail(response.email()), "Email conforms to standard email format"),
                () -> assertFalse(response.email().isBlank(), "Email is non-blank"),
                () -> assertTrue(response.status().equals("ACTIVE") || response.status().equals("PENDING") || response.status().equals("INITIAL"), "Status matches one of the candidate values")
        );
    }

    @Test
    @DisplayName("String Network & Array Blankness Test")
    void stringNetworkAndArrayBlanknessTest() throws Exception {
        UserResponse response = registerUser();

        assertAll(
                () -> assertTrue(isValidIpv4(response.ipAddress()), "IP address is valid IPv4"),
                () -> assertTrue(isValidUrl(response.websiteUrl()), "Website URL is valid web address"),
                () -> assertTrue(isValidUuid(response.trackingUuid()), "Tracking UUID is a valid UUID format"),
                () -> assertTrue(response.allBlankTags().stream().allMatch(TraditionalTestSupport::isBlank), "All elements in allBlankTags are blank strings"),
                () -> assertTrue(response.anyBlankTags().stream().anyMatch(TraditionalTestSupport::isBlank), "At least one element in anyBlankTags is blank"),
                () -> assertTrue(response.noneBlankTags().stream().noneMatch(TraditionalTestSupport::isBlank), "No element in noneBlankTags is blank"),
                () -> assertTrue(response.noneBlankTags().stream().noneMatch(String::isEmpty), "No element in noneBlankTags is empty")
        );
    }
}


