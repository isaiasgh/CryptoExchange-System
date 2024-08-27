package com.globant.model.Orders;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class OrderBook implements Serializable {
    private List <BuyOrder> buyOrders;
    private List <SellingOrder> sellingOrders;

    public OrderBook () {
        this.buyOrders = new ArrayList<>();
        this.sellingOrders = new ArrayList<>();
    }
}