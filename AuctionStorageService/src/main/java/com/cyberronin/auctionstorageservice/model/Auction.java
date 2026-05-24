package com.cyberronin.auctionstorageservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Auction {

    @Id
    private UUID id;

    private long createdAt;
    private long expiresAt;

    @Enumerated(EnumType.STRING)
    private AuctionStatus status;

    private UUID sellerId;

    private String itemName;
    private String itemDescription;
    private String itemImageUrl;

    private long startingAmount;

    private long highestBidAmount;
    private UUID highestBidUserId;
    private long highestBidTimestamp;
}