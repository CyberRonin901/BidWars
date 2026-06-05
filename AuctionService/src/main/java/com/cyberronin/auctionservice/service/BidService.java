package com.cyberronin.auctionservice.service;

import com.cyberronin.auctionservice.dto.AuctionHighestBidUpdateEventDTO;
import com.cyberronin.auctionservice.dto.BidDTO;
import com.cyberronin.auctionservice.dto.BidPlacedEventDTO;
import com.cyberronin.auctionservice.producer.RabbitMQProducer;
import com.cyberronin.auctionservice.repo.AuctionHashRepo;
import com.cyberronin.auctionservice.repo.BidSortedSetRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BidService {

    private final AuctionHashRepo auctionRepo;
    private final BidSortedSetRepo bidRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final RabbitMQProducer rabbitMQProducer;

    @Value("${websocket.destination}")
    private String WEBSOCKET_DESTINATION;

    public void processAndBroadcastBid(UUID auctionId, BidDTO bidDto)
    {
        long timestamp = Instant.now().toEpochMilli();

        boolean isHighestBid = bidRepo.executeBidScript(auctionId, bidDto, timestamp);

//         Broadcast if and only if the Lua script confirmed it was valid and successful
        if (isHighestBid) {
            String destination = WEBSOCKET_DESTINATION + auctionId;
            messagingTemplate.convertAndSend(destination, bidDto);

            rabbitMQProducer.bidPlaced(
                    new BidPlacedEventDTO(
                            auctionId,
                            bidDto.userId(),
                            bidDto.amount(),
                            timestamp
                    )
            );

//            rabbitMQProducer.auctionHighestBidUpdate(
//                    new AuctionHighestBidUpdateEventDTO(
//                            auctionId,
//                            bidDto.userId(),
//                            bidDto.amount(),
//                            timestamp
//                    )
//            );
        }
    }
}
