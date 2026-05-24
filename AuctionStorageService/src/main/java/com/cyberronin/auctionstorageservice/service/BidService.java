package com.cyberronin.auctionstorageservice.service;

import com.cyberronin.auctionstorageservice.dto.BidDTO;
import com.cyberronin.auctionstorageservice.model.Bid;
import com.cyberronin.auctionstorageservice.repo.BidRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepo bidRepo;

    public Bid save(BidDTO reqObj, UUID auctionId) {
        Bid bid = Bid.builder()
                .id(reqObj.id())
                .auctionId(auctionId)
                .userId(reqObj.userId())
                .amount(reqObj.amount())
                .timestamp(reqObj.timestamp())
                .build();

        return bidRepo.save(bid);
    }

    public List<BidDTO> getBidHistory(UUID auctionId) {
        return bidRepo.findAllByAuctionIdOrderByTimestampDesc(auctionId)
                .stream()
                .map(bid -> new BidDTO(
                        bid.getId(),
                        bid.getUserId(),
                        bid.getAmount(),
                        bid.getTimestamp()
                ))
                .collect(Collectors.toList());
    }
}