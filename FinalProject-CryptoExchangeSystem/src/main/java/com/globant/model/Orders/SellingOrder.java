package com.globant.model.Orders;

import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.User;

import java.math.BigDecimal;

public class SellingOrder extends Order {
    private BigDecimal minimumPrice;

    public SellingOrder(Cryptocurrency cryptocurrencyType, BigDecimal amount, BigDecimal minimumPrice, User owner) {
        super (cryptocurrencyType, amount, owner);
        this.minimumPrice = minimumPrice;
    }

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }
}