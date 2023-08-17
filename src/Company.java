public class Company {
    int stocks = 1000;
    int currPrice = 1;

    public void sell(double price,int amount, Trader trader, Agent seller) {

        trader.addSellingOffer(price, amount, seller);
    }
}
