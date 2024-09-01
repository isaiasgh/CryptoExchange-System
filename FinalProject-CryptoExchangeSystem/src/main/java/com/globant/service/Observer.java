package com.globant.service;

import com.globant.model.orders.Order;

public interface Observer {
    void update(Order order);
}