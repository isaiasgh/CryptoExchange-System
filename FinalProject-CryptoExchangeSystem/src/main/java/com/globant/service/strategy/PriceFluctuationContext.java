package com.globant.service.strategy;

import com.globant.model.orders.SellingOrder;
import com.globant.model.system.Cryptocurrency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.PriorityQueue;

public class PriceFluctuationContext implements Serializable {
    private PriceFluctuationStrategy strategy;
    private PriorityQueue <SellingOrder> sellingOrders;

    public PriceFluctuationContext(PriceFluctuationStrategy strategy) {
        this.strategy = strategy;
    }

    public void updatePrices (Cryptocurrency crypto, PriorityQueue<SellingOrder> sellingOrders) {
        if (strategy != null) {
            this.sellingOrders = sellingOrders;
            BigDecimal newValue = calculateAverageOrdersValue();
            strategy.applyFluctuation(crypto, newValue);
        }
    }

    public void setStrategy(PriceFluctuationStrategy strategy) {
        this.strategy = strategy;
    }

    private BigDecimal calculateAverageOrdersValue () {
        if (sellingOrders.isEmpty()) return BigDecimal.ZERO;

        BigDecimal total = new BigDecimal("0");
        int size = sellingOrders.size();

        while (!sellingOrders.isEmpty()) {
            total = total.add(getCryptoValue(sellingOrders.poll()));
        }

        return total.divide(new BigDecimal(size));
    }

    private BigDecimal getCryptoValue (SellingOrder order) {
        BigDecimal quantity = order.getAmount();
        return order.getMinimumPrice().divide(quantity);
    }
}