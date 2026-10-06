package com.example.demo.executor;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.services.UserRegistrationService;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationExecutor implements EntryPointExecutor<UserRequest, UserResponse> {

    private final UserRegistrationService userRegistrationService;

    public UserRegistrationExecutor(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

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
        return userRegistrationService.execute(userRequest);
    }
}


