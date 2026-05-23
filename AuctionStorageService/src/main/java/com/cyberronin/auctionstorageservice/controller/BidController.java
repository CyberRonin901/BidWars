package com.cyberronin.auctionstorageservice.controller;

import com.cyberronin.auctionstorageservice.dto.SaveBidReqDTO;
import com.cyberronin.auctionstorageservice.dto.UpdateHighestBidReqDTO;
import com.cyberronin.auctionstorageservice.model.Auction;
import com.cyberronin.auctionstorageservice.model.Bid;
import com.cyberronin.auctionstorageservice.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/storage/bid")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping("/save/{id}")
    public Mono<Bid> save(@RequestBody SaveBidReqDTO requestDTO, @PathVariable("id") UUID auctionId){
        return bidService.save(requestDTO, auctionId);
    }

    @GetMapping("/bidHistory/{id}")
    public Flux<Bid> getBidHistory(@PathVariable("id") UUID auctionId){
        return bidService.getBidHistory(auctionId);
    }
}
