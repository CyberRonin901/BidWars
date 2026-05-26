package com.cyberronin.auctionservice.service;

import com.cyberronin.auctionservice.dto.CreateAuctionRequestDTO;
import com.cyberronin.auctionservice.feign.client.UserServiceInterface;
import com.cyberronin.auctionservice.feign.dto.UserResponseDTO;
import com.cyberronin.auctionservice.model.Auction;
import com.cyberronin.auctionservice.model.AuctionStatus;
import com.cyberronin.auctionservice.repo.*;
import com.cyberronin.auctionservice.util.AuctionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuctionService
{
    private final ActiveAuctionsSetRepo activeAuctionsRepo;
    private final AuctionHashRepo auctionRepo;
    private final BidSortedSetRepo bidRepo;

    private final UserServiceInterface userServiceInterface;

    public Auction createAuction(CreateAuctionRequestDTO reqObj) {

         UserResponseDTO sellerDetails = userServiceInterface.getUserDetails(reqObj.sellerId());

         Auction auction = Auction.builder()
                 .id(UUID.randomUUID())
                 .createdAt(Instant.now().toEpochMilli())
                 .expiresAt(reqObj.expiresAt())
                 .status(AuctionStatus.ACTIVE)
                 .sellerId(reqObj.sellerId())
                 .sellerName(sellerDetails.username())
                 .sellerLocation(sellerDetails.location())
                 .itemName(reqObj.itemName())
                 .itemDescription(reqObj.itemDescription())
                 .itemImageUrl(reqObj.itemImageUrl())
                 .startingAmount(reqObj.startingAmount())
                 .build();

        Map<String, String> auctionMap = AuctionMapper.toMap(auction);

        long ttl = auction.getExpiresAt() - Instant.now().toEpochMilli();
        auctionRepo.save(auction.getId(), auctionMap, ttl);
        activeAuctionsRepo.addAuction(auction.getId());

        return auction;
    }

    public Auction getAuctionById(UUID id) {
        Map<String, String> auctionMap = auctionRepo.getById(id);
        return AuctionMapper.toAuction((auctionMap));
    }

    public List<Auction> getAllActive() {
        Set<UUID> activeAuctionsIDs = activeAuctionsRepo.getAllActiveAuctions();
        return activeAuctionsIDs.stream()
                .map(this::getAuctionById)
                .filter(Objects::nonNull)
                .toList();
    }
}
