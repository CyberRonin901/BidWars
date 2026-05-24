package com.cyberronin.userservice.controller;

import com.cyberronin.userservice.model.User;
import com.cyberronin.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService service;

    @GetMapping("/getDetails")
    public ResponseEntity<User> getUserDetails (@RequestHeader("X-User-Id") UUID userId){
        User user = service.getUserById(userId);
        return ResponseEntity.ok(user);
    }
}
