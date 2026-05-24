package com.cyberronin.auctionstorageservice.service;

import com.cyberronin.auctionstorageservice.dto.SaveAuctionReqDTO;
import com.cyberronin.auctionstorageservice.dto.UpdateHighestBidReqDTO;
import com.cyberronin.auctionstorageservice.model.Auction;
import com.cyberronin.auctionstorageservice.repo.AuctionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepo auctionRepo;

    public Auction save(SaveAuctionReqDTO reqObj) {
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
                .build();

        return auctionRepo.save(auction);
    }

    public Auction getAuctionById(UUID auctionId) {
        return auctionRepo.findById(auctionId).orElse(null);
    }

    public Auction updateHighestBid(UUID auctionId, UpdateHighestBidReqDTO reqObj) {
        Auction existingAuction = auctionRepo.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found with ID: " + auctionId));

        existingAuction.setHighestBidAmount(reqObj.highestBidAmount());
        existingAuction.setHighestBidUserId(reqObj.highestBidUserId());
        existingAuction.setHighestBidTimestamp(reqObj.highestBidTimestamp());

        return auctionRepo.save(existingAuction);
    }
}
