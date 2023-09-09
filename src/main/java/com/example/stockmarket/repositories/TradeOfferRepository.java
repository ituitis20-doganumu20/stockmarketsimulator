package com.example.stockmarket.repositories;

import com.example.stockmarket.entities.TradeOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeOfferRepository extends JpaRepository<TradeOffer, Long> {
}
