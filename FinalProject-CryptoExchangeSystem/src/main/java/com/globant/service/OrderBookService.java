package com.globant.service;

import com.globant.model.Orders.BuyOrder;
import com.globant.model.Orders.SellingOrder;
import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.ExchangeSystem;
import com.globant.model.System.User;

import java.math.BigDecimal;

public class OrderBookService {
    public void createBuyOrder (Cryptocurrency crypto, BigDecimal amount, BigDecimal maximumPrice, User owner) {
        BuyOrder newBuyOrder = new BuyOrder(crypto, amount, maximumPrice, owner);
        ExchangeSystem.getInstance().getOrderBook().addOrder(newBuyOrder);
    }

    public void createSellingOrder (Cryptocurrency crypto, BigDecimal amount, BigDecimal minimumPrice, User owner) {
        SellingOrder newSellingOrder = new SellingOrder(crypto, amount, minimumPrice, owner);
        ExchangeSystem.getInstance().getOrderBook().addOrder(newSellingOrder);
    }
}