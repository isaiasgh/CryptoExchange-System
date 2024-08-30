package com.globant.service;

import com.globant.model.Finance.Wallet;
import com.globant.model.Orders.BuyOrder;
import com.globant.model.Orders.SellingOrder;
import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.ExchangeSystem;
import com.globant.model.System.User;

import java.math.BigDecimal;
import java.util.List;

public class OrderBookService {
    public static BigDecimal cryptoAmountInSellingOrders (Cryptocurrency crypto, Wallet wallet, User user) {
        List<SellingOrder> sellingOrders = ExchangeSystem.getInstance().getOrderBook().getSellingOrders();
        BigDecimal cryptoAmount = new BigDecimal("0");

        for (SellingOrder s : sellingOrders) {
            if (s.getOwner().getId() == user.getId() && s.getCryptocurrencyType().getUniqueID().equals(crypto.getUniqueID())) {
                cryptoAmount = cryptoAmount.add(s.getAmount());
            }
        }
        return cryptoAmount;
    }

    public static BigDecimal fiatAmountInBuyOrders (Wallet wallet, User user) {
        List<BuyOrder> buyOrders = ExchangeSystem.getInstance().getOrderBook().getBuyOrders();
        BigDecimal fiatAmount = new BigDecimal("0");

        for (BuyOrder b : buyOrders) {
            if (b.getOwner().getId() == user.getId()) {
                fiatAmount = fiatAmount.add(b.getMaximumPrice());
            }
        }
        return fiatAmount;
    }

    public void createBuyOrder (Cryptocurrency crypto, BigDecimal amount, BigDecimal maximumPrice, User owner) {
        BuyOrder newBuyOrder = new BuyOrder(crypto, amount, maximumPrice, owner);
        ExchangeSystem.getInstance().getOrderBook().addOrder(newBuyOrder);
    }

    public void createSellingOrder (Cryptocurrency crypto, BigDecimal amount, BigDecimal minimumPrice, User owner) {
        SellingOrder newSellingOrder = new SellingOrder(crypto, amount, minimumPrice, owner);
        ExchangeSystem.getInstance().getOrderBook().addOrder(newSellingOrder);
    }
}