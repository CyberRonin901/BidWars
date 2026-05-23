package com.cyberronin.auctionstorageservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table ("auction")
public class Auction implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column("created_at")
    private long createdAt;

    @Column("expires_at")
    private long expiresAt;

    private AuctionStatus status;

    @Column("seller_id")
    private UUID sellerId;

    @Column("item_name")
    private String itemName;

    @Column("item_description")
    private String itemDescription;

    @Column("item_image_url")
    private String itemImageUrl;

    @Column("starting_amount")
    private long startingAmount;

    @Column("highest_bid_amount")
    private long highestBidAmount;

    @Column("highest_bid_user_id")
    private UUID highestBidUserId;

    @Column("highest_bid_timestamp")
    private long highestBidTimestamp;

    @Transient // Tells R2DBC to completely ignore this field during SQL generation
    private boolean isNewRecord = true;

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return this.isNewRecord;
    }
}