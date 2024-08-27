package com.globant.model.Orders;

import java.math.BigDecimal;

public class BuyOrder extends Order {
    private BigDecimal maximumPrice;

    public BuyOrder (String amount, String maximumPrice) {
        super (amount);
        this.maximumPrice = new BigDecimal(maximumPrice);
    }
}