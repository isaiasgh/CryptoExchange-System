package com.globant.model.Orders;

import com.globant.model.System.Cryptocurrency;

import java.math.BigDecimal;

public class SellingOrder extends Order {
    private BigDecimal minimumPrice;

    public SellingOrder(Cryptocurrency cryptocurrencyType, String amount, String minimumPrice) {
        super (cryptocurrencyType, amount);
        this.minimumPrice = new BigDecimal(minimumPrice);
    }

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }
}