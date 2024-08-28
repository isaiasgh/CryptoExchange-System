package com.globant.model.Orders;

import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.User;

import java.math.BigDecimal;

public class BuyOrder extends Order {
    private BigDecimal maximumPrice;

    public BuyOrder (Cryptocurrency cryptocurrencyType, BigDecimal amount, BigDecimal maximumPrice, User owner) {
        super (cryptocurrencyType, amount, owner);
        this.maximumPrice = maximumPrice;
    }

    public BigDecimal getMaximumPrice() {
        return maximumPrice;
    }
}