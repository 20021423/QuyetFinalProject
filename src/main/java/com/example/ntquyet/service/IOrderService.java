package com.example.ntquyet.service;

import com.example.ntquyet.model.OrderItem;
import com.example.ntquyet.model.User;
import java.util.List;

public interface IOrderService {
    void placeOrder(User user, List<OrderItem> cartItems);
}
