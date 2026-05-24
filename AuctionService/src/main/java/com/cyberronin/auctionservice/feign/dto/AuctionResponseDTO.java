package com.cyberronin.auctionservice.feign.dto;

import com.cyberronin.auctionservice.model.AuctionStatus;

import java.util.UUID;

public record AuctionResponseDTO(
        UUID id,
        long createdAt,
        long expiresAt,
        AuctionStatus status,
        UUID sellerId,
        String itemName,
        String itemDescription,
        String itemImageUrl,
        long startingAmount,
        long highestBidAmount,
        UUID highestBidUserId,
        long highestBidTimestamp
) {}
