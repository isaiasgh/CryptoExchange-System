package com.globant.model.Finance;

import com.globant.model.System.Cryptocurrency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class Transaction implements Serializable {
    private String ID;
    private Cryptocurrency cryptocurrency;
    private BigDecimal amountTraded;
    private BigDecimal price;
    private Character type;

    public Transaction (Cryptocurrency cryptocurrency, BigDecimal amountTraded, BigDecimal price, Character type) {
        this.ID = UUID.randomUUID().toString();
        this.cryptocurrency = cryptocurrency;
        this.amountTraded = amountTraded;
        this.price = price;
        this.type = type;
    }

    public String getID() {
        return ID;
    }

    public Cryptocurrency getCryptocurrency() {
        return cryptocurrency;
    }

    public BigDecimal getAmountTraded() {
        return amountTraded;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Character getType() {
        return type;
    }
}