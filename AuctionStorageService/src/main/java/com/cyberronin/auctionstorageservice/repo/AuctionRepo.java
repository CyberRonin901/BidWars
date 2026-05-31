package com.cyberronin.auctionstorageservice.repo;

import com.cyberronin.auctionstorageservice.model.Auction;
import com.cyberronin.auctionstorageservice.model.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuctionRepo extends JpaRepository<Auction, UUID> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Auction a SET a.status=?2 where a.id=?1")
    int updateStatus(UUID id, AuctionStatus status);
}
