package com.cyberronin.auctionstorageservice.dto;

import java.util.UUID;

public record AuctionHighestBidUpdateDTO(
        UUID id,
        UUID highestBidUserId,
        long highestBidAmount,
        long highestBidTimestamp
){}
