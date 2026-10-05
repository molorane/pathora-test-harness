package com.example.demo.executor;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import org.springframework.stereotype.Component;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class UserRegistrationExecutor implements EntryPointExecutor<UserRequest, UserResponse> {

    @Override
    public String getEntryPointName() {
        return "user-registration-service";
    }

    @Override
    public Class<UserRequest> getRequestType() {
        return UserRequest.class;
    }

    @Override
    public UserResponse execute(UserRequest userRequest) {
        String userId = "USR-" + UUID.randomUUID().toString().substring(0, 8);
        String status = userRequest.status() != null ? userRequest.status() : "ACTIVE";

        return new UserResponse(
                userId,
                userRequest.username(),
                userRequest.email(),
                userRequest.role(),
                status,
                Instant.now().toString(),
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


