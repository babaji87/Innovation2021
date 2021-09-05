package com.example.worker.model;

import java.io.Serializable;

public class StockMessage implements Serializable {

    private static final long serialVersionUID = -295422703255886286L;
    private String name;

    public StockMessage(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}