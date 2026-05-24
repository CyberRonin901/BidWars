package com.cyberronin.auctionservice.service;

import com.cyberronin.auctionservice.dto.CreateAuctionRequestDTO;
import com.cyberronin.auctionservice.model.Auction;
import com.cyberronin.auctionservice.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionService
{
    private final ActiveAuctionsSetRepo activeAuctionsRepo;
    private final AuctionHashRepo auctionRepo;
    private final BidSortedSetRepo bidRepo;


    public Auction createAuction(CreateAuctionRequestDTO reqObj) {

        return null;
    }
}
