package com.globant.model.Orders;

import java.math.BigDecimal;

public class SellingOrder extends Order {
    private BigDecimal minimumPrice;

    public SellingOrder(String amount, String minimumPrice) {
        super (amount);
        this.minimumPrice = new BigDecimal(minimumPrice);
    }
}