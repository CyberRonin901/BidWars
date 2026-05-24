package com.cyberronin.auctionservice.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AuctionHashRepo {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "auction:";

    // HSET: Save or Update auction details synchronously
    public void save(UUID auctionId, Map<String, String> auctionData) {
        redisTemplate.opsForHash()
                .putAll(KEY_PREFIX + auctionId.toString(), auctionData);
    }

    // HGETALL: Fetch current real-time auction state instantly
    public Map<Object, Object> getById(UUID auctionId) {
        return redisTemplate.opsForHash()
                .entries(KEY_PREFIX + auctionId.toString());
    }
}