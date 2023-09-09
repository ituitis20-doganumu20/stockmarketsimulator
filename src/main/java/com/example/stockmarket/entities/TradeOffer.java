package com.example.stockmarket.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tradeoffer")
public class TradeOffer implements Comparable<TradeOffer>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long my_id;

    public double price;
    public int amount;

    @ManyToOne
    public Agent agent;

    public TradeOffer(double price, int amount, Agent agent) {
        this.price = price;
        this.amount = amount;
        this.agent = agent;
    }


    @Override
    public int compareTo(TradeOffer other) {
        return Double.compare(this.price, other.price);
    }
    // getters, setters
}
