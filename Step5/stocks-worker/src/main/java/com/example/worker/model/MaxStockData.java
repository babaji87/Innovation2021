package com.example.worker.model;

import javax.persistence.*;

@Entity
@Table(name = "maxstocksdata")
public class MaxStockData implements Comparable<MaxStockData>{
    public MaxStockData(long id, String symbol, Double maxPrice, String latestTime) {
        this.id = id;
        this.symbol = symbol;
        this.maxPrice = maxPrice;
        this.latestTime = latestTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }


    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double latestPrice) {
        this.maxPrice = latestPrice;
    }

    public String getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(String latestTime) {
        this.latestTime = latestTime;
    }

    public MaxStockData(String symbol, Double latestPrice, String latestTime) {
        this.symbol = symbol;
        this.maxPrice = latestPrice;
        this.latestTime = latestTime;
    }

    public MaxStockData() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "symbol", nullable = false)
    private String symbol;
    @Column(name = "maxPrice", nullable = false)
    private Double maxPrice;
    @Column(name = "latestTime", nullable = true)
    private String latestTime;


    @Override
    public int compareTo(MaxStockData o) {
        if (this.getMaxPrice() > o.getMaxPrice()) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "MaxStockData{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", maxPrice=" + maxPrice +
                ", latestTime='" + latestTime + '\'' +
                '}';
    }
}