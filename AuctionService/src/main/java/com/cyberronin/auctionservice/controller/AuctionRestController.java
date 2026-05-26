package com.cyberronin.auctionservice.controller;

import com.cyberronin.auctionservice.dto.CreateAuctionRequestDTO;
import com.cyberronin.auctionservice.model.Auction;
import com.cyberronin.auctionservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
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

    @GetMapping("/get/{id}")
    public ResponseEntity<Auction> getAuctionById(@PathVariable UUID id){
        Auction auction = service.getAuctionById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(auction);
    }

    @GetMapping("/get-all-active")
    public ResponseEntity<List<Auction>> getAllActiveAuctions(){
        return ResponseEntity.ok(service.getAllActive());
    }
}
