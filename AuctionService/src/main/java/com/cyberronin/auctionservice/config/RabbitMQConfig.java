package com.cyberronin.auctionservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig
{
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    // --- Auction Creation ---
    @Value("${rabbitmq.queue.auction.creation}")
    private String auctionCreationQueue;

    @Value("${rabbitmq.routing-key.auction.creation}")
    private String auctionCreationRoutingKey;

    // --- Auction Expiration ---
    @Value("${rabbitmq.queue.auction.expire}")
    private String auctionExpireQueue;

    @Value("${rabbitmq.routing-key.auction.expire}")
    private String auctionExpireRoutingKey;

    // --- Auction Status Update ---
    @Value("${rabbitmq.queue.auction.status.update}")
    private String auctionStatusUpdateQueue;

    @Value("${rabbitmq.routing-key.auction.status.update}")
    private String auctionStatusUpdateRoutingKey;

    // --- Auction General Update ---
    @Value("${rabbitmq.queue.auction.update}")
    private String auctionUpdateQueue;

    @Value("${rabbitmq.routing-key.auction.update}")
    private String auctionUpdateRoutingKey;

    // --- Bid Placed ---
    @Value("${rabbitmq.queue.bid.placed}")
    private String bidPlacedQueue;

    @Value("${rabbitmq.routing-key.bid.placed}")
    private String bidPlacedRoutingKey;

    // Bean for exchange (only 1 beans serves multiple queues with different routing keys)
    @Bean
    public TopicExchange exchange(){
        var exchange = new TopicExchange(exchangeName);
        exchange.setDelayed(true);
        return exchange;
    }

    // Spring auto config will automatically make these following beans:
    // ConnectionFactory
    // RabbitTemplate
    // RabbitAdmin

    // Setup the converter for sending and receiving JSON messages to RabbitMQ
    @Bean
    public MessageConverter converter(){
        return new JacksonJsonMessageConverter();
    }

    // Set the JSON converter in the RabbitTemplate
    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory){
        RabbitTemplate  rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    // ---------------QUEUE and BINDING CONFIGURATION-------------------------------

    // Queue and binding for Auction Creation
    @Bean
    public Queue auctionCreatedQueue(){
        return new Queue(auctionCreationQueue);
    }

    @Bean
    public Binding auctionCreatedBinding(){
        return BindingBuilder
                .bind(auctionCreatedQueue())
                .to(exchange())
                .with(auctionCreationRoutingKey);
    }

    // Queue and binding for Handling auction expire inside this (auction) service
    @Bean
    public Queue auctionExpireQueue(){
        return new Queue(auctionExpireQueue);
    }

    @Bean
    public Binding auctionExpireBinding(){
        return BindingBuilder
                .bind(auctionExpireQueue())
                .to(exchange())
                .with(auctionExpireRoutingKey);
    }

    // Queue and Binding for auction status update
    @Bean
    public Queue auctionStatusUpdateQueue(){
        return new Queue(auctionStatusUpdateQueue);
    }

    @Bean
    public Binding auctionStatusUpdateBinding(){
        return BindingBuilder
                .bind(auctionStatusUpdateQueue())
                .to(exchange())
                .with(auctionStatusUpdateRoutingKey);
    }

    // Queue and binding for auction data updates like highest bid details
    @Bean
    public Queue auctionUpdateQueue(){
        return new Queue(auctionUpdateQueue);
    }

    @Bean
    public Binding auctionUpdateBinding(){
        return BindingBuilder
                .bind(auctionUpdateQueue())
                .to(exchange())
                .with(auctionUpdateRoutingKey);
    }

    // Queue and Binding for placing bids
    @Bean
    public Queue bidPlacedQueue(){
        return new Queue(bidPlacedQueue);
    }

    @Bean
    public Binding bidPlacedBinding(){
        return BindingBuilder
                .bind(bidPlacedQueue())
                .to(exchange())
                .with(bidPlacedRoutingKey);
    }
}
