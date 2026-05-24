package com.cyberronin.auctionservice.feign.client;

import com.cyberronin.auctionservice.feign.dto.AuctionResponseDTO;
import com.cyberronin.auctionservice.feign.dto.UpdateHighestBidReqDTO;
import com.cyberronin.auctionservice.model.Auction;
import com.cyberronin.auctionservice.model.Bid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "AUCTION-STORAGE-SERVICE", path = "/storage")
public interface AuctionStorageInterface {

    @PostMapping("/auction/save")
    Void saveAuction(@RequestBody Auction reqObj);

    @GetMapping("/auction/get/{id}")
    AuctionResponseDTO getAuctionById(@PathVariable("id") UUID auctionId);

    @PostMapping("/auction/updateHighestBid/{id}")
    Void updateHighestBid(@PathVariable("id") UUID auctionId, @RequestBody UpdateHighestBidReqDTO reqObj);

    @PostMapping("/bid/save/{id}")
    Void save(@RequestBody Bid requestDTO, @PathVariable("id") UUID auctionId);

    @GetMapping("/bid/bidHistory/{id}")
    List<Bid> getBidHistory(@PathVariable("id") UUID auctionId);
}
