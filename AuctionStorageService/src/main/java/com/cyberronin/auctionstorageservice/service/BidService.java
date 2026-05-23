package com.cyberronin.auctionstorageservice.service;

import com.cyberronin.auctionstorageservice.dto.SaveBidReqDTO;
import com.cyberronin.auctionstorageservice.dto.UpdateHighestBidReqDTO;
import com.cyberronin.auctionstorageservice.model.Auction;
import com.cyberronin.auctionstorageservice.model.Bid;
import com.cyberronin.auctionstorageservice.repo.BidRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepo bidRepo;

    public Mono<Bid> save(SaveBidReqDTO reqObj, UUID auctionId) {
        Bid bid = Bid.builder()
                .id(reqObj.id())
                .auctionId(auctionId)
                .userId(reqObj.userId())
                .amount(reqObj.amount())
                .timestamp(reqObj.timestamp())
                .isNewRecord(true) // Forces R2DBC SQL INSERT
                .build();

        return bidRepo.save(bid);
    }

    public Flux<Bid> getBidHistory(UUID auctionId) {
        return bidRepo.findAllByAuctionIdOrderByTimestampDesc(auctionId);
    }
}