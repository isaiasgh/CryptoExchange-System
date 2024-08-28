package com.globant.service;

import com.globant.model.Finance.Transaction;
import com.globant.model.Finance.Wallet;
import com.globant.model.Orders.BuyOrder;
import com.globant.model.Orders.SellingOrder;
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
        return ExchangeSystem.getInstance().getCryptocurrencyByShorthandSymbol(shorthandSymbol);
    }

    public boolean checkEnoughFunds(Wallet wallet, BigDecimal amount) {
        if (wallet.getFiatMoneyBalance().compareTo(amount) >= 0) {
            return true;
        }

        throw new InsufficientFundsException();
    }

    public boolean checkEnoughFunds(Wallet wallet, BigDecimal amount, Cryptocurrency crypto) {
        if (wallet.getCryptocurrenciesBalance().get(crypto).compareTo(amount) >= 0) {
            return true;
        }

        throw new InsufficientFundsException();
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

    public boolean executeTrade (BuyOrder buyOrder, SellingOrder sellingOrder) {
        Cryptocurrency crypto = sellingOrder.getCryptocurrencyType();
        BigDecimal fiatMoneyAmount = sellingOrder.getMinimumPrice();
        BigDecimal cryptoMoneyAmount = sellingOrder.getAmount();
        User buyer = buyOrder.getOwner();
        User seller = sellingOrder.getOwner();

        if (!buyer.getWallet().subtractFiatMoney(fiatMoneyAmount)) return false;

        seller.getWallet().addFiatMoney(fiatMoneyAmount);

        if (!seller.getWallet().subtractCryptoBalance(crypto, cryptoMoneyAmount)) {
            seller.getWallet().subtractFiatMoney(fiatMoneyAmount);
            return false;
        }

        buyer.getWallet().addCryptoBalance(crypto, cryptoMoneyAmount);
        return true;
    }

    public boolean generateTransaction (User buyer, User seller, BigDecimal amount, BigDecimal price, Cryptocurrency crypto) {
        Transaction buyTransaction = new Transaction (crypto, amount, price, 'B');
        Transaction sellingTransaction = new Transaction (crypto, amount, price, 'S');

        buyer.addTransaction(buyTransaction);
        buyer.addTransaction(sellingTransaction);

        return true;
    }
}