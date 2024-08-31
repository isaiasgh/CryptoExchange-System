package com.globant.service.fluctuation;

import com.globant.model.system.Cryptocurrency;

import java.io.Serializable;
import java.math.BigDecimal;

public class MatchBasedPriceFluctuationStrategy implements PriceFluctuationStrategy, Serializable {

    @Override
    public void applyFluctuation(Cryptocurrency crypto, BigDecimal newValue) {

    }
}