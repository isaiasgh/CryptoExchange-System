package com.globant.model.Finance;

import com.globant.model.System.Cryptocurrency;

import java.io.Serializable;
import java.math.BigDecimal;

public class Transaction implements Serializable {
    private Cryptocurrency cryptocurrency;
    private BigDecimal amountTraded;
    private BigDecimal price;
    private Character type;

    public Transaction (Cryptocurrency cryptocurrency, String amountTraded, String price, Character type) {
        this.cryptocurrency = cryptocurrency;
        this.amountTraded = new BigDecimal (amountTraded);
        this.price = new BigDecimal (price);
        this.type = type;
    }
}