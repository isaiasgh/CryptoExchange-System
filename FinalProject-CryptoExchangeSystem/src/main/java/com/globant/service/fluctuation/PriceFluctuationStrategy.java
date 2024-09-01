package com.globant.service.fluctuation;

import com.globant.model.system.Cryptocurrency;

import java.math.BigDecimal;

public interface PriceFluctuationStrategy {
    void applyFluctuation(Cryptocurrency crypto, BigDecimal newValue);
}