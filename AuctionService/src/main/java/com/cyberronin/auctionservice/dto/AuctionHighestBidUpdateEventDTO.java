package com.cyberronin.auctionservice.dto;

import java.util.UUID;

public record AuctionHighestBidUpdateEventDTO(
        UUID id,
        UUID highestBidUserId,
        long highestBidAmount,
        long highestBidTimestamp
){}
