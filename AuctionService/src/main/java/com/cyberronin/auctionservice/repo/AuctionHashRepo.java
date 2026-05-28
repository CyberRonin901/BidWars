package com.cyberronin.auctionservice.repo;

import com.cyberronin.auctionservice.util.Prefix;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.cyberronin.auctionservice.model.AuctionStatus;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class AuctionHashRepo {

    private final StringRedisTemplate redisTemplate;
    private final String KEY_PREFIX = Prefix.AUCTION_HASH;

    // HSET: Save or Update auction details synchronously
    public void save(UUID auctionId, Map<String, String> auctionData, long ttl)
    {
        String key = KEY_PREFIX + auctionId.toString();
        redisTemplate.opsForHash()
                .putAll(key, auctionData);

        if (ttl > 0) {
            redisTemplate.expire(key, ttl, TimeUnit.MILLISECONDS);
        }
    }

    // HGETALL: Fetch current real-time auction state instantly
    public Map<String, String> getById(UUID auctionId)
    {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(KEY_PREFIX + auctionId.toString());
    }

    // Update only the status of an auction
    public void updateStatus(UUID auctionId, AuctionStatus status)
    {
        String key = KEY_PREFIX + auctionId.toString();
        redisTemplate.opsForHash().put(key, "status", status.name());
    }

    // get seller userID
    public UUID getSellerId(UUID auctionId) {
        String key = KEY_PREFIX + auctionId.toString();

        String sellerIdStr = (String) redisTemplate.opsForHash().get(key, "sellerId");

        if (sellerIdStr == null) {
            throw new RuntimeException("Auction or SellerId not found");
        }
        return UUID.fromString(sellerIdStr);
    }

    // get highest bidder userID
    public UUID getHighestBidderId(UUID auctionId) {
        String key = KEY_PREFIX + auctionId.toString();

        String userIdStr = (String) redisTemplate.opsForHash().get(key, "highestBidUserId");

        if (userIdStr == null) {
            throw new RuntimeException("Auction or SellerId not found");
        }
        return UUID.fromString(userIdStr);
    }
}