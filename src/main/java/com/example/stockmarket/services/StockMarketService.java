package com.example.stockmarket.services;

import com.example.stockmarket.entities.Agent;
import com.example.stockmarket.entities.Company;
import com.example.stockmarket.repositories.AgentRepository;
import com.example.stockmarket.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockMarketService {
    private final CompanyRepository companyRepository;
    private final AgentRepository agentRepository;
    private final TraderService traderService;

    @Autowired
    public StockMarketService(
            CompanyRepository companyRepository,
            AgentRepository agentRepository,
            TraderService traderService) {
        this.companyRepository = companyRepository;
        this.agentRepository = agentRepository;
        this.traderService = traderService;
    }

    @Async
    //@Scheduled(fixedDelay = 1000) // Run every 1 second
    public void simulateStockMarket() {
        //Company comp = companyRepository.findById(1L).orElse(new Company(2000, 1));
        //Company comp = new Company(2000,1);
        //Agent agent3 = new Agent(1000, 2000);

        // Save initial data to repositories
        /*companyRepository.save(comp);
        agentRepository.save(agent3);
        traderService.addBuyingOffer(1.1,4,agent3);
        traderService.addSellingOffer(1,2000,agent3);*/
        int iteration = 0;
        while (true) {
            iteration++;
            traderService.regulate();
            traderService.trade();
            traderService.updateAgents();
            try {
                Thread.sleep(1000); // Wait for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (iteration == 3600000) {
                break;
            }
        }
    }

    // Define additional methods as needed for your application logic
}