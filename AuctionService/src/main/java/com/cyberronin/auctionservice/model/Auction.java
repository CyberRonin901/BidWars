package com.cyberronin.auctionservice.model;

// This is the bid room / data i.e item and seller details

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auction {

    @Id
    private UUID id;

    private long createdAt; // epoch time
    private long expiresAt; // epoch time

    private AuctionStatus status;

    private UUID sellerId;
    private String sellerName;
    private String sellerLocation;

    private String itemName;

    private String itemDescription;

    private String itemImageUrl;

    private long startingAmount;
    private long highestBidAmount;
    private UUID highestBidUserId;
    private long highestBidTimestamp;
}