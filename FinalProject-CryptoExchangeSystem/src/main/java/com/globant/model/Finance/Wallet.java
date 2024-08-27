package com.globant.model.Finance;

import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.ExchangeSystem;

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

    public boolean addCryptoBalance  (Cryptocurrency crypto, BigDecimal amount) {
        BigDecimal newAmount = cryptocurrenciesBalance.get(crypto).add(amount);
        cryptocurrenciesBalance.put(crypto, newAmount);
        return true;
    }

    public boolean subtractCryptoBalance(Cryptocurrency crypto, BigDecimal cryptoMoneyAmount) {
        BigDecimal currentAmount = cryptocurrenciesBalance.get(crypto);

        if (cryptoMoneyAmount.compareTo(currentAmount) > 0) return false;

        BigDecimal newAmount = currentAmount.subtract(cryptoMoneyAmount);
        cryptocurrenciesBalance.put(crypto, newAmount);
        return true;
    }

    public boolean addFiatMoney (BigDecimal amount) {
        fiatMoneyBalance = fiatMoneyBalance.add(amount);
        return true;
    }

    public boolean subtractFiatMoney (BigDecimal amount) {
        if (amount.compareTo(fiatMoneyBalance) > 0) return false;

        fiatMoneyBalance = fiatMoneyBalance.subtract(amount);
        return true;
    }

    public HashMap<Cryptocurrency, BigDecimal> getCryptocurrenciesBalance() {
        return cryptocurrenciesBalance;
    }

    public BigDecimal getFiatMoneyBalance() {
        return fiatMoneyBalance;
    }
}