package com.example.worker.model;

import javax.persistence.*;

@Entity
@Table(name = "stocksdata")
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "symbol", nullable = false)
    private String symbol;
    @Column(name = "companyName", nullable = false)
    private String companyName;
    @Column(name = "primaryExchange", nullable = false)
    private String primaryExchange;
    @Column(name = "latestPrice", nullable = false)
    private Double latestPrice;
    @Column(name = "latestTime", nullable = true)
    private String latestTime;


    @Override
    public int compareTo(StocksData o) {
        if (this.getLatestPrice() > o.getLatestPrice()) {
            return 1;
        }
        return 0;
    }
}