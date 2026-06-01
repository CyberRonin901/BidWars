package com.cyberronin.auctionstorageservice.controller;

import com.cyberronin.auctionstorageservice.model.Auction;
import com.cyberronin.auctionstorageservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/storage/auction")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

//    @PostMapping("/save")
//    public ResponseEntity<Void> saveAuction(@RequestBody SaveAuctionReqDTO reqObj){
//        auctionService.save(reqObj);
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/get/{id}")
    public Auction getAuctionById(@PathVariable("id") UUID auctionId){
        return auctionService.getAuctionById(auctionId);
    }

    @GetMapping("/get/seller-id/{auctionId}")
    public UUID getSellerId(@PathVariable UUID auctionId){
        return auctionService.getSellerId(auctionId);
    }

    @GetMapping("/get/highest-bidder-id/{auctionId}")
    public UUID getHighestBidderId(@PathVariable UUID auctionId){
        return auctionService.getHighestBidderId(auctionId);
    }

//    @PostMapping("/updateHighestBid/{id}")
//    public ResponseEntity<Void> updateHighestBid(@RequestBody AuctionHighestBidUpdateDTO reqObj)
//    {
//        auctionService.updateHighestBid(reqObj);
//        return ResponseEntity.ok().build();
//    }
}
