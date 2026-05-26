package com.cyberronin.auctionservice.dto;

import java.time.Instant;
import java.util.UUID;

public record CreateAuctionRequestDTO(
    UUID sellerId,
    long expiresAt, // epoch milli
    String itemName,
    String itemDescription,
    String itemImageUrl,
    long startingAmount
) {}
