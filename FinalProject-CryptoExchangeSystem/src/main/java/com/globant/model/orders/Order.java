package com.globant.model.orders;

import com.globant.model.system.Cryptocurrency;
import com.globant.model.system.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order implements Serializable {
    private int ID;;
    private User owner;
    private Cryptocurrency cryptocurrencyType;
    private BigDecimal amount;
    private LocalDateTime creationTime;

    public Order (Cryptocurrency cryptocurrencyType, BigDecimal amount, User owner, int id) {
        creationTime = LocalDateTime.now();
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