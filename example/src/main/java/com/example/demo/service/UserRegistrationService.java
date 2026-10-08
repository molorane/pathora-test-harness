package com.example.demo.service;

import com.example.demo.dto.user.UserRequest;
import com.example.demo.dto.user.UserResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class UserRegistrationService {

    public UserResponse registerUser(UserRequest userRequest) {
        String userId = "USR-" + UUID.randomUUID().toString().substring(0, 8);
        String status = userRequest.status() != null ? userRequest.status() : "ACTIVE";

        return new UserResponse(
                userId,
                userRequest.username(),
                userRequest.email(),
                userRequest.role(),
                status,
                Instant.now(),
                "192.168.1.1",
                "https://example.com/user/profile",
                "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d",
                "John",
                "John Doe",
                "Admin User",
                "   ",
                "",
                "90210",
                List.of("  ", "\t", ""),
                List.of("tag1", "   "),
                List.of("ADMIN", "SUPPORT", "STAFF")
        );
    }
}


