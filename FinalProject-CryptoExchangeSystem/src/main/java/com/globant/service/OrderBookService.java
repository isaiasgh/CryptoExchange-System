package com.globant.service;

import com.globant.model.finance.Wallet;
import com.globant.model.orders.BuyOrder;
import com.globant.model.orders.Order;
import com.globant.model.orders.SellingOrder;
import com.globant.model.system.Cryptocurrency;
import com.globant.model.system.ExchangeSystem;
import com.globant.model.system.User;

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

    public Order createBuyOrder (Cryptocurrency crypto, BigDecimal amount, BigDecimal maximumPrice, User owner) {
        int id = ExchangeSystem.getInstance().getOrderBook().getNextOrderID();
        BuyOrder newBuyOrder = new BuyOrder(crypto, amount, maximumPrice, owner, id);
        return ExchangeSystem.getInstance().getOrderBook().addOrder(newBuyOrder);
    }

    public Order createSellingOrder (Cryptocurrency crypto, BigDecimal amount, BigDecimal minimumPrice, User owner) {
        int id = ExchangeSystem.getInstance().getOrderBook().getNextOrderID();
        SellingOrder newSellingOrder = new SellingOrder(crypto, amount, minimumPrice, owner, id);
        return ExchangeSystem.getInstance().getOrderBook().addOrder(newSellingOrder);
    }

    public Order getOrderSelected (int id) {
        List <SellingOrder> sellingOrders = ExchangeSystem.getInstance().getOrderBook().getSellingOrders();
        List <BuyOrder> buyOrders = ExchangeSystem.getInstance().getOrderBook().getBuyOrders();

        for (SellingOrder sellingOrder : sellingOrders) {
            if (sellingOrder.getID() == id) return sellingOrder;
        }

        for (BuyOrder buyOrder : buyOrders) {
            if (buyOrder.getID() == id) return buyOrder;
        }

        return null;
    }

    public boolean removeOrder (Order order) {
        if (order instanceof BuyOrder buyOrder) {
            return ExchangeSystem.getInstance().getOrderBook().removeBuyOrder(buyOrder);
        }

        if (order instanceof  SellingOrder sellingOrder) {
            return ExchangeSystem.getInstance().getOrderBook().removeSellingOrder(sellingOrder);
        }

        return false;
    }
}