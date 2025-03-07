package com.example.ntquyet.service.impl;

import com.example.ntquyet.model.Order;
import com.example.ntquyet.model.OrderItem;
import com.example.ntquyet.model.User;
import com.example.ntquyet.repository.OrderRepository;
import com.example.ntquyet.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void placeOrder(User user, List<OrderItem> cartItems) {
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .items(cartItems)
                .build();
        orderRepository.save(order);
    }
}
