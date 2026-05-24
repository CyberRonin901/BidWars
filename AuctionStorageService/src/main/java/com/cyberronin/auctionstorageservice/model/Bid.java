package com.cyberronin.auctionstorageservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Bid {

    @Id
    private UUID id;

    private UUID auctionId;
    private UUID userId;
    private long amount;
    private long timestamp; // Stored as plain long unix epoch millisecond
}
