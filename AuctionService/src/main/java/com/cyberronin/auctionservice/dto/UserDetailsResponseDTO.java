package com.cyberronin.auctionservice.dto;

public record UserDetailsResponseDTO(
        String username,
        String mobile,
        String location
) {}
