package com.example.stockmarket.services;

import com.example.stockmarket.entities.TradeOffer;
import com.example.stockmarket.repositories.TradeOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeOfferService {

    private final TradeOfferRepository tradeOfferRepository;

    @Autowired
    public TradeOfferService(TradeOfferRepository tradeOfferRepository) {
        this.tradeOfferRepository = tradeOfferRepository;
    }

    public List<TradeOffer> getAllTradeOffers() {
        return tradeOfferRepository.findAll();
    }

    // Add more methods to interact with the TradeOffer entity and repository
}
