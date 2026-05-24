package com.cyberronin.auctionstorageservice.controller;

import com.cyberronin.auctionstorageservice.dto.BidDTO;
import com.cyberronin.auctionstorageservice.model.Bid;
import com.cyberronin.auctionstorageservice.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/storage/bid")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping("/save/{id}")
    public ResponseEntity<Void> save(@RequestBody BidDTO requestDTO, @PathVariable("id") UUID auctionId){
        bidService.save(requestDTO, auctionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bidHistory/{id}")
    public ResponseEntity<List<BidDTO>> getBidHistory(@PathVariable("id") UUID auctionId){
        return ResponseEntity.ok(bidService.getBidHistory(auctionId));
    }
}
