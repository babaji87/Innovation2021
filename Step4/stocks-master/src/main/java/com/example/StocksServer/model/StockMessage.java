package com.example.StocksServer.model;

import java.io.Serializable;

public class StockMessage implements Serializable {

    private static final long serialVersionUID = -295422703255886286L;
    private String stockName;

    public StockMessage(String name) {
        setName(name);
    }

    public String getName() {
        return stockName;
    }

    public void setName(String name) {
        this.stockName = name;
    }

}
