package com.cyberronin.auctionservice.util;

import com.cyberronin.auctionservice.model.Auction;
import com.cyberronin.auctionservice.model.AuctionStatus;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@UtilityClass
public class AuctionMapper {

    public Map<String, String> toMap(Auction auction) {
        if (auction == null) {
            return new HashMap<>();
        }

        Map<String, String> map = new HashMap<>();

        map.put("id", String.valueOf(auction.getId()));

        map.put("createdAt", String.valueOf(auction.getCreatedAt()));

        map.put("expiresAt", String.valueOf(auction.getExpiresAt()));

        map.put("status", auction.getStatus() != null ? auction.getStatus().name() : "");

        map.put("sellerId", auction.getSellerId() != null ? auction.getSellerId().toString() : "");

        map.put("highestBidUserId", auction.getHighestBidUserId() != null ? auction.getHighestBidUserId().toString() : "");

        map.put("sellerName", auction.getSellerName() != null ? auction.getSellerName() : "");

        map.put("sellerLocation", auction.getSellerLocation() != null ? auction.getSellerLocation() : "");

        map.put("itemName", auction.getItemName() != null ? auction.getItemName() : "");

        map.put("itemDescription", auction.getItemDescription() != null ? auction.getItemDescription() : "");

        map.put("itemImageUrl", auction.getItemImageUrl() != null ? auction.getItemImageUrl() : "");

        map.put("startingAmount", String.valueOf(auction.getStartingAmount()));

        map.put("highestBidAmount", String.valueOf(auction.getHighestBidAmount()));

        map.put("highestBidTimestamp", String.valueOf(auction.getHighestBidTimestamp()));

        return map;
    }

    public static Auction toAuction(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        String idStr = map.get("id");
        UUID auctionId = (idStr != null) ? UUID.fromString(idStr) : null;
        return toAuction(auctionId, map);
    }

    public Auction toAuction(UUID id, Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        return Auction.builder()
                .id(id)
                .createdAt(parseLongSafely(map.get("createdAt")))
                .expiresAt(parseLongSafely(map.get("expiresAt")))
                .status(parseEnumSafely(map.get("status")))
                .sellerId(parseUuidSafely(map.get("sellerId")))
                .sellerName(map.get("sellerName")) // Defaults to null if not present
                .sellerLocation(map.get("sellerLocation"))
                .itemName(map.get("itemName"))
                .itemDescription(map.get("itemDescription"))
                .itemImageUrl(map.get("itemImageUrl"))
                .startingAmount(parseLongSafely(map.get("startingAmount")))
                .highestBidAmount(parseLongSafely(map.get("highestBidAmount")))
                .highestBidUserId(parseUuidSafely(map.get("highestBidUserId")))
                .highestBidTimestamp(parseLongSafely(map.get("highestBidTimestamp")))
                .build();
    }

    // Parsing Helpers
    private static long parseLongSafely(String value) {
        if (value == null || value.isBlank()) {
            return 0L; // Default primitive long value
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    private static UUID parseUuidSafely(String value) {
        if (value == null || value.isBlank()) {
            return null; // Default object value
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static AuctionStatus parseEnumSafely(String value) {
        if (value == null || value.isBlank()) {
            return AuctionStatus.UNKNOWN; // Or set a default like AuctionStatus.PENDING if required
        }
        try {
            return AuctionStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return AuctionStatus.UNKNOWN;
        }
    }
}