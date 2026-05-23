package com.cyberronin.auctionstorageservice.service;

import com.cyberronin.auctionstorageservice.dto.SaveAuctionReqDTO;
import com.cyberronin.auctionstorageservice.dto.UpdateHighestBidReqDTO;
import com.cyberronin.auctionstorageservice.model.Auction;
import com.cyberronin.auctionstorageservice.repo.AuctionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepo auctionRepo;

    public Mono<Auction> save(SaveAuctionReqDTO reqObj) {
        // Map the payload directly into your R2DBC execution model
        Auction auction = Auction.builder()
                .id(reqObj.id())
                .createdAt(reqObj.createdAt())
                .expiresAt(reqObj.expiresAt())
                .status(reqObj.status())
                .sellerId(reqObj.sellerId())
                .itemName(reqObj.itemName())
                .itemDescription(reqObj.itemDescription())
                .itemImageUrl(reqObj.itemImageUrl())
                .startingAmount(reqObj.startingAmount())
                .isNewRecord(true) // Explicitly flags R2DBC to issue a clean SQL INSERT statement
                .build();

        return auctionRepo.save(auction);
    }


    public Mono<Auction> getAuctionById(UUID auctionId) {
        return auctionRepo.findById(auctionId);
    }

    public Mono<Auction> updateHighestBid(UUID auctionId, UpdateHighestBidReqDTO reqObj) {
        return auctionRepo.findById(auctionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Auction not found with ID: " + auctionId)))
                .flatMap(existingAuction -> {
                    existingAuction.setHighestBidAmount(reqObj.highestBidAmount());
                    existingAuction.setHighestBidUserId(reqObj.highestBidUserId());
                    existingAuction.setHighestBidTimestamp(reqObj.highestBidTimestamp());

                    existingAuction.setNewRecord(false); // Forces R2DBC SQL UPDATE

                    return auctionRepo.save(existingAuction);
                });
    }
}
