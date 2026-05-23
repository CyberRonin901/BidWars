package com.cyberronin.auctionservice.repo;

import com.cyberronin.auctionservice.model.Auction;
import com.cyberronin.auctionservice.model.AuctionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AuctionRepo {

    private static final String PREFIX = "auction:";

    private final ReactiveStringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public Mono<Auction> save(Auction auction)
    {
        String key = PREFIX + auction.getId();

        Map<String, String> fields =
                objectMapper.convertValue(
                        auction,
                        new TypeReference<Map<String, String>>() {}
                );

        return redisTemplate.opsForHash()
                .putAll(key, fields)
                .thenReturn(auction);
    }

    public Mono<Auction> findById(UUID id)
    {
        String key = PREFIX + id;

        return redisTemplate.opsForHash()
                .entries(key)
                .collectMap(
                        e -> e.getKey().toString(),
                        e -> e.getValue().toString()
                )
                .flatMap(map -> {
                    if (map.isEmpty()) {
                        return Mono.empty();
                    }
                    return Mono.just(
                            objectMapper.convertValue(map, Auction.class)
                    );
                });
    }

    // fields not in createAuction that need to be updated on event:
    /*
    sellerName
    sellerLocation
    status
    */
    public Mono<Boolean> updateAtCreationEvent(
            UUID auctionId,
            String sellerName,
            String sellerLocation,
            AuctionStatus status
    ) {

        String key = PREFIX + auctionId;

        Map<String, String> updates = Map.of(
                "sellerName", sellerName,
                "sellerLocation", sellerLocation,
                "status", status.name()
        );

        return redisTemplate.opsForHash()
                .putAll(key, updates);
    }
}
