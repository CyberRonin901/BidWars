package com.cyberronin.auctionservice.httpExchangeClient;

import com.cyberronin.auctionservice.dto.UserResponseDTO;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@HttpExchange("/user") // Binds to the root path of the target service
public interface UserServiceExchangeClient {

    @GetExchange("/getDetails")
    Mono<UserResponseDTO> getUserDetails(@RequestHeader("X-User-Id") UUID userId);
}