package com.cyberronin.auctionservice.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
public class AuctionWebSocketController {

    /**
     * Map inbound messages to: /app/auction/{auctionId}/placeBid
     * Outbound broadcast routes to: /topic/auction/{auctionId}
     */
//    @MessageMapping("/auction/{auctionId}/placeBid")
//    @SendTo("/topic/auction/{auctionId}")
//    public BidResponse placeBid(@DestinationVariable String auctionId, BidPayload payload) {
//        System.out.println("Processing bid for Auction [" + auctionId + "] by " + payload.getBidderName() + ": $" + payload.getAmount());
//
//        // Handle core validation/service layer saving mechanics here
//
//        Instant currentTime = Instant.now();
//        return new BidResponse(payload.getBidderName(), payload.getAmount(), currentTime);
//    }
}