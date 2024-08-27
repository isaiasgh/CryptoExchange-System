package com.globant.model.Orders;

import com.globant.model.System.Cryptocurrency;

import java.io.Serializable;
import java.math.BigDecimal;

public class Order implements Serializable {
    private Cryptocurrency cryptocurrencyType;
    private BigDecimal amount;

    public Order (Cryptocurrency cryptocurrencyType, String amount) {
        this.amount = new BigDecimal (amount);
        this.cryptocurrencyType = cryptocurrencyType;
    }
}