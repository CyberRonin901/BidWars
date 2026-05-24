package com.cyberronin.userservice.service;

import com.cyberronin.userservice.model.User;
import com.cyberronin.userservice.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepo repo;

    public User getUserById(UUID userId) {
        return repo.findById(userId).orElse(null);
    }
}
