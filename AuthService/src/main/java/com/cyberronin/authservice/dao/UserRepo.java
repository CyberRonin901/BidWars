package com.cyberronin.authservice.dao;

import com.cyberronin.authservice.model.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepo extends R2dbcRepository<User, UUID>
{
    Mono<User> findUserByUsername(String username);
}
