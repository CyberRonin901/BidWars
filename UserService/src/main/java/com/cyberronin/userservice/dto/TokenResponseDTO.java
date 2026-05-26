package com.cyberronin.userservice.dto;

import java.time.Instant;

public record TokenResponseDTO(
        String token,
        String type,
        Instant expiresAt
) {
    public TokenResponseDTO(String token, Instant expiresAt) {
        this(token, "Bearer", expiresAt);
    }
}