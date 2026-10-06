package com.example.demo.traditional;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserRegistrationTest extends TraditionalTestSupport {

    @Test
    @DisplayName("Valid User Registration Test")
    void validUserRegistrationTest() throws Exception {
        UserResponse response = registerUser(request -> new UserRequest("sarah_connor", "sarah.connor@cyberdyne.org", "MANAGER", request.status()));

        assertAll(
                () -> assertTrue(response.userId().startsWith("USR-"), "User ID must be generated with USR- prefix"),
                () -> assertEquals("sarah_connor", response.username(), "Username must match updated request parameter"),
                () -> assertTrue(isValidEmail(response.email()), "Email must be a valid email format"),
                () -> assertEquals("MANAGER", response.role(), "Role must match assigned role"),
                () -> assertEquals("ACTIVE", response.status(), "Status must be ACTIVE")
        );
    }
}

