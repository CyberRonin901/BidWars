package com.cyberronin.auctionservice.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class AuctionHashRepo {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "auction:";

    // HSET: Save or Update auction details synchronously
    public void save(UUID auctionId, Map<String, String> auctionData, long ttl)
    {
        String key = KEY_PREFIX + auctionId.toString();
        redisTemplate.opsForHash()
                .putAll(key, auctionData);

        if (ttl > 0)
            redisTemplate.expire(key, ttl, TimeUnit.MILLISECONDS);
    }

    // HGETALL: Fetch current real-time auction state instantly
    public Map<String, String> getById(UUID auctionId)
    {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(KEY_PREFIX + auctionId.toString());
    }
}