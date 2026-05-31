package com.cyberronin.auctionstorageservice.service;

import com.cyberronin.auctionstorageservice.dto.AuctionHighestBidUpdateDTO;
import com.cyberronin.auctionstorageservice.dto.SaveAuctionReqDTO;
import com.cyberronin.auctionstorageservice.dto.UpdateHighestBidReqDTO;
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

    public void save(SaveAuctionReqDTO reqObj) {
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

    public void updateHighestBid(AuctionHighestBidUpdateDTO dto)
    {
        Auction existingAuction = auctionRepo.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Auction not found with ID: " + dto.id()));

        existingAuction.setHighestBidAmount(dto.highestBidAmount());
        existingAuction.setHighestBidUserId(dto.highestBidUserId());
        existingAuction.setHighestBidTimestamp(dto.highestBidTimestamp());

        auctionRepo.save(existingAuction);
    }

    @Transactional
    public void updateStatus(UUID id, AuctionStatus status) {
        auctionRepo.updateStatus(id, status);
    }
}
