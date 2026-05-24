package com.cyberronin.userservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder // Added for easier object creation in Service
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    private UUID id;
    private String username;
    private String mobile;
    private String location;

    @Column(name = "password_hash")
    private String passwordHash; // BCRYPT HASH

    private String role;

    @Column(name = "created_at")
    private Instant createdAt;
}