package com.cyberronin.auctionservice.controller;

import com.cyberronin.auctionservice.dto.CreateAuctionRequestDTO;
import com.cyberronin.auctionservice.model.Auction;
import com.cyberronin.auctionservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auction")
@RequiredArgsConstructor
public class AuctionRestController {

    private final AuctionService service;

    @PostMapping("/create")
    public ResponseEntity<Auction> createAuction(@RequestBody CreateAuctionRequestDTO reqObj) {
        Auction createdAuction = service.createAuction(reqObj);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdAuction);
    }
}
