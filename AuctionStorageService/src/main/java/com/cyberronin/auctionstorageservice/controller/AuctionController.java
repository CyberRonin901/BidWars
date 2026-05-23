package com.cyberronin.auctionstorageservice.controller;

import com.cyberronin.auctionstorageservice.dto.SaveAuctionReqDTO;
import com.cyberronin.auctionstorageservice.dto.UpdateHighestBidReqDTO;
import com.cyberronin.auctionstorageservice.model.Auction;
import com.cyberronin.auctionstorageservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/storage/auction")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping("/save")
    public Mono<Auction> saveAuction(@RequestBody SaveAuctionReqDTO reqObj){
        return auctionService.save(reqObj);
    }

    @GetMapping("/get/{id}")
    public Mono<Auction> getAuctionById(@PathVariable("id") UUID auctionId){
        return auctionService.getAuctionById(auctionId);
    }

    @PostMapping("/updateHighestBid/{id}")
    public Mono<Auction> updateHighestBid(@PathVariable("id") UUID auctionId, @RequestBody UpdateHighestBidReqDTO reqObj)
    {
        return auctionService.updateHighestBid(auctionId, reqObj);
    }
}
