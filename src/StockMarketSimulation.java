import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


class Agent {
    int stocks = 0;
    double balance;

    public Agent(double balance) {
        this.balance = balance;
    }

    public void buy(double price, int amount) {
        int maxStocksToBuy = (int) (balance / price);
        stocks += maxStocksToBuy;
        balance -= maxStocksToBuy * price;
    }

    public void sell(double price) {
        balance += stocks * price;
        stocks = 0;
    }
}

class Trader {
    PriorityQueue<TradeOffer> buyingQueue = new PriorityQueue<>(Comparator.reverseOrder());
    PriorityQueue<TradeOffer> sellingQueue = new PriorityQueue<>();

    public void trade() {
        while (!buyingQueue.isEmpty() && !sellingQueue.isEmpty() && buyingQueue.peek().price >= sellingQueue.peek().price) {
            TradeOffer buyer = buyingQueue.poll();
            TradeOffer seller = sellingQueue.poll();

            if (buyer.amount > seller.amount) {
                buyer.agent.buy(seller.price, seller.amount);
                seller.agent.sell(seller.price);
                buyer.amount -= seller.amount;
                buyingQueue.add(buyer);
            } else {
                buyer.agent.buy(seller.price, buyer.amount);
                seller.agent.sell(seller.price);
                seller.amount -= buyer.amount;
                if (seller.amount > 0) {
                    sellingQueue.add(seller);
                }

            }
        }
        //System.out.println("Current Price: " + getCurrentPrice());
    }

    public void addBuyingOffer(double price, int amount, Agent agent) {
        buyingQueue.add(new TradeOffer(price, amount, agent));
        trade();
    }

    public void addSellingOffer(double price, int amount, Agent agent) {
        sellingQueue.add(new TradeOffer(price, amount, agent));
        trade();
    }

    public double getCurrentPrice() {
        return sellingQueue.isEmpty() ? 1 : sellingQueue.peek().price;
    }
}

class TradeOffer implements Comparable<TradeOffer> {
    double price;
    int amount;
    Agent agent;

    public TradeOffer(double price, int amount, Agent agent) {
        this.price = price;
        this.amount = amount;
        this.agent = agent;
    }

    @Override
    public int compareTo(TradeOffer other) {
        return Double.compare(this.price, other.price);
    }
}

public class StockMarketSimulation {
    public static void main(String[] args) {
        Trader trader = new Trader();
        Company company = new Company();
        Agent companyAgent = new Agent(0); // Create an agent for the company

        company.sell(1.0,1000, trader, companyAgent); // Pass the company agent

        RandomNumberGenerator r= new RandomNumberGenerator();
        double prevPrice = -1;
        while(true){
            double balance = Math.max(0, Math.min(20, new Random().nextGaussian() * 5 + 10));
            //double balance = r.generateRandomNumber(10);
            Agent agent = new Agent(balance);


            double buyingPrice = r.generateRandomNumber(trader.getCurrentPrice());
            int buyingAmount = (int) (balance / buyingPrice);
            trader.addBuyingOffer(buyingPrice, buyingAmount, agent);

            if (buyingAmount > 0) {
                double sellingPrice = r.generateRandomNumber(trader.getCurrentPrice());
                trader.addSellingOffer(sellingPrice, buyingAmount, agent);
            }

            double currentPrice = trader.getCurrentPrice();
            if (currentPrice != prevPrice) {
                System.out.println("Current Price: " + currentPrice);
                prevPrice = currentPrice;
            }
            /*try {
                Thread.sleep(1000); // Wait for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

        }

        /*double prevPrice = -1;
        while (true) {
            trader.trade();
            double currentPrice = trader.getCurrentPrice();
            if (currentPrice != prevPrice) {
                System.out.println("Current Price: " + currentPrice);
                prevPrice = currentPrice;
            }
            try {
                Thread.sleep(500); // Wait for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
}
