package com.example.stockmarket.controllers;

import com.example.stockmarket.entities.TradeOffer;
import com.example.stockmarket.services.TradeOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trade-offers")
public class TradeOfferController {

    private final TradeOfferService tradeOfferService;

    @Autowired
    public TradeOfferController(TradeOfferService tradeOfferService) {
        this.tradeOfferService = tradeOfferService;
    }

    @GetMapping
    public List<TradeOffer> getAllTradeOffers() {
        return tradeOfferService.getAllTradeOffers();
    }

    // Add more methods for CRUD operations on trade offers
}
