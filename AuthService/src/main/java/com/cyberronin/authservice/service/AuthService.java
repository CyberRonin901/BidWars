package com.cyberronin.authservice.service;

import com.cyberronin.authservice.dao.UserRepo;
import com.cyberronin.authservice.dto.TokenResponseDTO;
import com.cyberronin.authservice.dto.UserRequestDTO;
import com.cyberronin.authservice.dto.UserResponseDTO;
import com.cyberronin.authservice.exceptions.UsernameAlreadyExistsException;
import com.cyberronin.authservice.model.User;
import com.cyberronin.authservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${JWT_EXPIRATION:3600000}")
    private Long jwtExpiration;

    public AuthService(UserRepo userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Mono<UserResponseDTO> registerUser(UserRequestDTO request, String role) {
        // Map DTO to Entity
        User user = new User();
        user.setUsername(request.username());
        user.setMobile(request.mobile());
        user.setLocation(request.location());
        user.setPasswordHash(passwordEncoder.encode(request.password())); // Hash the raw password from the DTO
        user.setRole(role);
        user.setCreatedAt(Instant.now());

        return userRepository.save(user)
                .map(this::mapToResponseDTO)
                .onErrorResume(ex -> {
                    if (ex.getMessage() != null && ex.getMessage().contains("users_username_key")) {
                        return Mono.error(new UsernameAlreadyExistsException(request.username()));
                    }
                    return Mono.error(ex);
                });
    }

    public Mono<TokenResponseDTO> authenticate(String username, String rawPassword) {
        // authenticate only needs username and password for checking if user is registered
        return userRepository.findUserByUsername(username)
                .filter(u -> passwordEncoder.matches(rawPassword, u.getPasswordHash()))
                .map(u -> {
                    String token = jwtUtil.generateToken(u.getId(), u.getUsername(), u.getRole());
                    Instant expiresAt = Instant.now().plusMillis(jwtExpiration);
                    return new TokenResponseDTO(token, expiresAt);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Credentials")));
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}