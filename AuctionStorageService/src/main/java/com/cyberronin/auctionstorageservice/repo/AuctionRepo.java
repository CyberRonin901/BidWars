package com.cyberronin.auctionstorageservice.repo;

import com.cyberronin.auctionstorageservice.model.Auction;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuctionRepo extends R2dbcRepository<Auction, UUID> {
}
