package com.cyberronin.userservice.repo;

import com.cyberronin.userservice.model.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepo extends R2dbcRepository<User, UUID>
{
    Mono<User> findUserByUsername(String username);
}
