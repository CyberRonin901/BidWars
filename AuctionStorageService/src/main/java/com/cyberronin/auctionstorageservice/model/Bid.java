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
@Table("bid")
public class Bid implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column("auction_id")
    private UUID auctionId;

    @Column("user_id")
    private UUID userId;
    private long amount;

    private long timestamp; // Stored as plain long unix epoch millisecond

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
