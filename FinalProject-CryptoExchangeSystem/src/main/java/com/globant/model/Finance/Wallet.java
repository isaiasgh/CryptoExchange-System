package com.globant.model.Finance;

import com.globant.model.System.Cryptocurrency;

import java.io.Serializable;
import java.util.HashMap;
import java.math.BigDecimal;

public class Wallet implements Serializable {
    private HashMap <Cryptocurrency, BigDecimal> cryptocurrenciesBalance;
    private BigDecimal fiatMoneyBalance;

    public Wallet () {
        cryptocurrenciesBalance = new HashMap <> ();
        fiatMoneyBalance = new BigDecimal(0);
    }
}