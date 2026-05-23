package com.cyberronin.auctionstorageservice.dto;

import java.util.UUID;

public record SaveBidReqDTO(
        UUID id,
        UUID userId,
        long amount,
        long timestamp
) {}
