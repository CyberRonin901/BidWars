package com.cyberronin.auctionservice.dto;

import java.util.UUID;

public record BidRequestDTO(
        UUID userId,
        long amount
){}
