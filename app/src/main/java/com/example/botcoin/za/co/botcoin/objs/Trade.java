package com.example.botcoin.za.co.botcoin.objs;

public class Trade
{
    public static final String BUY_TYPE = "BUY";
    public static final String SELL_TYPE = "SELL";

    private String type;
    private String amount;
    private String price;


    public Trade(String type, String amount, String price)
    {
        this.type = type;
        this.amount = amount;
        this.price = price;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
