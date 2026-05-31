package com.cyberronin.auctionservice.dto;

import java.util.UUID;

public record BidPlacedEventDTO(
        UUID auctionId,
        UUID userId,
        long amount,
        long timestamp
) {}