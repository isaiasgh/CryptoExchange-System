package com.globant.model.System;

import com.globant.model.Orders.OrderBook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExchangeSystem implements Serializable {
    private static ExchangeSystem exchangeSystemInstance;

    private List <User> users;
    private List <Cryptocurrency> cryptocurrencies;
    private OrderBook orderBook;
    private int idUserCounter;

    private ExchangeSystem () {
        this.users = new ArrayList<>();
        this.cryptocurrencies = new ArrayList<>();
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

    @Override
    public String toString () {
        return users.toString();
    }
}