package com.example.botcoin.za.co.botcoin.objs;

public class TradePrice
{
    private Double price;
    private Integer counter;
    private Boolean isIncreased;

    public TradePrice(Double price, boolean isIncreased)
    {
        this.price = price;
        this.counter = 0;
        this.isIncreased = isIncreased;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Boolean getIncreased() {
        return isIncreased;
    }

    public void setIncreased(Boolean increased) {
        isIncreased = increased;
    }
}
