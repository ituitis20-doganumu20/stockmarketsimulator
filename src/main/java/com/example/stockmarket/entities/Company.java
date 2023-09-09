package com.example.stockmarket.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long my_id;

    public int stocks;
    private double currPrice;

    public Company(){}
    public Company(int stocks, double currPrice){
        this.stocks=stocks;
        this.currPrice=currPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currPrice= currentPrice;
    }

    public double getCurrentPrice() {
        return currPrice;
    }

    // Constructors, getters, setters
}
