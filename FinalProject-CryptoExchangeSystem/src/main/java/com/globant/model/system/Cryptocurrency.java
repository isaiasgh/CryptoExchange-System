package com.globant.model.system;

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

    public String getName() {
        return name;
    }

    public String getShorthandSymbol () {
        return shorthandSymbol;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    @Override
    public String toString () {
        return name + "(ID: " + uniqueID + ")\nMarket Price: $" + marketPrice;
    }
}