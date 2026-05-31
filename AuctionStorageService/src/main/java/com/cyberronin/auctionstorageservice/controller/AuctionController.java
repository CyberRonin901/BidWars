package com.cyberronin.auctionstorageservice.controller;

import com.cyberronin.auctionstorageservice.dto.AuctionHighestBidUpdateDTO;
import com.cyberronin.auctionstorageservice.dto.SaveAuctionReqDTO;
import com.cyberronin.auctionstorageservice.dto.UpdateHighestBidReqDTO;
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
    public ResponseEntity<Auction> getAuctionById(@PathVariable("id") UUID auctionId){
        Auction auction = auctionService.getAuctionById(auctionId);
        return ResponseEntity.ok(auction);
    }

//    @PostMapping("/updateHighestBid/{id}")
//    public ResponseEntity<Void> updateHighestBid(@RequestBody AuctionHighestBidUpdateDTO reqObj)
//    {
//        auctionService.updateHighestBid(reqObj);
//        return ResponseEntity.ok().build();
//    }
}
