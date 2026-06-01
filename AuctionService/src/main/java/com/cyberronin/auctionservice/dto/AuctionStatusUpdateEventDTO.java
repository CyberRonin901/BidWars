package com.cyberronin.auctionservice.dto;

import com.cyberronin.auctionservice.model.AuctionStatus;

import java.util.UUID;

public record AuctionStatusUpdateEventDTO(
        UUID id,
        AuctionStatus status
) {}
