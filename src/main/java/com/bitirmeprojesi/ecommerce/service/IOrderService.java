package com.bitirmeprojesi.ecommerce.service;

import com.bitirmeprojesi.ecommerce.domain.OrderStatus;
import com.bitirmeprojesi.ecommerce.model.*;

import java.util.List;
import java.util.Set;

public interface IOrderService {

    Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
    Order findOrderById(Long id);
    List<Order> usersOrderHistory(Long userId);
    List<Order> sellersOrder(Long sellerId);
    Order updateOrderStatus(Long orderId, OrderStatus orderStatus);
    Order cancelOrder(Long orderId, User user);
    OrderItem getOrderItemById(Long id);
}
