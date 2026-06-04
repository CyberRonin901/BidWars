package com.cyberronin.auctionservice.service;

import com.cyberronin.auctionservice.dto.AuctionCreatedEventDTO;
import com.cyberronin.auctionservice.dto.AuctionStatusUpdateEventDTO;
import com.cyberronin.auctionservice.dto.CreateAuctionRequestDTO;
import com.cyberronin.auctionservice.dto.UserDetailsResponseDTO;
import com.cyberronin.auctionservice.feign.client.AuctionStorageServiceInterface;
import com.cyberronin.auctionservice.feign.client.UserServiceInterface;
import com.cyberronin.auctionservice.feign.dto.UserResponseDTO;
import com.cyberronin.auctionservice.model.Auction;
import com.cyberronin.auctionservice.model.AuctionStatus;
import com.cyberronin.auctionservice.producer.RabbitMQProducer;
import com.cyberronin.auctionservice.repo.*;
import com.cyberronin.auctionservice.util.AuctionMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${websocket.destination}")
    private String WEBSOCKET_DESTINATION;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuctionService.class);

    private final UserServiceInterface userServiceInterface;
    private final AuctionStorageServiceInterface auctionStorageServiceInterface;

    public Auction createAuction(UUID sellerId, CreateAuctionRequestDTO reqObj)
    {
         UserResponseDTO sellerDetails = userServiceInterface.getUserDetails(sellerId);

        long now = Instant.now().toEpochMilli();
        long ttl = reqObj.expiresIn() * 1000;
        long expiresAt = ttl + now;

         Auction auction = Auction.builder()
                 .id(UUID.randomUUID())
                 .createdAt(now)
                 .expiresAt(expiresAt)
                 .status(AuctionStatus.ACTIVE)
                 .sellerId(sellerId)
                 .sellerName(sellerDetails.username())
                 .sellerLocation(sellerDetails.location())
                 .itemName(reqObj.itemName())
                 .itemDescription(reqObj.itemDescription())
                 .itemImageUrl(reqObj.itemImageUrl())
                 .startingAmount(reqObj.startingAmount())
                 .build();

        Map<String, String> auctionMap = AuctionMapper.toMap(auction);

        auctionRepo.save(auction.getId(), auctionMap, ttl);
        bidRepo.setExpiry(auction.getId(), ttl);

        activeAuctionsRepo.addAuction(auction.getId());

        rabbitMQProducer.auctionCreation(
                new AuctionCreatedEventDTO(
                        auction.getId(),
                        auction.getCreatedAt(),
                        auction.getExpiresAt(),
                        auction.getStatus(),
                        auction.getSellerId(),
                        auction.getSellerName(),
                        auction.getSellerLocation(),
                        auction.getItemName(),
                        auction.getItemDescription(),
                        auction.getItemImageUrl(),
                        auction.getStartingAmount()
                )
        );

        rabbitMQProducer.auctionExpire(auction.getId(), ttl);

        return auction;
    }

    public Auction getAuctionById(UUID id)
    {
        Map<String, String> auctionMap = auctionRepo.getById(id);

        if (auctionMap.isEmpty()){
            var auctionDto = auctionStorageServiceInterface.getAuctionById(id);
            if (auctionDto == null)
                    throw new RuntimeException("Auction Not found : " + id);
            var sellerDetails = userServiceInterface.getUserDetails(auctionDto.sellerId());
            return Auction.builder()
                    .id(auctionDto.id())
                    .createdAt(auctionDto.createdAt())
                    .expiresAt(auctionDto.expiresAt())
                    .status(auctionDto.status())
                    .sellerId(auctionDto.sellerId())
                    .sellerName(sellerDetails.username())
                    .sellerLocation(sellerDetails.location())
                    .itemName(auctionDto.itemName())
                    .itemDescription(auctionDto.itemDescription())
                    .itemImageUrl(auctionDto.itemImageUrl())
                    .startingAmount(auctionDto.startingAmount())
                    .highestBidAmount(auctionDto.highestBidAmount())
                    .highestBidUserId(auctionDto.highestBidUserId())
                    .highestBidTimestamp(auctionDto.highestBidTimestamp())
                    .build();
        }
        else
            return AuctionMapper.toAuction(auctionMap);
    }

    public List<Auction> getAllActive()
    {
        Set<UUID> activeAuctionsIDs = activeAuctionsRepo.getAllActiveAuctions();
        return activeAuctionsIDs.stream()
                .map(this::getAuctionById)
                .filter(auction -> auction != null && auction.getStatus() == AuctionStatus.ACTIVE)
                .toList();
    }

    public void cancelAuction(UUID id)
    {
        auctionRepo.updateStatus(id, AuctionStatus.CANCELLED);
        activeAuctionsRepo.removeAuction(id);

        String destination = WEBSOCKET_DESTINATION + id;
        try {
            var auctionStatusDTO = new AuctionStatusUpdateEventDTO(id, AuctionStatus.ENDED);
            messagingTemplate.convertAndSend(destination, auctionStatusDTO);
        } catch (Exception e) {
            LOGGER.error("Failed websocket notification", e);
        }

        rabbitMQProducer.auctionStatusUpdate(
                new AuctionStatusUpdateEventDTO(
                    id,
                    AuctionStatus.CANCELLED
                )
        );
    }

    public UserDetailsResponseDTO getSellerDetails(UUID auctionId)
    {
        UUID sellerId = auctionRepo.getSellerId(auctionId);

        if (sellerId == null){
            sellerId = auctionStorageServiceInterface.getSellerId(auctionId);

            if (sellerId == null)
                throw new RuntimeException("Cannot find seller details for auction : " + auctionId);

            var sellerDetails = userServiceInterface.getUserDetails(sellerId);
            return new UserDetailsResponseDTO(
                    sellerDetails.username(),
                    sellerDetails.mobile(),
                    sellerDetails.location()
            );
        }

        var sellerDetails = userServiceInterface.getUserDetails(sellerId);

        return new UserDetailsResponseDTO(
                sellerDetails.username(),
                sellerDetails.mobile(),
                sellerDetails.location()
        );
    }

    public UserDetailsResponseDTO getHighestBidderDetails(UUID auctionId)
    {
        UUID highestBidderId = auctionRepo.getHighestBidderId(auctionId);

        if (highestBidderId == null){
            highestBidderId = auctionStorageServiceInterface.getHighestBidderId(auctionId);
            if (highestBidderId == null)
                throw new RuntimeException("Cannot find highest bidder details for auction : " + auctionId);
            var userDetails = userServiceInterface.getUserDetails(highestBidderId);
            return new UserDetailsResponseDTO(
                    userDetails.username(),
                    userDetails.mobile(),
                    userDetails.location()
            );
        }

        UserResponseDTO userDetails = userServiceInterface.getUserDetails(highestBidderId);

        return new UserDetailsResponseDTO(
                userDetails.username(),
                userDetails.mobile(),
                userDetails.location()
        );
    }
}
