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

    public void trade() {
        //List<TradeOffer> offers = new ArrayList<>();
        if(!buyingQueue.isEmpty() && !sellingQueue.isEmpty()){

            /*if(sellingQueue.peek().agent==buyingQueue.peek().agent){
                while(sellingQueue.peek().agent==buyingQueue.peek().agent) {
                    //offers.add(sellingQueue.poll());
                    sellingQueue.poll();
                }
            }*/

            if (buyingQueue.peek().price >= sellingQueue.peek().price) {
                TradeOffer buyer = buyingQueue.poll();
                TradeOffer seller = sellingQueue.poll();
                if (buyer.amount <= seller.amount) {
                    buyer.agent.buy(seller.price, buyer.amount,seller.agent);
                    if(buyer.agent==seller.agent) same++;
                    else different++;
                    System.out.println("number of trades between same"+same+" and between different "+different);
                    double sellingPrice = RandomNumberGenerator.generateRandomNumber(seller.price,0.01);
                    addSellingOffer(sellingPrice,buyer.amount, buyer.agent);
                    double buyingPrice = RandomNumberGenerator.generateRandomNumber(seller.price,0.01);
                    addBuyingOffer(buyingPrice, seller.agent);
                    if(seller.agent.stocks!=0){
                        addSellingOffer(RandomNumberGenerator.generateRandomNumber(seller.price,0.01), seller.agent.stocks,seller.agent);
                    }

                } else {//buyer has more stocks to buy
                    buyer.agent.buy(seller.price, seller.amount,seller.agent);
                    if(buyer.agent==seller.agent) same++;
                    else different++;
                    System.out.println("number of trades between same"+same+" and between different "+different);
                    double sellingPrice = RandomNumberGenerator.generateRandomNumber(seller.price,0.1);
                    addSellingOffer(sellingPrice, seller.amount, buyer.agent);
                    addBuyingOffer(RandomNumberGenerator.generateRandomNumber(seller.price,0.1), buyer.agent);
                    double buyingPrice = RandomNumberGenerator.generateRandomNumber(seller.price,0.1);
                    addBuyingOffer(buyingPrice,seller.agent);
                }
                System.out.println("Current Price: " + getCurrentPrice());


            }
            /*if (!offers.isEmpty()){
                sellingQueue.addAll(offers);
            }*/

        }
        else{
            double balance = Math.max(0, RandomNumberGenerator.generateRandomNumber(10,2));
            Agent agent = new Agent(balance,0);
            double buyingPrice = RandomNumberGenerator.generateRandomNumber(getCurrentPrice(),0.1);
            addBuyingOffer(buyingPrice, agent);
            agentSum++;
            System.out.println("Number of Agents: " + agentSum);
        }

        //System.out.println("Current Price: " + getCurrentPrice());
    }

    public void addBuyingOffer(double price, Agent agent) {
        int buyingAmount = (int) (agent.balance / price);
        if (buyingAmount!=0){
            buyingQueue.add(new TradeOffer(price, buyingAmount, agent));
            maintainQueueSize(buyingQueue);
        }
    }

    public void addSellingOffer(double price,int amount ,Agent agent) {
        sellingQueue.add(new TradeOffer(price, amount, agent));
        maintainQueueSize(sellingQueue);
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
        Trader trader = new Trader();
        //Agent agent1= new Agent(0,2);
        Agent agent2= new Agent(15,7);
        Agent agent3= new Agent(15,2);
        trader.addBuyingOffer(1.01,agent3);
        trader.addSellingOffer(1,2,agent3);
        trader.addSellingOffer(1.02,7,agent2);
        trader.addBuyingOffer(1.03,agent2);
        while (true){
            trader.trade();
            try {
                Thread.sleep(10); // Wait for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int stocksum=0;
            for (TradeOffer t:trader.sellingQueue){
                stocksum+=t.amount;
            }
            System.out.println("number of stocks: "+stocksum);
        }


        /*Company company = new Company();
        Agent companyAgent = new Agent(0,1000); // Create an agent for the company

        company.sell(1.0,10, trader, companyAgent); // Pass the company agent

         */

        //RandomNumberGenerator r= new RandomNumberGenerator();
        //double prevPrice = -1;
        //int a=1;
        /*while(true){
            trader.trade();
            a++;
            if(a == 1000){
                System.out.println("wtf");
                //return;
            }
            /*double balance = Math.max(0, RandomNumberGenerator.generateRandomNumber(10,2));
            //double balance = r.generateRandomNumber(10);
            Agent agent = new Agent(balance,0);


            double buyingPrice = RandomNumberGenerator.generateRandomNumber(trader.getCurrentPrice(),0.1);
            int buyingAmount = (int) (balance / buyingPrice);
            trader.addBuyingOffer(buyingPrice, buyingAmount, agent);*/

            /*if (trader.tradeisdone) {
                double sellingPrice = r.generateRandomNumber(trader.getCurrentPrice(),0.1);
                trader.addSellingOffer(sellingPrice, buyingAmount, agent);
            }*/

            //double currentPrice = trader.getCurrentPrice();

            /*try {
                Thread.sleep(1000); // Wait for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int stocksum=0;
            for (TradeOffer t:trader.sellingQueue){
                stocksum+=t.amount;
            }
            System.out.println(stocksum);*/
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

