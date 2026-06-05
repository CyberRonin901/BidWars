package com.cyberronin.auctionstorageservice.service;

import com.cyberronin.auctionstorageservice.dto.AuctionCreatedEventDTO;
import com.cyberronin.auctionstorageservice.dto.AuctionHighestBidUpdateEventDTO;
import com.cyberronin.auctionstorageservice.dto.BidPlacedEventDTO;
import com.cyberronin.auctionstorageservice.model.Auction;
import com.cyberronin.auctionstorageservice.model.AuctionStatus;
import com.cyberronin.auctionstorageservice.repo.AuctionRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepo auctionRepo;

    @Transactional
    public void save(AuctionCreatedEventDTO reqObj) {
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

        auctionRepo.save(auction);
    }

    public Auction getAuctionById(UUID auctionId) {
        return auctionRepo.findById(auctionId).orElse(null);
    }

    @Transactional
    public void updateHighestBid(BidPlacedEventDTO dto)
    {
        auctionRepo.updateHighestBidderDetailsByAuctionId(
                dto.auctionId(),
                dto.userId(),
                dto.amount(),
                dto.timestamp()
        );
    }

    @Transactional
    public void updateStatus(UUID id, AuctionStatus status) {
        auctionRepo.updateStatus(id, status);
    }

    public UUID getSellerId(UUID auctionId) {
        return auctionRepo.findSellerIdByAuctionId(auctionId);
    }

    public UUID getHighestBidderId(UUID auctionId) {
        return auctionRepo.findHighestBidderIdByAuctionId(auctionId);
    }
}
