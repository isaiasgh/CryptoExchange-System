package com.globant.service;

import com.globant.model.Finance.Wallet;
import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.ExchangeSystem;
import com.globant.model.System.User;

import java.math.BigDecimal;

public class FinanceService {
    public boolean deposit (User user, BigDecimal amount) {
        Wallet wallet = user.getWallet();
        wallet.addFiatMoney(amount);
        return true;
    }

    public Cryptocurrency getCryptoSelected (String shorthandSymbol) {
        Cryptocurrency crypto = ExchangeSystem.getInstance().getCryptocurrencyByShorthandSymbol(shorthandSymbol);
        return crypto;
    }

    public BigDecimal buy (Wallet wallet, Cryptocurrency crypto, BigDecimal amount) {
        BigDecimal totalPrice = amount.multiply(crypto.getMarketPrice());

        if (wallet.getFiatMoneyBalance().compareTo(totalPrice) < 0) {
            throw new InsufficientFundsException();
        }

        if (ExchangeSystem.getInstance().getCryptoTotalAmount(crypto).compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        wallet.subtractFiatMoney(totalPrice);
        BigDecimal newTotalAmount = ExchangeSystem.getInstance().getCryptoTotalAmount(crypto).subtract(amount);
        ExchangeSystem.getInstance().updateCrypto(crypto, newTotalAmount);
        wallet.addCryptoBalance(crypto, amount);
        return totalPrice;
    }
}