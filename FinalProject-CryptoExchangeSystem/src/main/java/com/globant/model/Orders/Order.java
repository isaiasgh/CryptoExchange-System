package com.globant.model.Orders;

import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.User;

import java.io.Serializable;
import java.math.BigDecimal;

public class Order implements Serializable {
    private Cryptocurrency cryptocurrencyType;
    private BigDecimal amount;
    private User owner;

    public Order (Cryptocurrency cryptocurrencyType, String amount) {
        this.amount = new BigDecimal (amount);
        this.cryptocurrencyType = cryptocurrencyType;
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