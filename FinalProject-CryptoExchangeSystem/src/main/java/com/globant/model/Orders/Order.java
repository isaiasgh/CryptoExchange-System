package com.globant.model.Orders;

import java.io.Serializable;
import java.math.BigDecimal;

public class Order implements Serializable {
    private BigDecimal amount;

    public Order (String amount) {
        this.amount = new BigDecimal (amount);
    }
}