package com.cyberronin.auctionservice.service;

import com.cyberronin.auctionservice.dto.CreateAuctionRequestDTO;
import com.cyberronin.auctionservice.httpExchangeClient.UserServiceExchangeClient;
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
    private final UserServiceExchangeClient userClient;

    // TODO: saving creates an event to the StorageService to store data
    public Mono<Auction> createAuction(CreateAuctionRequestDTO reqObj) {
        return userClient.getUserDetails(reqObj.sellerId())
                // if user doesn't exist
                .switchIfEmpty(Mono.error(new RuntimeException("User profile not found")))

                // Map and build the execution payload once user data arrives
                .flatMap(userProfile -> {
                    Auction auction = Auction.builder()
                            .id(UUID.randomUUID())
                            .createdAt(Instant.now().toEpochMilli())
                            .expiresAt(reqObj.expiresAt().toEpochMilli())
                            .status(AuctionStatus.ACTIVE)
                            .sellerId(reqObj.sellerId())
                            .sellerName(userProfile.username())
                            .sellerLocation(userProfile.location())
                            .itemName(reqObj.itemName())
                            .itemDescription(reqObj.itemDescription())
                            .itemImageUrl(reqObj.itemImageUrl())
                            .startingAmount(reqObj.startingAmount())
                            .build();

                    return auctionRepo.save(auction);
                })
                .flatMap(savedAuction -> activeAuctionRepo.addActiveAuction(savedAuction.getId())
                        // Maintain the reactive chain pipeline by returning the Auction object
                        .thenReturn(savedAuction)
                );
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
