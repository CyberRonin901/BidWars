package com.cyberronin.auctionservice.controller;

import com.cyberronin.auctionservice.dto.BidDTO;
import com.cyberronin.auctionservice.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class BidWebsocketController {

    // Connect to http://_____/ws
//    @MessageMapping("/send-message") // sender endpoint
//    @SendTo("/topic/notification") // receiver endpoint
//    public String sendMessage(String message){
//        IO.println(message);
//        return message;
//    }

    private final BidService bidService;

    // Client sends to: /app/place-bid/{auction-id}
    // Receive at : /topic/auction/{auctionId}
    @MessageMapping("/place-bid/{auctionId}")
    public void handleIncomingBid(@DestinationVariable UUID auctionId, BidDTO bidDto) {
        bidService.processAndBroadcastBid(auctionId, bidDto);
    }
}
