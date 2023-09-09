package com.example.stockmarket.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "agent")
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long my_id;

    private int stocks;
    private double balance;
    private int bought;
    private int sold;

    public Agent() {
        // Default constructor
    }

    public Agent(double balance, int stocks) {
        this.balance = balance;
        this.stocks = stocks;
        this.bought = 0;
        this.sold = 0;
    }

    // Getters and Setters

    public Long getMy_id() {
        return my_id;
    }

    public void setMy_id(Long my_id) {
        this.my_id = my_id;
    }

    public int getStocks() {
        return stocks;
    }

    public void setStocks(int stocks) {
        this.stocks = stocks;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getBought() {
        return bought;
    }

    public void setBought(int bought) {
        this.bought = bought;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public void buy(double price, int amount, Agent seller) {
        stocks += amount;
        seller.stocks-=amount;
        balance -= amount * price;
        seller.balance+=amount*price;
        bought+=amount;
        seller.sold+=amount;
    }
}
