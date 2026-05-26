package com.cyberronin.auctionservice.feign.client;

import com.cyberronin.auctionservice.feign.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "USER-SERVICE", path = "/user")
public interface UserServiceInterface {

    @GetMapping("/getDetails")
    UserResponseDTO getUserDetails (@RequestHeader("X-User-Id") UUID userId);
}
