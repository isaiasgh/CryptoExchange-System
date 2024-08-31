package com.globant.model.Orders;

import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class Order implements Serializable {
    private Cryptocurrency cryptocurrencyType;
    private BigDecimal amount;
    private User owner;
    private int ID;;

    public Order (Cryptocurrency cryptocurrencyType, BigDecimal amount, User owner, int id) {
        this.ID = id;
        this.owner = owner;
        this.amount = amount;
        this.cryptocurrencyType = cryptocurrencyType;
    }

    public int getID() {
        return ID;
    }

    public Cryptocurrency getCryptocurrencyType() {
        return cryptocurrencyType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public User getOwner() {
        return owner;
    }
}