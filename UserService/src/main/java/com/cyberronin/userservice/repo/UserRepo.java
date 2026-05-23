package com.cyberronin.userservice.repo;

import com.cyberronin.userservice.model.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepo extends R2dbcRepository<User, UUID> {

}
