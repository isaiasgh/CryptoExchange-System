package com.globant.model.Finance;

import com.globant.model.System.Cryptocurrency;

import java.io.Serializable;
import java.math.BigDecimal;

public class Transaction implements Serializable {
    private Cryptocurrency cryptocurrency;
    private BigDecimal amountTraded;
    private BigDecimal price;
    private Character type;

    public Transaction (Cryptocurrency cryptocurrency, BigDecimal amountTraded, BigDecimal price, Character type) {
        this.cryptocurrency = cryptocurrency;
        this.amountTraded = amountTraded;
        this.price = price;
        this.type = type;
    }
}