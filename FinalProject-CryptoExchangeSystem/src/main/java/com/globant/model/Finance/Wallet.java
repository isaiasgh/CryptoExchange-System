package com.globant.model.Finance;

import com.globant.model.System.Cryptocurrency;

import java.io.Serializable;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.List;

public class Wallet implements Serializable {
    private HashMap <Cryptocurrency, BigDecimal> cryptocurrenciesBalance;
    private BigDecimal fiatMoneyBalance;

    public Wallet (List<Cryptocurrency> cryptos) {
        cryptocurrenciesBalance = new HashMap <> ();

       for (Cryptocurrency crypto : cryptos) {
           cryptocurrenciesBalance.put(crypto, new BigDecimal("0"));
       }

        fiatMoneyBalance = new BigDecimal(0);
    }

    public void addFiatMoney (BigDecimal amount) {
        fiatMoneyBalance = fiatMoneyBalance.add(amount);
    }

    public HashMap<Cryptocurrency, BigDecimal> getCryptocurrenciesBalance() {
        return cryptocurrenciesBalance;
    }

    public BigDecimal getFiatMoneyBalance() {
        return fiatMoneyBalance;
    }
}