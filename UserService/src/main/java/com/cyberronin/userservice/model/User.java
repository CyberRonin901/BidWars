package com.cyberronin.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder // Added for easier object creation in Service
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {
    @Id
    private UUID id;
    private String username;
    private String mobile;
    private String location;

    @Column("password_hash")
    private String passwordHash; // BCRYPT HASH

    private String role;

    @Column("created_at")
    private Instant createdAt;
}