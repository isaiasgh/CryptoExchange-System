package com.globant.service;

import com.globant.model.finance.Transaction;
import com.globant.model.finance.Wallet;
import com.globant.model.orders.BuyOrder;
import com.globant.model.orders.SellingOrder;
import com.globant.model.system.Cryptocurrency;
import com.globant.model.system.ExchangeSystem;
import com.globant.model.system.User;
import com.globant.service.exceptions.ExceedingCryptoBalanceException;
import com.globant.service.exceptions.InsufficientExchangeFundsException;
import com.globant.service.exceptions.InsufficientFundsException;
import com.globant.service.exceptions.OrdersExceedFiatBalanceException;
import com.globant.util.BudgetCheckResult;

import java.math.BigDecimal;

public class FinanceService {
    private User user;

    public boolean deposit (BigDecimal amount) {
        Wallet wallet = user.getWallet();
        wallet.addFiatMoney(amount);
        return true;
    }

    public Cryptocurrency getCryptoSelected (String shorthandSymbol) {
        return ExchangeSystem.getInstance().getCryptocurrencyByShorthandSymbol(shorthandSymbol);
    }

    private boolean checkEnoughFiatFunds(Wallet wallet, BigDecimal amount) {
        BigDecimal totalFiatAmount = wallet.getFiatMoneyBalance();
        BigDecimal fiatAmountInBuyingOrders = OrderBookService.fiatAmountInBuyOrders (wallet, user);

        if (wallet.getFiatMoneyBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        if (totalFiatAmount.subtract(fiatAmountInBuyingOrders).compareTo(amount) < 0) {
            throw new OrdersExceedFiatBalanceException();
        }

        return true;
    }

    private boolean checkEnoughCryptoFunds(Wallet wallet, BigDecimal amount, Cryptocurrency crypto) {
        BigDecimal totalCryptoAmount = wallet.getCryptocurrenciesBalance().get(crypto);
        BigDecimal cryptoAmountInSellingOrders = OrderBookService.cryptoAmountInSellingOrders (crypto, wallet, user);

        if (totalCryptoAmount.subtract(cryptoAmountInSellingOrders).compareTo(amount) < 0) {
            throw new ExceedingCryptoBalanceException();
        }

        if (wallet.getCryptocurrenciesBalance().get(crypto).compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        return true;
    }

    public BudgetCheckResult handleEnoughFiatBudget(BigDecimal amount) {
        try {
            boolean hasEnoughFunds = checkEnoughFiatFunds(user.getWallet(), amount);
            return new BudgetCheckResult(hasEnoughFunds, null);
        } catch (InsufficientFundsException e) {
            return new BudgetCheckResult(false, "ERROR: You do not have enough funds in your account to place this buy order. Please try again later.");
        } catch (OrdersExceedFiatBalanceException e) {
            return new BudgetCheckResult(false, "ERROR: Your current buy orders have utilized all of your available fiat balance. Please deposit more funds and try again later.");
        }
    }

    public BudgetCheckResult handleEnoughCryptoBudget(BigDecimal amount, Cryptocurrency crypto) {
        try {
            boolean hasEnoughCrypto = checkEnoughCryptoFunds(user.getWallet(), amount, crypto);
            return new BudgetCheckResult(hasEnoughCrypto, null);
        } catch (InsufficientFundsException e) {
            return new BudgetCheckResult(false, "ERROR: You do not have enough cryptocurrency in your account to place this selling order. Please try again later.");
        } catch (ExceedingCryptoBalanceException e) {
            return new BudgetCheckResult(false, "ERROR: The total amount of cryptocurrencies in your sell orders matches your current balance. Please acquire more cryptocurrencies and try again later.");
        }
    }

    public BigDecimal buy (Cryptocurrency crypto, BigDecimal quantity) {
        Wallet wallet = user.getWallet();
        BigDecimal totalPrice = quantity.multiply(crypto.getMarketPrice());

        if (ExchangeSystem.getInstance().getCryptoTotalAmount(crypto).compareTo(quantity) < 0) {
            throw new InsufficientExchangeFundsException();
        }

        wallet.subtractFiatMoney(totalPrice);
        BigDecimal newTotalAmount = ExchangeSystem.getInstance().getCryptoTotalAmount(crypto).subtract(quantity);
        ExchangeSystem.getInstance().updateCrypto(crypto, newTotalAmount);
        wallet.addCryptoBalance(crypto, quantity);
        return totalPrice;
    }

    public static boolean executeTrade (BuyOrder buyOrder, SellingOrder sellingOrder) {
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

    public static boolean generateTransaction (User buyer, User seller, BigDecimal amount, BigDecimal price, Cryptocurrency crypto) {
        Transaction buyTransaction = new Transaction (crypto, amount, price, 'B');
        Transaction sellingTransaction = new Transaction (crypto, amount, price, 'S');

        buyer.addTransaction(buyTransaction);
        seller.addTransaction(sellingTransaction);

        return true;
    }

    public boolean generateTransaction (BigDecimal amount, BigDecimal price, Cryptocurrency crypto) {
        Transaction buyTransaction = new Transaction (crypto, amount, price, 'B');

        user.addTransaction(buyTransaction);
        return true;
    }

    public static BigDecimal getAvailableFiatMoney (User user) {
        BigDecimal fiatMoneyBalance = user.getWallet().getFiatMoneyBalance();
        BigDecimal fiatMoneyInOrders = OrderBookService.fiatAmountInBuyOrders(user.getWallet(), user);
        return fiatMoneyBalance.subtract(fiatMoneyInOrders);
    }

    public static BigDecimal getAvailableCrypto (User user, Cryptocurrency crypto) {
        BigDecimal cryptoInSellOrders = OrderBookService.cryptoAmountInSellingOrders(crypto, user.getWallet(), user);
        return user.getWallet().getCryptocurrenciesBalance().get(crypto).subtract(cryptoInSellOrders);
    }

    public void setUser (User user) {
        this.user = user;
    }
}