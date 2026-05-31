package com.cyberronin.auctionservice.service;

import com.cyberronin.auctionservice.dto.AuctionHighestBidUpdateDTO;
import com.cyberronin.auctionservice.dto.BidDTO;
import com.cyberronin.auctionservice.dto.BidPlacedEventDTO;
import com.cyberronin.auctionservice.feign.client.UserServiceInterface;
import com.cyberronin.auctionservice.producer.RabbitMQProducer;
import com.cyberronin.auctionservice.repo.AuctionHashRepo;
import com.cyberronin.auctionservice.repo.BidSortedSetRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class BidService {

    private final AuctionHashRepo auctionRepo;
    private final BidSortedSetRepo bidRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final RabbitMQProducer rabbitMQProducer;

    public void processAndBroadcastBid(UUID auctionId, BidDTO bidDto)
    {
        long timestamp = Instant.now().toEpochMilli();

        boolean isHighestBid = bidRepo.executeBidScript(auctionId, bidDto);

//         Broadcast if and only if the Lua script confirmed it was valid and successful
        if (isHighestBid) {
            String destination = "/topic/auction/" + auctionId;
            messagingTemplate.convertAndSend(destination, bidDto);

            rabbitMQProducer.bidPlaced(
                    new BidPlacedEventDTO(
                            auctionId,
                            bidDto.userId(),
                            bidDto.amount(),
                            timestamp
                    )
            );

            rabbitMQProducer.auctionHighestBidUpdate(
                    new AuctionHighestBidUpdateDTO(
                            auctionId,
                            bidDto.userId(),
                            bidDto.amount(),
                            timestamp
                    )
            );
        }
    }
}
