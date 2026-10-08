package com.example.demo.dto.user;

public record UserRequest(
        String username,
        String email,
        String role,
        String status
) {
}

