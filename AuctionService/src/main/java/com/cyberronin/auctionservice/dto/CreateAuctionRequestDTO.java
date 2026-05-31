package com.cyberronin.auctionservice.dto;

import java.util.UUID;

public record CreateAuctionRequestDTO(
    UUID sellerId,
    long expiresIn, // seconds
    String itemName,
    String itemDescription,
    String itemImageUrl,
    long startingAmount
) {}