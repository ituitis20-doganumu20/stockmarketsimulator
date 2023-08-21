import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


class Agent {
    int stocks ;
    double balance;

    public Agent(double balance, int stock) {
        this.balance = balance;
        this.stocks=stock;
    }

    public void buy(double price, int amount, Agent seller) {
        stocks += amount;
        seller.stocks-=amount;
        balance -= amount * price;
        seller.balance+=amount*price;
    }



}

class Trader {
    private static final int MAX_QUEUE_SIZE = 10000;
    int different=0;
    int same=0;

    int agentSum=0;
    int compStock;

    Trader(Company company){
        compStock=company.stocks;
    }
    //boolean tradeisdone=false;
    PriorityQueue<TradeOffer> buyingQueue = new PriorityQueue<>(Comparator.reverseOrder());
    PriorityQueue<TradeOffer> sellingQueue = new PriorityQueue<>();

    private void maintainQueueSize(PriorityQueue<TradeOffer> queue) {
        if (queue.size() > MAX_QUEUE_SIZE) {
            int newSize = Math.max(MAX_QUEUE_SIZE, queue.size() / 2); // Cut the queue in half
            List<TradeOffer> offers = new ArrayList<>(queue);
            queue.clear();

            for (int i = 0; i < newSize; i++) {
                queue.offer(offers.get(i));
            }
        }
    }

    public int getagents(){return agentSum;}
    public void trade() {
        List<Agent> agents=new ArrayList<>();
        if(buyingQueue.peek().price < sellingQueue.peek().price){
            double buyingPrice = RandomNumberGenerator.generateRandomNumber(getCurrentPrice(),0.01);
            double balance = Math.max(0, RandomNumberGenerator.generateRandomNumber(buyingPrice*10,2));
            Agent agent = new Agent(balance,0);
            agentSum++;
            System.out.println("Number of Agents: " + agentSum);

            int buyingAmount = (int) (agent.balance / buyingPrice);
            if(buyingAmount>0)
                addBuyingOffer(buyingPrice, buyingAmount, agent);
            return;
        }
        int totalAmount=0;
        TradeOffer buyer=buyingQueue.poll();

        while (buyer.price >= getCurrentPrice()){
            TradeOffer seller = sellingQueue.poll();
            if (buyer.amount <= seller.amount) {
                buyer.agent.buy(seller.price, buyer.amount,seller.agent);
                if(buyer.agent==seller.agent) same++;
                else different++;
                System.out.println("number of trades between same"+same+" and between different "+different);
                addSellingOffer(seller.price, seller.amount-buyer.amount,seller.agent );
                agents.add(seller.agent);
                totalAmount+= buyer.amount;
                break;

            } else {//buyer has more stocks to buy
                buyer.agent.buy(seller.price, seller.amount,seller.agent);
                totalAmount+= seller.amount;
                buyer.amount-= seller.amount;
                System.out.println(buyer.amount);
                if(buyer.agent==seller.agent) same++;
                else different++;
                System.out.println("number of trades between same"+same+" and between different "+different);
                agents.add(seller.agent);
            }
        }
        addSellingOffer(RandomNumberGenerator.generateRandomNumber(getCurrentPrice(),0.01),totalAmount,buyer.agent);
        for (Agent agent:agents) {
            if(agent.balance>0){
                double buyingPrice = RandomNumberGenerator.generateRandomNumber(getCurrentPrice(),0.01);
                int buyingAmount = (int) (agent.balance / buyingPrice);
                if(buyingAmount>0)
                    addBuyingOffer(buyingPrice, buyingAmount, agent);
            }
        }
        System.out.println("Current Price: " + getCurrentPrice());
    }

    public void addBuyingOffer(double price, int amount, Agent agent) {
        amount = Math.min(amount, compStock);
        buyingQueue.add(new TradeOffer(price, amount, agent));
    }

    public void addSellingOffer(double price,int amount ,Agent agent) {
        if(amount>0)
            sellingQueue.add(new TradeOffer(price, amount, agent));
        //maintainQueueSize(sellingQueue);
    }

    public double getCurrentPrice() {
        return sellingQueue.peek().price;
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
        Company comp = new Company(200,1);
        Trader trader = new Trader(comp);
        //Agent agent1= new Agent(0,2);
        //List<Agent> agentList= new ArrayList<>();
        //int agentSum=0;
        //Agent agent2= new Agent(15,0);//b:14.7
        Agent agent3= new Agent(37,200);
        trader.addBuyingOffer(1.1,34,agent3);
        trader.addSellingOffer(1.01,200,agent3);
        /*trader.addSellingOffer(1,20,agent3);
        trader.addSellingOffer(1.01,10,agent3);
        trader.addSellingOffer(1.02,5,agent3);
        trader.addSellingOffer(1.03,165,agent3);*/
        //trader.addSellingOffer(1.02,7,agent2);
        //trader.addBuyingOffer(1.03,14,agent2);
        //agentList.add(agent2);
        //agentList.add(agent3);
        while (true){
            /*try {
                Thread.sleep(1000); // Wait for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            int stocksum=0;
            for (TradeOffer t:trader.sellingQueue){
                stocksum+=t.amount;
            }
            System.out.println("number of stocks: "+stocksum);
            trader.trade();

            if(trader.getagents()==3600)
                break;

        }


    }}

