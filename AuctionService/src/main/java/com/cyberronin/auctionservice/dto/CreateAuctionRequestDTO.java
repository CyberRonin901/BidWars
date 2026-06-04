package com.cyberronin.auctionservice.dto;

import java.util.UUID;

public record CreateAuctionRequestDTO(
    long expiresIn, // seconds
    String itemName,
    String itemDescription,
    String itemImageUrl,
    long startingAmount
) {}