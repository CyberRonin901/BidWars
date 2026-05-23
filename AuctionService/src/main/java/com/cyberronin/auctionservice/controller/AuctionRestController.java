package com.cyberronin.auctionservice.controller;

import com.cyberronin.auctionservice.dto.CreateAuctionRequestDTO;
import com.cyberronin.auctionservice.model.Auction;
import com.cyberronin.auctionservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping ("/auction")
@RequiredArgsConstructor
public class AuctionRestController
{
    private final AuctionService service;

    @PostMapping("/create")
    public Mono<ResponseEntity<Auction>> createAuction(@RequestBody CreateAuctionRequestDTO reqObj)
    {
        return service.createAuction(reqObj)
            .map(createdAuction ->
                ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(createdAuction)
            );
    }

    @GetMapping("/getAuction/{id}")
    public Mono<Auction> getLiveAuctionById(@PathVariable UUID id)
    {
        return service.getAuctionById(id);
    }

//    @GetMapping("/getLiveAuctions")
//    public Flux<List<Auction>> getAllLiveAuctions()
//    {
//        return service.getAllLiveAuctions();
//    }
}
