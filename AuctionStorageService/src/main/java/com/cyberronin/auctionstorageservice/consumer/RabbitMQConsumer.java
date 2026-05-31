package com.cyberronin.auctionstorageservice.consumer;

import com.cyberronin.auctionstorageservice.dto.AuctionHighestBidUpdateDTO;
import com.cyberronin.auctionstorageservice.dto.AuctionStatusUpdateDTO;
import com.cyberronin.auctionstorageservice.dto.BidDTO;
import com.cyberronin.auctionstorageservice.dto.SaveAuctionReqDTO;
import com.cyberronin.auctionstorageservice.model.AuctionStatus;
import com.cyberronin.auctionstorageservice.service.AuctionService;
import com.cyberronin.auctionstorageservice.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final AuctionService auctionService;
    private final BidService bidService;

    @RabbitListener(queues = {"${q.auction-service.auction.expire}"})
    public void createAuction(SaveAuctionReqDTO auction)
    {
        auctionService.save(auction);
    }

    @RabbitListener(queues = {"${q.auction-service.auction.expire}"})
    public void expireAuction(UUID auctionId)
    {
        auctionService.updateStatus(auctionId, AuctionStatus.ENDED);
    }

    @RabbitListener(queues = {"${q.storage-service.auction.status.update}"})
    public void auctionStatusUpdate(AuctionStatusUpdateDTO dto)
    {
        auctionService.updateStatus(dto.id(), dto.status());
    }

    @RabbitListener(queues = {"${q.storage-service.auction.update}"})
    public void auctionUpdateHighestBid(AuctionHighestBidUpdateDTO dto)
    {
        auctionService.updateHighestBid(dto);
    }

    @RabbitListener(queues = {"${q.storage-service.bid.placed}"})
    public void bidPlaced(BidDTO dto)
    {
        bidService.save(dto);
    }
}
