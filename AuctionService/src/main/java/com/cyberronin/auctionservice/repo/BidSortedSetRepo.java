package com.cyberronin.auctionservice.repo;

import com.cyberronin.auctionservice.dto.BidDTO;
import com.cyberronin.auctionservice.util.LuaScripts;
import com.cyberronin.auctionservice.util.Prefix;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class BidSortedSetRepo {

    private final StringRedisTemplate redisTemplate;

    // ZADD: Add a bid; returns primitive Boolean indicating if it's a new element
    public Boolean addBid(UUID auctionId, String userId, double bidAmount)
    {
        String key = Prefix.BID_ZSET + auctionId.toString();
        long epochTime = Instant.now().toEpochMilli();
        String member = Long.toString(epochTime) + ":" + userId;
        return redisTemplate.opsForZSet().add(key, member, bidAmount);
    }

    public boolean setExpiry(UUID auctionId, long ttl)
    {
        String key = Prefix.BID_ZSET + auctionId.toString();
        redisTemplate.opsForZSet().add(key, "init", 0);
        if (ttl > 0) {
            return redisTemplate.expire(key, ttl, TimeUnit.MILLISECONDS);
        }
        return false;
    }

    public boolean executeBidScript(UUID auctionId, BidDTO bidDto)
    {
        String bidKey = Prefix.BID_ZSET + auctionId.toString();
        String auctionHashKey = Prefix.AUCTION_HASH + auctionId.toString();

        String luaScript = LuaScripts.BID_VALIDATION_SCRIPT;

        RedisScript<Long> script = new DefaultRedisScript<>(luaScript, Long.class);

        // Arguments: userId, amount, current epoch time
        List<String> keys = List.of(bidKey, auctionHashKey);
        Long result = redisTemplate.execute(
                script,
                keys,
                bidDto.userId().toString(),
                String.valueOf(bidDto.amount()),
                String.valueOf(Instant.now().toEpochMilli()));

        return result != null && result == 1L; // true = success | false = failure
    }
}