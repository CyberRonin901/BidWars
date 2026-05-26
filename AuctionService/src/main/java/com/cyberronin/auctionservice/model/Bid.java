package com.cyberronin.auctionservice.model;

// This is the data about an individual bid

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bid {

    @Id
    private UUID id;

    private UUID userId;
    private long amount;
    private long timestamp; // Stored as plain long unix epoch millisecond
}
