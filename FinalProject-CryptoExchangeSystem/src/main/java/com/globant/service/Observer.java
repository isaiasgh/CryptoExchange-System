package com.globant.service;

import com.globant.model.Orders.Order;

public interface Observer {
    void update(Order order);
}