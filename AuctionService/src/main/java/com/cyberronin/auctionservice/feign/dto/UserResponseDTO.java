package com.cyberronin.auctionservice.feign.dto;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(

    UUID id,
    String username,
    String mobile,
    String location,
    String passwordHash, // BCRYPT HASH
    String role,
    Instant createdAt
) {
}
