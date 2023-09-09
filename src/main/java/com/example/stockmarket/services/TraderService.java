package com.example.stockmarket.services;

import com.example.stockmarket.entities.Agent;
import com.example.stockmarket.entities.Company;
import com.example.stockmarket.entities.RandomNumberGenerator;
import com.example.stockmarket.entities.TradeOffer;
import com.example.stockmarket.repositories.AgentRepository;
import com.example.stockmarket.repositories.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.PriorityQueue;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class TraderService {
    int compStock;

    public void setCompStock(int compStock){this.compStock=compStock;}
    double lastPrice =0;
    private final List<Agent> agents = new ArrayList<>();

    @Autowired
    private AgentRepository agentRepository;

    public void addAgent(Agent agent,double balance,
                                      double buyingPriceOffer, double initialPrice,int stocks) {
        // Create a new agent
        agents.add(agent);
        agentRepository.save(agent);

        addBuyingOffer(buyingPriceOffer, (int) (balance / buyingPriceOffer), agent);
        if(stocks!=0)
            addSellingOffer(initialPrice, stocks, agent);
    }

    /*@Autowired
    public TraderService(){
        Company company = new Company(2000, 1);
        compStock= company.stocks;
    }*/

    //private final RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator();
    private final PriorityQueue<TradeOffer> buyingQueue = new PriorityQueue<>(Comparator.reverseOrder());
    private final PriorityQueue<TradeOffer> sellingQueue = new PriorityQueue<>();

    public void trade() {
        // Trading logic here, using the agents, buyingQueue, sellingQueue, and company
        TradeOffer buyer=buyingQueue.poll();
        while (buyer.price >= sellingQueue.peek().price){
            TradeOffer seller=sellingQueue.poll();
            if (buyer.amount <= seller.amount) {
                buyer.agent.buy(seller.price, buyer.amount,seller.agent);
                addSellingOffer(seller.price, seller.amount-buyer.amount,seller.agent );
                break;

            } else {//buyer has more stocks to buy
                buyer.agent.buy(seller.price, seller.amount,seller.agent);
                buyer.amount-= seller.amount;
                lastPrice =seller.price;
            }
        }
        updateCompanyPriceInDatabase(getCurrentPrice());
        System.out.println("Current Price: " + getCurrentPrice());
    }

    public void updateAgents() {
        // Update agents' offers and actions
        for (Agent agent:agents) {
            if(agent.getBought() >0)
            {
                addSellingOffer(RandomNumberGenerator.generateRandomNumber(getCurrentPrice(),0.03), agent.getBought(), agent);
                agent.setBought(0);
            }
            if(agent.getSold() >0){
                double buyingPrice = RandomNumberGenerator.generateRandomNumber(getCurrentPrice(),0.03);
                int buyingAmount = (int) (agent.getBalance() / buyingPrice);
                if(buyingAmount>0)
                    addBuyingOffer(buyingPrice, buyingAmount, agent);
                agent.setSold(0);
            }
        }
    }
    Random r =new Random();
    public void regulate() {
        // Regulatory logic
        if(buyingQueue.peek().price<sellingQueue.peek().price){
            if(r.nextBoolean()){
                double diff=sellingQueue.peek().price-buyingQueue.peek().price;
                for (TradeOffer t:buyingQueue) {
                    t.price+=diff;
                }
            }
            else{
                double diff=sellingQueue.peek().price-buyingQueue.peek().price;
                for (TradeOffer t:sellingQueue) {
                    t.price-=diff;
                }
            }
        }
    }

    public void addBuyingOffer(double price, int amount, Agent agent) {
        // Adding a buying offer to the queue
        amount = Math.min(amount, compStock);
        buyingQueue.add(new TradeOffer(price, amount, agent));
    }

    public void addSellingOffer(double price, int amount, Agent agent) {
        // Adding a selling offer to the queue
        if(amount>0)
            sellingQueue.add(new TradeOffer(price, amount, agent));
    }

    public double getCurrentPrice() {
        // Get the current price
        return sellingQueue.isEmpty()? lastPrice:sellingQueue.peek().price;
    }

    // Other methods from the existing Trader class
    @Autowired
    private CompanyRepository companyRepository; // Inject your repository
    public void updateCompanyPriceInDatabase(double currentPrice) {
        // Update the current price in the database
        Company company = companyRepository.findById(1L).orElse(new Company(2000,1)); // Assuming company ID is 1
        company.setCurrentPrice(currentPrice);
        companyRepository.save(company);
    }

}