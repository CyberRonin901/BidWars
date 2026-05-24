package com.cyberronin.auctionservice.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BidSortedSetRepo {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "bids:";

    // ZADD: Add a bid; returns primitive Boolean indicating if it's a new element
    public Boolean addBid(UUID auctionId, String userId, double bidAmount) {
        String key = KEY_PREFIX + auctionId.toString();
        return redisTemplate.opsForZSet().add(key, userId, bidAmount);
    }
}