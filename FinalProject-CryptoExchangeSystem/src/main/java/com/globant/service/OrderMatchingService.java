package com.globant.service;

import com.globant.model.orders.BuyOrder;
import com.globant.model.orders.Order;
import com.globant.model.orders.OrderBook;
import com.globant.model.orders.SellingOrder;
import com.globant.model.system.Cryptocurrency;
import com.globant.model.system.ExchangeSystem;
import com.globant.model.system.User;
import com.globant.service.fluctuation.PriceFluctuationContext;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderMatchingService implements Observer, Serializable {
    private static OrderMatchingService orderMatchingService;

    private Map<Cryptocurrency, Integer> matchCount = new HashMap<>();
    private static final int FLUCTUATION_THRESHOLD = 5;
    private PriceFluctuationContext fluctuationContext;

    private OrderMatchingService () {
        for (Map.Entry <Cryptocurrency, BigDecimal> entry : ExchangeSystem.getInstance().getCryptocurrencies().entrySet()) {
            matchCount.put(entry.getKey(), 0);
        }
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
        FinanceService financeService = new FinanceService();

        User buyer = buyOrder.getOwner();
        User seller = sellingOrder.getOwner();
        BigDecimal amount = sellingOrder.getAmount();
        BigDecimal price = sellingOrder.getMinimumPrice();


        if (!financeService.executeTrade(buyOrder, sellingOrder)) {
            return;
        }

        OrderBook orderBook = ExchangeSystem.getInstance().getOrderBook();
        orderBook.removeBuyOrder (buyOrder);
        orderBook.removeSellingOrder (sellingOrder);

        financeService.generateTransaction(buyer, seller, amount, price, buyOrder.getCryptocurrencyType());
        incrementMatchCount(buyOrder.getCryptocurrencyType());
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

    private void incrementMatchCount(Cryptocurrency crypto) {
        int currentCounter = matchCount.get(crypto);
        currentCounter++;
        matchCount.put(crypto, currentCounter);
        if (currentCounter == FLUCTUATION_THRESHOLD) {
            updatePrices(crypto);
            matchCount.put(crypto, 0);
        }
    }

    private void updatePrices(Cryptocurrency crypto) {

    }

    private Object readResolve() {
        return getInstance();
    }
}