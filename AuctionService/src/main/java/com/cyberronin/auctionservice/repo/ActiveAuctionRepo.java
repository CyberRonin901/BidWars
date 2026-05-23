package com.cyberronin.auctionservice.repo;

import com.cyberronin.auctionservice.model.Auction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ActiveAuctionRepo {

    private final ReactiveStringRedisTemplate redisTemplate;
    private final AuctionRepo auctionRepo;

    public Flux<Auction> findAllActive()
    {
        return redisTemplate.opsForSet()
                .members("auctions:active")
                .cast(String.class)
                .map(UUID::fromString)
                .flatMap(auctionRepo::findById);
    }

    public Mono<Long> addActiveAuction(UUID auctionId)
    {
        return redisTemplate.opsForSet().add("auctions:active", auctionId.toString());
    }

    public Mono<Long> removeActiveAuction(UUID auctionId)
    {
        return redisTemplate.opsForSet().remove("auctions:active", auctionId.toString());
    }
}
