package com.example.worker.model;

public class StocksData implements Comparable<StocksData>{
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }


    @Override
    public String toString() {
        return "StocksData{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", companyName='" + companyName + '\'' +
                ", primaryExchange='" + primaryExchange + '\'' +
                ", latestPrice=" + latestPrice +
                ", latestTime='" + latestTime + '\'' +
                '}';
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPrimaryExchange() {
        return primaryExchange;
    }

    public void setPrimaryExchange(String primaryExchange) {
        this.primaryExchange = primaryExchange;
    }

    public Double getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(Double latestPrice) {
        this.latestPrice = latestPrice;
    }

    public String getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(String latestTime) {
        this.latestTime = latestTime;
    }

    public StocksData(String symbol, String companyName, String primaryExchange, Double latestPrice, String latestTime) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.primaryExchange = primaryExchange;
        this.latestPrice = latestPrice;
        this.latestTime = latestTime;
    }

    public StocksData() {
    }

    private long id;

    private String symbol;

    private String companyName;

    private String primaryExchange;

    private Double latestPrice;

    private String latestTime;


    @Override
    public int compareTo(StocksData o) {
        if (this.getLatestPrice() > o.getLatestPrice()) {
            return 1;
        }
        return 0;
    }
}