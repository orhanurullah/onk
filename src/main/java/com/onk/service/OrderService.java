package com.onk.service;

import com.onk.core.results.DataResult;
import com.onk.model.Cart;
import com.onk.model.Order;

public interface OrderService {

    DataResult<Order> createOrder(Cart cart);
}
