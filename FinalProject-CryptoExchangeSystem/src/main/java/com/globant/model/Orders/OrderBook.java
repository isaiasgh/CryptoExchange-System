package com.globant.model.Orders;

import com.globant.service.Observer;
import com.globant.service.OrderMatchingService;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class OrderBook implements Serializable {
    private List<Observer> observers = new ArrayList<>();

    private List <BuyOrder> buyOrders;
    private List <SellingOrder> sellingOrders;

    public OrderBook () {
        this.buyOrders = new ArrayList<>();
        this.sellingOrders = new ArrayList<>();

        attach(OrderMatchingService.getInstance());
    }

    public boolean addOrder (Order order) {
        if (order instanceof  BuyOrder) {
            return addBuyOrder((BuyOrder) order);
        }

        if (order instanceof SellingOrder) {
            return addSellingOrder((SellingOrder) order);
        }
        return false;
    }

    public boolean removeBuyOrder (BuyOrder buyOrder) {
        buyOrders.remove(buyOrder);
        return true;
    }

    public boolean removeSellingOrder (SellingOrder sellingOrder) {
        sellingOrders.remove(sellingOrder);
        return true;
    }

    public boolean addBuyOrder (BuyOrder buyOrder) {
        buyOrders.add(buyOrder);
        notifyObservers(buyOrder);
        return true;
    }

    public boolean addSellingOrder (SellingOrder sellingOrder) {
        sellingOrders.add(sellingOrder);
        notifyObservers(sellingOrder);
        return true;
    }

    public void notifyObservers(Order order) {
        for (Observer observer : observers) {
            observer.update(order);
        }
    }

    public void attach (Observer observer) {
        observers.add(observer);
    }

    public void dettach (Observer observer) {
        observers.remove(observer);
    }

    public List<BuyOrder> getBuyOrders() {
        return buyOrders;
    }

    public List<SellingOrder> getSellingOrders() {
        return sellingOrders;
    }
}