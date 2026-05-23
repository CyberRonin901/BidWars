package com.cyberronin.auctionservice.dto;

import java.time.Instant;
import java.util.UUID;

public record CreateAuctionRequestDTO(
    UUID sellerId,
    Instant expiresAt,
    String itemName,
    String itemDescription,
    String itemImageUrl,
    long startingAmount
) {}
