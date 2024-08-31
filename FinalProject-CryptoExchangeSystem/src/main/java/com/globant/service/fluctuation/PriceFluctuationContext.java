package com.globant.service.fluctuation;

import com.globant.model.orders.SellingOrder;
import com.globant.model.system.Cryptocurrency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Deque;

public class PriceFluctuationContext implements Serializable {
    private PriceFluctuationStrategy strategy;
    private Deque <SellingOrder> sellingOrders;

    public PriceFluctuationContext(PriceFluctuationStrategy strategy) {
        this.strategy = strategy;
    }

    public void updatePrices (Cryptocurrency crypto, Deque <SellingOrder> sellingOrders) {
        if (strategy != null) {
            this.sellingOrders = sellingOrders;
            BigDecimal newValue = calculateAverageOrdersValue();
            strategy.applyFluctuation(crypto, newValue);
        }
    }

    private BigDecimal calculateAverageOrdersValue () {
        if (sellingOrders.isEmpty()) return BigDecimal.ZERO;

        BigDecimal total = new BigDecimal("0");
        int size = sellingOrders.size();

        while (!sellingOrders.isEmpty()) {
            total = total.add(sellingOrders.pop().getMinimumPrice());
        }

        return total.divide(new BigDecimal(size));
    }
}