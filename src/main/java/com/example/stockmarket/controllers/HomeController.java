package com.example.stockmarket.controllers;

import com.example.stockmarket.entities.Agent;
import com.example.stockmarket.entities.Company;
import com.example.stockmarket.repositories.*;
import com.example.stockmarket.services.StockMarketService;
import com.example.stockmarket.services.TraderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private TraderService traderService;
    @Autowired
    private AgentRepository AgentRepository;

    @Autowired
    private StockMarketService StockMarketService;
    @GetMapping("/home")
    public String home(Model model) {

        // Fetch the current price from the database
        Company company = companyRepository.findById(1L).orElse(new Company(2000,1)); // Assuming company ID is 1
        double currentPrice = company.getCurrentPrice();

        // Pass the current price to the Thymeleaf template
        model.addAttribute("currentPrice", currentPrice);

        return "home"; // Return the name of your Thymeleaf template (e.g., home.html)
    }


    @PostMapping("/initializeData")
    public String initializeData(@RequestBody InitializeDataRequest requestData) {

        int stocks=requestData.stocks;
        double initialPrice= requestData.initialPrice;
        double balance=requestData.balance;
        double buyingPriceOffer=requestData.buyingPriceOffer;

        System.out.println("i started");

        // Initialize data and start the simulation
        // Create a new company, agent, and add buying and selling offers
        // You will need to implement this logic in your service

        // Create a new company
        Company company = new Company(stocks, initialPrice);
        companyRepository.save(company);

        traderService.setCompStock(stocks);

        // Create a new agent
        Agent agent = new Agent(balance, stocks);
        traderService.addAgent(agent,balance,buyingPriceOffer,initialPrice,stocks);


        //StockMarketService.simulateStockMarket();
        return "redirect:/"; // Redirect to the home page
    }


    @PostMapping("/addAgent")
    public String addAgent(@RequestBody AddAgentRequest addAgentRequest) {
        double newAgentBalance=addAgentRequest.newAgentBalance;
        double newAgentBuyingPriceOffer= addAgentRequest.newAgentBuyingPriceOffer;
        // Add a new agent with a buying offer to the simulation
        // You will need to implement this logic in your service
        // Create a new agent
        Agent agent = new Agent(newAgentBalance, 0);
        traderService.addAgent(agent,newAgentBalance,newAgentBuyingPriceOffer,0,0);
        return "redirect:/"; // Redirect to the home page
    }
}

