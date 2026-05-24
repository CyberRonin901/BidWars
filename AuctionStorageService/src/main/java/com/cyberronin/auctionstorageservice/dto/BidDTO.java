package com.cyberronin.auctionstorageservice.dto;

import java.util.UUID;

public record BidDTO(
        UUID id,
        UUID userId,
        long amount,
        long timestamp
) {}
