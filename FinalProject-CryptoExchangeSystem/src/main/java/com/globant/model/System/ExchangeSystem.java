package com.globant.model.System;

import com.globant.model.Orders.OrderBook;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangeSystem implements Serializable {
    private static ExchangeSystem exchangeSystemInstance;

    private List <User> users;
    private Map<Cryptocurrency, BigDecimal> cryptocurrencies = new HashMap<>();
    private OrderBook orderBook;
    private int idUserCounter;

    private ExchangeSystem () {
        this.users = new ArrayList<>();
        this.cryptocurrencies = new HashMap<>();
        this.orderBook = new OrderBook();
        this.idUserCounter = 0;
    }

    public int getNextUserId () {
        idUserCounter++;
        return idUserCounter;
    }

    public boolean containsUserByEmail (String email) {
        for (User u : users) {
            if (u.getEmail().equals(email)) return true;
        }
        return false;
    }

    public void addUser (User user) {
        users.add(user);
    }

    public User getUser (String email, String password) {
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                if (password.equals(u.getPassword())) return u;
                else return null;
            }
        }
        return null;
    }

    public static ExchangeSystem getInstance () {
        if (exchangeSystemInstance == null) {
            exchangeSystemInstance = new ExchangeSystem ();
        }
        return exchangeSystemInstance;
    }

    public static void setInstance (ExchangeSystem instance) {
        exchangeSystemInstance = instance;
    }

    public Cryptocurrency getCryptocurrencyByShorthandSymbol (String shortHandSymbol) {
        for (Map.Entry <Cryptocurrency, BigDecimal> entry : cryptocurrencies.entrySet()) {
            if (entry.getKey().getShortHandSymbol().equals(shortHandSymbol)) return entry.getKey();
        }
        return null;
    }

    @Override
    public String toString () {
        return users.toString();
    }
}