package com.cyberronin.auctionstorageservice.dto;
import com.cyberronin.auctionstorageservice.model.AuctionStatus;

import java.util.UUID;

public record AuctionCreatedEventDTO(
        UUID id,

        long createdAt, // epoch time
        long expiresAt, // epoch time
        AuctionStatus status,

        UUID sellerId,
        String sellerName,
        String sellerLocation,

        String itemName,
        String itemDescription,
        String itemImageUrl,

        long startingAmount // shifted decimal i.e. $2.32 -> 232 and 4323 -> $43.23
) {}