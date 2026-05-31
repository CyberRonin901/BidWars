package com.cyberronin.auctionservice.consumer;

import com.cyberronin.auctionservice.repo.ActiveAuctionsSetRepo;
import com.cyberronin.auctionservice.repo.AuctionHashRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final ActiveAuctionsSetRepo activeAuctionsRepo;
    private final AuctionHashRepo auctionHashRepo;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    // this consumer is subscribed to the given queues and performs some task when data in those queues
//    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
//    public void consume(String message){
//        LOGGER.info("Message Recieved -> {}", message);
//    }

    @RabbitListener(queues = {"${rabbitmq.queue.auction.expire}"})
    public void auctionExpire(UUID id){
        // remove uuid from active auction
        activeAuctionsRepo.removeAuction(id);
        LOGGER.info("Auction {} expired", id);
    }
}
