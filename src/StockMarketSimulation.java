import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


class Agent {
    int stocks ;
    double balance;
    int bought=0;

    int sold=0;

    public Agent(double balance, int stock) {
        this.balance = balance;
        this.stocks=stock;
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

class Trader {
    int compStock;
    double lastprice=0;
    Trader(Company company){
        compStock=company.stocks;
    }
    PriorityQueue<TradeOffer> buyingQueue = new PriorityQueue<>(Comparator.reverseOrder());
    PriorityQueue<TradeOffer> sellingQueue = new PriorityQueue<>();
    public void trade() {

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
                lastprice=seller.price;
            }
        }

        System.out.println("Current Price: " + getCurrentPrice());
    }

    public void update(List<Agent>agents){
        for (Agent agent:agents) {
            if(agent.bought>0)
            {
                addSellingOffer(RandomNumberGenerator.generateRandomNumber(getCurrentPrice(),0.03), agent.bought, agent);
                agent.bought=0;
            }
            if(agent.sold>0){
                double buyingPrice = RandomNumberGenerator.generateRandomNumber(getCurrentPrice(),0.03);
                int buyingAmount = (int) (agent.balance / buyingPrice);
                if(buyingAmount>0)
                    addBuyingOffer(buyingPrice, buyingAmount, agent);
                agent.sold=0;
            }
        }
    }
    Random r =new Random();
    public void regulate(){
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
        amount = Math.min(amount, compStock);
        buyingQueue.add(new TradeOffer(price, amount, agent));
    }

    public void addSellingOffer(double price,int amount ,Agent agent) {
        if(amount>0)
            sellingQueue.add(new TradeOffer(price, amount, agent));
    }

    public double getCurrentPrice() {
        return sellingQueue.isEmpty()? lastprice:sellingQueue.peek().price;
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
        Company comp = new Company(2000,1);
        Trader trader = new Trader(comp);
        Agent agent3= new Agent(1000,2000);
        trader.addBuyingOffer(1.1,4,agent3);
        trader.addSellingOffer(1,2000,agent3);
        List<Agent> agents=new ArrayList<>();
        agents.add(agent3);
        int iteration=0;
        while (true){
            iteration++;
            try {
                Thread.sleep(1000); // Wait for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            trader.regulate();
            trader.trade();
            trader.update(agents);
            if(iteration==3600000) break;
        }
    }
}

