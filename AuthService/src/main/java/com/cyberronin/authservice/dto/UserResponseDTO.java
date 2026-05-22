package com.cyberronin.authservice.dto;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        String role,
        Instant createdAt
) {}