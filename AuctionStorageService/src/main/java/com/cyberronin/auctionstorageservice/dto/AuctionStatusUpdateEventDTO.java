package com.cyberronin.auctionstorageservice.dto;


import com.cyberronin.auctionstorageservice.model.AuctionStatus;

import java.util.UUID;

public record AuctionStatusUpdateEventDTO(
        UUID id,
        AuctionStatus status
) {}
