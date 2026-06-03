package com.cyberronin.auctionservice.consumer;

import com.cyberronin.auctionservice.dto.AuctionStatusUpdateEventDTO;
import com.cyberronin.auctionservice.model.AuctionStatus;
import com.cyberronin.auctionservice.producer.RabbitMQProducer;
import com.cyberronin.auctionservice.repo.ActiveAuctionsSetRepo;
import com.cyberronin.auctionservice.repo.AuctionHashRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final ActiveAuctionsSetRepo activeAuctionsRepo;
    private final RabbitTemplate rabbitTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${websocket.destination}")
    private String WEBSOCKET_DESTINATION;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing-key.auction.status.update}")
    private String auctionStatusUpdateRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    // this consumer is subscribed to the given queues and performs some task when data in those queues
//    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
//    public void consume(String message){
//        LOGGER.info("Message Recieved -> {}", message);
//    }

    @RabbitListener(queues = {"${rabbitmq.queue.auction.expire}"})
    public void auctionExpire(UUID id){
        // remove uuid from active auction
        LOGGER.info("Auction {} expired", id);
        activeAuctionsRepo.removeAuction(id);

        var auctionStatusDTO = new AuctionStatusUpdateEventDTO(id, AuctionStatus.ENDED);
        LOGGER.info("Auction Status Update Event -> {}", auctionStatusDTO);

        String destination = WEBSOCKET_DESTINATION + id;
        try {
            messagingTemplate.convertAndSend(destination, auctionStatusDTO);
        } catch (Exception e) {
            LOGGER.error("Failed websocket notification", e);
        }

        rabbitTemplate.convertAndSend(
                exchangeName,
                auctionStatusUpdateRoutingKey,
                auctionStatusDTO
        );
    }
}
