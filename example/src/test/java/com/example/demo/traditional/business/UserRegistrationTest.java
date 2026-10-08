package com.example.demo.traditional.business;

import com.example.demo.dto.user.UserRequest;
import com.example.demo.dto.user.UserResponse;
import com.example.demo.traditional.TraditionalTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

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


