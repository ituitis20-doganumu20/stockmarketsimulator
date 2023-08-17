import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


class Agent {
    int stocks ;
    double balance;

    public Agent(double balance, int stock) {
        this.balance = balance;
        this.stocks=stock;
    }

    public void buy(double price, int amount) {
        stocks += amount;
        balance -= amount * price;
    }

    public void sell(double price, int amount) {
        balance += amount * price;
        stocks = stocks-amount;
    }
}

class Trader {
    //boolean tradeisdone=false;
    PriorityQueue<TradeOffer> buyingQueue = new PriorityQueue<>(Comparator.reverseOrder());
    PriorityQueue<TradeOffer> sellingQueue = new PriorityQueue<>();

    public void trade() {

        while (!buyingQueue.isEmpty() && !sellingQueue.isEmpty() && buyingQueue.peek().price >= sellingQueue.peek().price) {

            TradeOffer buyer = buyingQueue.poll();
            TradeOffer seller = sellingQueue.poll();

            if (buyer.amount > seller.amount) {
                buyer.agent.buy(seller.price, seller.amount);
                seller.agent.sell(seller.price, seller.amount);

                buyer.amount-=seller.amount;
                addBuyingOffer(buyer.price,buyer.amount,buyer.agent);

                double sellingPrice = RandomNumberGenerator.generateRandomNumber(getCurrentPrice(),0.1);
                addSellingOffer(sellingPrice, buyer.agent.stocks, buyer.agent);
                System.out.println("Current Price: " + getCurrentPrice());
            } else {
                buyer.agent.buy(seller.price, buyer.amount);
                seller.agent.sell(seller.price,buyer.amount);
                System.out.println("Current Price: " + getCurrentPrice());
                if (seller.agent.stocks > 0) {
                    addSellingOffer(seller.price, seller.agent.stocks,seller.agent);
                    //System.out.println("Current Price: " + getCurrentPrice());
                    //System.out.println(seller.agent.stocks);
                }
                double sellingPrice = RandomNumberGenerator.generateRandomNumber(getCurrentPrice(),0.1);
                addSellingOffer(sellingPrice, buyer.agent.stocks, buyer.agent);
                System.out.println("Current Price: " + getCurrentPrice());
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
        Agent companyAgent = new Agent(0,1000); // Create an agent for the company

        company.sell(1.0,1000, trader, companyAgent); // Pass the company agent

        //RandomNumberGenerator r= new RandomNumberGenerator();
        double prevPrice = -1;
        int a=1;
        while(true){

            a++;
            if(a == 1000){
                System.out.println("wtf");
                //return;
            }
            double balance = Math.max(0, RandomNumberGenerator.generateRandomNumber(10000,1));
            //double balance = r.generateRandomNumber(10);
            Agent agent = new Agent(balance,0);


            double buyingPrice = RandomNumberGenerator.generateRandomNumber(trader.getCurrentPrice(),0.1);
            int buyingAmount = (int) (balance / buyingPrice);
            trader.addBuyingOffer(buyingPrice, buyingAmount, agent);

            /*if (trader.tradeisdone) {
                double sellingPrice = r.generateRandomNumber(trader.getCurrentPrice(),0.1);
                trader.addSellingOffer(sellingPrice, buyingAmount, agent);
            }*/

            double currentPrice = trader.getCurrentPrice();

            /*try {
                Thread.sleep(1000); // Wait for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            int stocksum=0;
            for (TradeOffer t:trader.sellingQueue){
                stocksum+=t.amount;
            }
            System.out.println(stocksum);
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
