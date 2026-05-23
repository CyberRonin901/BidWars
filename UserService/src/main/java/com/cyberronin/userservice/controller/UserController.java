package com.cyberronin.userservice.controller;

import com.cyberronin.userservice.model.User;
import com.cyberronin.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService service;

    @GetMapping("/getDetails")
    public Mono<User> getUserDetails (@RequestHeader("X-User-Id") String userId){
        return service.getUserById(userId);
    }
}
