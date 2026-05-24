package com.cyberronin.auctionstorageservice.repo;

import com.cyberronin.auctionstorageservice.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BidRepo extends JpaRepository<Bid, UUID> {

    List<Bid> findAllByAuctionIdOrderByTimestampDesc(UUID auctionId);
}
