package com.cyberronin.auctionservice.service;

import com.cyberronin.auctionservice.dto.CreateAuctionRequestDTO;
import com.cyberronin.auctionservice.model.Auction;
import com.cyberronin.auctionservice.model.AuctionStatus;
import com.cyberronin.auctionservice.repo.ActiveAuctionRepo;
import com.cyberronin.auctionservice.repo.AuctionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionService
{
    private final AuctionRepo auctionRepo;
    private final ActiveAuctionRepo activeAuctionRepo;

    // TODO: saving creates an event to the StorageService to store data
    // TODO: make call to UserService to get full data of seller and change status to active and update Active list
    public Mono<Auction> createAuction(CreateAuctionRequestDTO reqObj)
    {
        Auction auction = new Auction();
        auction.setId(UUID.randomUUID());
        auction.setCreatedAt(Instant.now().toEpochMilli());
        auction.setExpiresAt(reqObj.expiresAt().toEpochMilli());
        auction.setStatus(AuctionStatus.PENDING);
        auction.setSellerId(reqObj.sellerId());
        auction.setItemName(reqObj.itemName());
        auction.setItemDescription(reqObj.itemDescription());
        auction.setItemImageUrl(reqObj.itemImageUrl());
        auction.setStartingAmount(reqObj.startingAmount());

        return auctionRepo.save(auction);
    }

    public Flux<Auction> getAllLiveAuctions(){
        return activeAuctionRepo.findAllActive();
    }

    // TODO: it the auction status is not ACTIVE then fetch from StorageService if that also empty then throw error
    public Mono<Auction> getAuctionById(UUID id){
        return auctionRepo.findById(id);
    }

    public Mono<Long> addActiveAuction(UUID id){
        return activeAuctionRepo.addActiveAuction(id);
    }

    public Mono<Long> removeActiveAuction(UUID id){
        return activeAuctionRepo.removeActiveAuction(id);
    }
}
