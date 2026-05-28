package com.cyberronin.auctionservice.service;

import com.cyberronin.auctionservice.dto.BidRequestDTO;
import com.cyberronin.auctionservice.dto.BidResponseDTO;
import com.cyberronin.auctionservice.feign.client.UserServiceInterface;
import com.cyberronin.auctionservice.feign.dto.UserResponseDTO;
import com.cyberronin.auctionservice.repo.AuctionHashRepo;
import com.cyberronin.auctionservice.repo.BidSortedSetRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class BidService {

    private final AuctionHashRepo auctionRepo;
    private final BidSortedSetRepo bidRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserServiceInterface userServiceInterface;

    public void processAndBroadcastBid(UUID auctionId, BidRequestDTO bidDto)
    {
        boolean isHighestBid = bidRepo.executeBidScript(auctionId, bidDto);

//         Broadcast if and only if the Lua script confirmed it was valid and successful
        if (isHighestBid) {
            String destination = "/topic/auction/" + auctionId.toString();
            UserResponseDTO user = userServiceInterface.getUserDetails(bidDto.userId());
            BidResponseDTO bidResponse = new BidResponseDTO(user.username(), bidDto.amount());
            messagingTemplate.convertAndSend(destination, bidResponse);
        }
    }
}
