package com.globant.model.System;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class Cryptocurrency implements Serializable {
    private String uniqueID;
    private String shorthandSymbol;
    private String name;
    private BigDecimal marketPrice;

    public Cryptocurrency(String shorthandSymbol, String name, String marketPrice) {
        this.uniqueID = UUID.randomUUID().toString();
        this.shorthandSymbol = shorthandSymbol;
        this.name = name;
        this.marketPrice = new BigDecimal(marketPrice);
    }
}