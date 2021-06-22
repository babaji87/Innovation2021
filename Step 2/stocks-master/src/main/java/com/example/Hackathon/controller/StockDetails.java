package com.example.Hackathon.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class StockDetails {
    private String symbol;
    private String sector;
    private String securityType;
    private int bidPrice;
    private int bidSize;
    private int askPrice;
    private int askSize;
    private Timestamp lastUpdated;
    private float lastSalePrice;
    private int lastSaleSize;
    private Timestamp lastSaleTime;
    private int volume;
    public StockDetails()
    {
        super();
    }
 public StockDetails(String symbol, String sector, String securityType, int bidPrice, int bidSize, int askPrice, int askSize, Timestamp lastUpdated, float lastSalePrice, int lastSaleSize, Timestamp lastSaleTime, int volume) {
  this.symbol = symbol;
  this.sector = sector;
  this.securityType = securityType;
  this.bidPrice = bidPrice;
  this.bidSize = bidSize;
  this.askPrice = askPrice;
  this.askSize = askSize;
  this.lastUpdated = lastUpdated;
  this.lastSalePrice = lastSalePrice;
  this.lastSaleSize = lastSaleSize;
  this.lastSaleTime = lastSaleTime;
  this.volume = volume;
 }


 public String getSymbol() { return symbol; }

 public String getSector() {
  return sector;
 }

 public String getSecurityType() {
  return securityType;
 }

 public int getBidPrice() {
  return bidPrice;
 }

 public int getBidSize() {
  return bidSize;
 }

 public int getAskPrice() {
  return askPrice;
 }

 public int getAskSize() {
  return askSize;
 }

 public Timestamp getLastUpdated() {
  return lastUpdated;
 }

 public float getLastSalePrice() {
  return lastSalePrice;
 }

 public int getLastSaleSize() {
  return lastSaleSize;
 }

 public Timestamp getLastSaleTime() {
  return lastSaleTime;
 }

 public int getVolume() {
  return volume;
 }
}
