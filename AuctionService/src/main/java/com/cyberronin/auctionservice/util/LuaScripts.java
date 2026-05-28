package com.cyberronin.auctionservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LuaScripts
{
    public static final String BID_VALIDATION_SCRIPT =
        """
        local bid_zset     = KEYS[1]
        local auction_hash = KEYS[2]
        
        local user_id      = ARGV[1]
        local bid_amount   = tonumber(ARGV[2])
        local current_time = ARGV[3]
        
        -- Validate against Auction Floor Price
        local starting_amount = tonumber(redis.call('HGET', auction_hash, 'startingAmount'))
        if not starting_amount or bid_amount < starting_amount then
            return 0
        end
        
        -- Validate against Current Highest Bid
        local highest_bid_record = redis.call('ZREVRANGE', bid_zset, 0, 0, 'WITHSCORES')
        if #highest_bid_record > 0 then
            local current_highest_score = tonumber(highest_bid_record[2])
            if bid_amount <= current_highest_score then
                return 0
            end
        end
        
        -- Execute Atomic Write Operations
        local unique_member = current_time .. ':' .. user_id
        redis.call('ZADD', bid_zset, bid_amount, unique_member)
        
        -- Combined multi-field HSET to minimize internal execution cycles
        redis.call('HSET', auction_hash,
            'highestBidUserId', user_id,
            'highestBidAmount', tostring(bid_amount)
        )
        
        return 1
        """;
}
