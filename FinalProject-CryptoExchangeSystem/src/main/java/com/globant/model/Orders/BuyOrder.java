package com.globant.model.Orders;

import com.globant.model.System.Cryptocurrency;

import java.math.BigDecimal;

public class BuyOrder extends Order {
    private BigDecimal maximumPrice;

    public BuyOrder (Cryptocurrency cryptocurrencyType, String amount, String maximumPrice) {
        super (cryptocurrencyType, amount);
        this.maximumPrice = new BigDecimal(maximumPrice);
    }

    public BigDecimal getMaximumPrice() {
        return maximumPrice;
    }
}