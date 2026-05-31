package com.cyberronin.auctionservice.service;

import com.cyberronin.auctionservice.dto.AuctionStatusUpdateDTO;
import com.cyberronin.auctionservice.dto.CreateAuctionRequestDTO;
import com.cyberronin.auctionservice.dto.UserDetailsResponseDTO;
import com.cyberronin.auctionservice.feign.client.UserServiceInterface;
import com.cyberronin.auctionservice.feign.dto.UserResponseDTO;
import com.cyberronin.auctionservice.model.Auction;
import com.cyberronin.auctionservice.model.AuctionStatus;
import com.cyberronin.auctionservice.producer.RabbitMQProducer;
import com.cyberronin.auctionservice.repo.*;
import com.cyberronin.auctionservice.util.AuctionMapper;
import lombok.RequiredArgsConstructor;
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
    private final RabbitMQProducer rabbitMQProducer;

    private final UserServiceInterface userServiceInterface;

    public Auction createAuction(CreateAuctionRequestDTO reqObj)
    {
         UserResponseDTO sellerDetails = userServiceInterface.getUserDetails(reqObj.sellerId());

        long now = Instant.now().toEpochMilli();
        long expiresAt = reqObj.expiresIn() * 1000 + now;

         Auction auction = Auction.builder()
                 .id(UUID.randomUUID())
                 .createdAt(now)
                 .expiresAt(expiresAt)
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

        long ttl = reqObj.expiresIn() * 1000;

        auctionRepo.save(auction.getId(), auctionMap, ttl);
        bidRepo.setExpiry(auction.getId(), ttl);

        activeAuctionsRepo.addAuction(auction.getId());

        rabbitMQProducer.auctionCreation(auction);
        rabbitMQProducer.auctionExpire(auction.getId(), ttl);

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

    public void cancelAuction(UUID id) {
        auctionRepo.updateStatus(id, AuctionStatus.CANCELLED);
        activeAuctionsRepo.removeAuction(id);

        rabbitMQProducer.auctionStatusUpdate(
                new AuctionStatusUpdateDTO(
                    id,
                    AuctionStatus.CANCELLED
                )
        );
    }

    public UserDetailsResponseDTO getSellerDetails(UUID auctionId) {
        UUID sellerId = auctionRepo.getSellerId(auctionId);

        UserResponseDTO sellerDetails = userServiceInterface.getUserDetails(sellerId);

        return new UserDetailsResponseDTO(
                sellerDetails.username(),
                sellerDetails.mobile(),
                sellerDetails.location()
        );
    }

    public UserDetailsResponseDTO getHighestBidderDetails(UUID auctionId) {
        UUID highestBidderId = auctionRepo.getHighestBidderId(auctionId);

        UserResponseDTO userDetails = userServiceInterface.getUserDetails(highestBidderId);

        return new UserDetailsResponseDTO(
                userDetails.username(),
                userDetails.mobile(),
                userDetails.location()
        );
    }
}
