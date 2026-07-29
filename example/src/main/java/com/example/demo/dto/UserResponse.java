package com.example.demo.dto;

public record UserResponse(
        String userId,
        String username,
        String email,
        String role,
        String status,
        String createdAt
) {
}
