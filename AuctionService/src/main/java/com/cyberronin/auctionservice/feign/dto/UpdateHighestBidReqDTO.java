package com.cyberronin.auctionservice.feign.dto;

import com.cyberronin.auctionservice.model.AuctionStatus;

import java.util.UUID;

public record UpdateHighestBidReqDTO(
        AuctionStatus status,
        long highestBidAmount,
        UUID highestBidUserId,
        long highestBidTimestamp
) {}
