package com.cyberronin.auctionservice.repo;

import com.cyberronin.auctionservice.util.Prefix;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ActiveAuctionsSetRepo {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY = Prefix.ACTIVE_AUCTIONS;

    // SADD: Synchronously mark an auction as active
    public Long addAuction(UUID auctionId) {
        return redisTemplate.opsForSet().add(KEY, auctionId.toString());
    }

    // SREM: Synchronously remove an expired auction
    public Long removeAuction(UUID auctionId) {
        return redisTemplate.opsForSet().remove(KEY, auctionId.toString());
    }

    // SMEMBERS: Fetch all active items and collect into a standard Java Set
    public Set<UUID> getAllActiveAuctions() {
        Set<String> members = redisTemplate.opsForSet().members(KEY);
        if (members == null || members.isEmpty()) {
            return Collections.emptySet();
        }

        return members.stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());
    }
}