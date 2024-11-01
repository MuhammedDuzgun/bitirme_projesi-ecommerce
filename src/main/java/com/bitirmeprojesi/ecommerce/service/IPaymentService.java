package com.bitirmeprojesi.ecommerce.service;

import com.bitirmeprojesi.ecommerce.model.Order;
import com.bitirmeprojesi.ecommerce.model.PaymentOrder;
import com.bitirmeprojesi.ecommerce.model.User;
import com.stripe.exception.StripeException;

import java.util.Set;

public interface IPaymentService {
    PaymentOrder createOrder(User user, Set<Order> orders);
    PaymentOrder getPaymentOrderById(Long orderId);
    PaymentOrder getPaymentOrderByPaymentId(String paymentId);
    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId);
    String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;
}
