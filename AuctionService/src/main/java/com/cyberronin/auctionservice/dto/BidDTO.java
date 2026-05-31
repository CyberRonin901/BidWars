package com.cyberronin.auctionservice.dto;

import java.util.UUID;

public record BidDTO(
        UUID userId,
        String username,
        long amount
){}
