package com.globant.service.fluctuation;

import com.globant.model.system.Cryptocurrency;

public interface PriceFluctuationStrategy {
    void applyFluctuation(Cryptocurrency crypto);
}