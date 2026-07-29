package com.example.demo.executor;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class UserRegistrationExecutor implements EntryPointExecutor {

    @Override
    public String getEntryPointName() {
        return "user-registration-service";
    }

    @Override
    public Class<?> getRequestType() {
        return UserRequest.class;
    }

    @Override
    public Object execute(Object request) {
        UserRequest userRequest = (UserRequest) request;
        String userId = "USR-" + UUID.randomUUID().toString().substring(0, 8);
        String status = userRequest.status() != null ? userRequest.status() : "ACTIVE";

        return new UserResponse(
                userId,
                userRequest.username(),
                userRequest.email(),
                userRequest.role(),
                status,
                Instant.now().toString()
        );
    }
}
