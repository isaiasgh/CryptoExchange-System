package com.globant.model.Orders;

import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.User;

import java.math.BigDecimal;

public class BuyOrder extends Order {
    private BigDecimal maximumPrice;

    public BuyOrder (Cryptocurrency cryptocurrencyType, BigDecimal amount, BigDecimal maximumPrice, User owner, int id) {
        super (cryptocurrencyType, amount, owner, id);
        this.maximumPrice = maximumPrice;
    }

    public BigDecimal getMaximumPrice() {
        return maximumPrice;
    }
}