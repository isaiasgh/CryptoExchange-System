package com.globant.service;

import com.globant.model.orders.BuyOrder;
import com.globant.model.orders.Order;
import com.globant.model.orders.OrderBook;
import com.globant.model.orders.SellingOrder;
import com.globant.model.system.Cryptocurrency;
import com.globant.model.system.ExchangeSystem;
import com.globant.model.system.User;
import com.globant.service.strategy.MatchBasedPriceFluctuationStrategy;
import com.globant.service.strategy.PriceFluctuationContext;
import com.globant.service.strategy.PriceFluctuationStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.List;

public class OrderMatchingService implements Observer, Serializable {
    private static OrderMatchingService orderMatchingService;

    private Map<Cryptocurrency, PriorityQueue<SellingOrder>> matchCount = new HashMap<>();
    private int FLUCTUATION_THRESHOLD = 10;
    private PriceFluctuationContext fluctuationContext = new PriceFluctuationContext(new MatchBasedPriceFluctuationStrategy());

    private OrderMatchingService () {
        matchCount.put(ExchangeSystem.getInstance().getBitcoin(), new PriorityQueue<>());
        matchCount.put(ExchangeSystem.getInstance().getDogecoin(), new PriorityQueue<>());
        matchCount.put(ExchangeSystem.getInstance().getEthereum(), new PriorityQueue<>());
    }

    public static OrderMatchingService getInstance () {
        if (orderMatchingService == null) {
            orderMatchingService = new OrderMatchingService ();
        }
        return orderMatchingService;
    }

    private void matchBuyOrder (BuyOrder buyOrder) {
        List<SellingOrder> sellingOrders = ExchangeSystem.getInstance().getOrderBook().getSellingOrders();
        ArrayList<SellingOrder> matchingOrders = new ArrayList<>();

        for (SellingOrder order : sellingOrders) {

            if (areSameAmountAndCryptoType(buyOrder, order)) {
                if (buyOrder.getMaximumPrice().compareTo(order.getMinimumPrice()) >= 0) {
                    matchingOrders.add(order);
                }
            }
        }

        if (matchingOrders.isEmpty()) return;

        SellingOrder sellingOrderSelected = matchingOrders.get(0);
        makeTradeEffective(buyOrder, sellingOrderSelected);
    }

    private void matchSellingOrder (SellingOrder sellingOrder) {
        List<BuyOrder> buyOrders = ExchangeSystem.getInstance().getOrderBook().getBuyOrders();
        ArrayList<BuyOrder> matchingOrders = new ArrayList<>();

        for (BuyOrder order : buyOrders) {
            if (areSameAmountAndCryptoType(order, sellingOrder)) {
                if (order.getMaximumPrice().compareTo(sellingOrder.getMinimumPrice()) >= 0) {
                    matchingOrders.add(order);
                }
            }
        }

        if (matchingOrders.isEmpty()) return;

        BuyOrder buyOrderSelected = matchingOrders.get(0);
        makeTradeEffective(buyOrderSelected, sellingOrder);
    }

    private boolean areSameAmountAndCryptoType (BuyOrder buyOrder, SellingOrder sellingOrder) {
        boolean sameAmount = buyOrder.getAmount().equals(sellingOrder.getAmount());
        boolean sameCryptoType = buyOrder.getCryptocurrencyType().getUniqueID().equals(sellingOrder.getCryptocurrencyType().getUniqueID());
        return sameAmount && sameCryptoType && (buyOrder.getOwner().getId() != sellingOrder.getOwner().getId());
    }

    private void makeTradeEffective (BuyOrder buyOrder, SellingOrder sellingOrder) {
        User buyer = buyOrder.getOwner();
        User seller = sellingOrder.getOwner();
        BigDecimal amount = sellingOrder.getAmount();
        BigDecimal price = sellingOrder.getMinimumPrice();


        if (!FinanceService.executeTrade(buyOrder, sellingOrder)) {
            return;
        }

        OrderBook orderBook = ExchangeSystem.getInstance().getOrderBook();
        orderBook.removeBuyOrder (buyOrder);
        orderBook.removeSellingOrder (sellingOrder);

        FinanceService.generateTransaction(buyer, seller, amount, price, buyOrder.getCryptocurrencyType());
        matchCount.get(sellingOrder.getCryptocurrencyType()).offer(sellingOrder);
        incrementMatchCount(sellingOrder.getCryptocurrencyType());
        ExchangeSystemService.write();
    }

    @Override
    public void update(Order order) {
        if (order instanceof BuyOrder) {
            matchBuyOrder((BuyOrder) order);
        }

        if (order instanceof SellingOrder) {
            matchSellingOrder((SellingOrder) order);
        }
    }

    public boolean setStrategy (PriceFluctuationStrategy newStrategy) {
        fluctuationContext.setStrategy(newStrategy);
        return true;
    }

    public boolean setFrequency (int frequencyOfFluctuation) {
        if (frequencyOfFluctuation <= 0) {
            return false;
        }

        FLUCTUATION_THRESHOLD = frequencyOfFluctuation;
        return true;
    }

    private void incrementMatchCount(Cryptocurrency crypto) {
        if (matchCount.get(crypto).size() == FLUCTUATION_THRESHOLD) {
            updatePrices(crypto);
            matchCount.put(crypto, new PriorityQueue<>());
        }
    }

    private void updatePrices(Cryptocurrency crypto) {
        fluctuationContext.updatePrices(crypto, matchCount.get(crypto));
    }
}