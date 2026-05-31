package com.cyberronin.auctionservice.producer;

import com.cyberronin.auctionservice.dto.AuctionHighestBidUpdateDTO;
import com.cyberronin.auctionservice.dto.AuctionStatusUpdateDTO;
import com.cyberronin.auctionservice.dto.BidDTO;
import com.cyberronin.auctionservice.dto.BidPlacedEventDTO;
import com.cyberronin.auctionservice.model.Auction;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RabbitMQProducer
{
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    // --- Auction Creation ---
    @Value("${rabbitmq.routing-key.auction.creation}")
    private String auctionCreationRoutingKey;

    // --- Auction Expiration ---
    @Value("${rabbitmq.routing-key.auction.expire}")
    private String auctionExpireRoutingKey;

    // --- Auction Status Update ---
    @Value("${rabbitmq.routing-key.auction.status.update}")
    private String auctionStatusUpdateRoutingKey;

    // --- Auction Highest Bid Update ---
    @Value("${rabbitmq.routing-key.auction.update}")
    private String auctionUpdateRoutingKey;

    // --- Bid Placed ---
    @Value("${rabbitmq.routing-key.bid.placed}")
    private String bidPlacedRoutingKey;

    private final RabbitTemplate rabbitTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    public void auctionCreation(Auction auction)
    {
        LOGGER.info("Auction Created Event -> {}", auction);

        rabbitTemplate.convertAndSend(
                exchangeName,
                auctionCreationRoutingKey,
                auction
        );
    }

    public void auctionExpire(UUID auctionId, long ttl){
        if (ttl <= 0) {
            throw new IllegalArgumentException("TTL must be positive");
        }

        LOGGER.info("Auction Expire Event -> {}, delay : {}s", auctionId, ttl / 1000);

        rabbitTemplate.convertAndSend(
                exchangeName,
                auctionExpireRoutingKey,
                auctionId,
                message -> {
                    message.getMessageProperties().setDelayLong(ttl);
                    return message;
                    }
                );
    }

    public void auctionStatusUpdate(AuctionStatusUpdateDTO auctionStatusDTO)
    {
        LOGGER.info("Auction Status Update Event -> {}", auctionStatusDTO);

        rabbitTemplate.convertAndSend(
                exchangeName,
                auctionStatusUpdateRoutingKey,
                auctionStatusDTO
        );
    }

    public void auctionHighestBidUpdate(AuctionHighestBidUpdateDTO auctionUpdateDto)
    {
        LOGGER.info("Auction Highest Bid Update Event -> {}", auctionUpdateDto);

        rabbitTemplate.convertAndSend(
                exchangeName,
                auctionUpdateRoutingKey,
                auctionUpdateDto
        );
    }

    public void bidPlaced(BidPlacedEventDTO bid)
    {
        LOGGER.info("Bid Placed Event -> {}", bid);

        rabbitTemplate.convertAndSend(
                exchangeName,
                bidPlacedRoutingKey,
                bid
        );
    }
}

