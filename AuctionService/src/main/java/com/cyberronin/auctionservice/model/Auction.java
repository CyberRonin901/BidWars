package com.cyberronin.auctionservice.model;

// This is the bid room / data i.e item and seller details

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder // Added for easier object creation in Service
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

    private long startingAmount; // shifted decimal i.e. $2.32 -> 232 and 4323 -> $43.23

    private long highestBidAmount;
    private UUID highestBidUserId;
    private long highestBidTimestamp; // epoch time
}
