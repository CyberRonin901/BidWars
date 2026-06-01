package com.cyberronin.auctionservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableRabbit
public class AuctionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionServiceApplication.class, args);
    }

}
