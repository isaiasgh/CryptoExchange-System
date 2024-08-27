package com.globant.model.System;

import com.globant.model.Orders.OrderBook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExchangeSystem implements Serializable {
    private ExchangeSystem exchangeSystemInstance;

    private List <User> user;
    private List <Cryptocurrency> cryptocurrencies;
    private OrderBook orderBook;
    private int idUserCounter;

    private ExchangeSystem () {
        this.user = new ArrayList<>();
        this.cryptocurrencies = new ArrayList<>();
        this.orderBook = new OrderBook();
        this.idUserCounter = 0;
    }

    public int getNextUserId () {
        idUserCounter++;
        return idUserCounter;
    }

    public ExchangeSystem getInstance () {
        if (exchangeSystemInstance == null) {
            exchangeSystemInstance = new ExchangeSystem ();
        }
        return exchangeSystemInstance;
    }

    public void setInstance (ExchangeSystem exchangeSystemInstance) {
        this.exchangeSystemInstance = exchangeSystemInstance;
    }
}