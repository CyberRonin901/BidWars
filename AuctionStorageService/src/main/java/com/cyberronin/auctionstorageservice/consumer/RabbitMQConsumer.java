package com.cyberronin.auctionstorageservice.consumer;

import com.cyberronin.auctionstorageservice.dto.*;
import com.cyberronin.auctionstorageservice.model.AuctionStatus;
import com.cyberronin.auctionstorageservice.service.AuctionService;
import com.cyberronin.auctionstorageservice.service.BidService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final AuctionService auctionService;
    private final BidService bidService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = {"${rabbitmq.queue.auction.creation}"})
    public void createAuction(AuctionCreatedEventDTO auction)
    {
        LOGGER.info("Consume auction creation event -> {}", auction);
        auctionService.save(auction);
    }

    @RabbitListener(queues = {"${rabbitmq.queue.auction.expire}"})
    public void expireAuction(UUID auctionId)
    {
        LOGGER.info("Consume auction expire event -> {}", auctionId);
        IO.println("Performing expiry task : " + auctionId);
        auctionService.updateStatus(auctionId, AuctionStatus.ENDED);
    }

    @RabbitListener(queues = {"${rabbitmq.queue.auction.status.update}"})
    public void auctionStatusUpdate(AuctionStatusUpdateEventDTO dto)
    {
        LOGGER.info("Consume auction status update event -> {}", dto);
        auctionService.updateStatus(dto.id(), dto.status());
    }

    @RabbitListener(queues = {"${rabbitmq.queue.auction.update}"})
    public void auctionUpdateHighestBid(AuctionHighestBidUpdateEventDTO dto)
    {
        LOGGER.info("Consume auction highest bid update event -> {}", dto);
        auctionService.updateHighestBid(dto);
    }

    @RabbitListener(queues = {"${rabbitmq.queue.bid.placed}"})
    public void bidPlaced(BidPlacedEventDTO dto)
    {
        LOGGER.info("Consume bid placed event -> {}", dto);
        bidService.save(dto);
    }
}
