package com.example.stockmarket.services;

import com.example.stockmarket.services.StockMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimulationController {

    @Autowired
    private StockMarketService stockMarketService;

    @PostMapping("/simulate")
    public void startSimulation() {
        stockMarketService.simulateStockMarket();
    }
}
