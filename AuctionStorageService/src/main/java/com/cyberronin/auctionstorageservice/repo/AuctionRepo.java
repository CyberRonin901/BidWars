package com.cyberronin.auctionstorageservice.repo;

import com.cyberronin.auctionstorageservice.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuctionRepo extends JpaRepository<Auction, UUID> {
}
