package com.cyberronin.auctionstorageservice.repo;

import com.cyberronin.auctionstorageservice.model.Bid;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@Repository
public interface BidRepo extends R2dbcRepository<Bid, UUID> {

    Flux<Bid> findAllByAuctionIdOrderByTimestampDesc(UUID auctionId);
}
