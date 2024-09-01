package com.globant.service.strategy;

import com.globant.model.system.Cryptocurrency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class RandomPriceFluctuationStrategy implements PriceFluctuationStrategy, Serializable {
    private static final BigDecimal MIN_FLUCTUATION_PERCENTAGE = new BigDecimal("0.01");
    private static final BigDecimal MAX_FLUCTUATION_PERCENTAGE = new BigDecimal("0.05");
    private final Random random = new Random();

    @Override
    public void applyFluctuation(Cryptocurrency crypto, BigDecimal averageOrdersValue) {
        if (averageOrdersValue.compareTo(new BigDecimal("0")) == 0) return;

        BigDecimal oldValue = crypto.getMarketPrice();
        BigDecimal fluctuationPercentage = MIN_FLUCTUATION_PERCENTAGE.add(
                new BigDecimal(random.nextDouble()).multiply(MAX_FLUCTUATION_PERCENTAGE.subtract(MIN_FLUCTUATION_PERCENTAGE))
        );

        BigDecimal fluctuationAmount = oldValue.multiply(fluctuationPercentage).setScale(2, RoundingMode.HALF_UP);

        if (oldValue.compareTo(averageOrdersValue) >= 0) {
            BigDecimal newValue = oldValue.subtract(fluctuationAmount).max(BigDecimal.ZERO);
            crypto.setMarketPrice(newValue);
        } else {
            BigDecimal newValue = oldValue.add(fluctuationAmount);
            crypto.setMarketPrice(newValue);
        }
    }
}