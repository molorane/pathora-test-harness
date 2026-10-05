package com.example.demo.dto;

import java.util.List;

public record UserResponse(
        String userId,
        String username,
        String email,
        String role,
        String status,
        String createdAt,
        String ipAddress,
        String websiteUrl,
        String trackingUuid,
        String firstName,
        String fullName,
        String mixedCaseNotes,
        String blankBio,
        String emptyNotes,
        String postalCode,
        List<String> allBlankTags,
        List<String> anyBlankTags,
        List<String> noneBlankTags
) {
}


