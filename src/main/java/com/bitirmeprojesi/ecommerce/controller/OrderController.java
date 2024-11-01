package com.bitirmeprojesi.ecommerce.controller;

import com.bitirmeprojesi.ecommerce.domain.PaymentMethod;
import com.bitirmeprojesi.ecommerce.exception.SellerException;
import com.bitirmeprojesi.ecommerce.model.*;
import com.bitirmeprojesi.ecommerce.repository.IPaymentOrderRepository;
import com.bitirmeprojesi.ecommerce.response.PaymentLinkResponse;
import com.bitirmeprojesi.ecommerce.service.*;
import com.bitirmeprojesi.ecommerce.service.impl.SellerService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final IOrderService orderService;
    private final IUserService userService;
    private final ICartService cartService;
    private final ISellerService sellerService;
    private final ISellerReportService sellerReportService;
    private final IPaymentService paymentService;
    private final IPaymentOrderRepository paymentOrderRepository;



    @PostMapping
    public ResponseEntity<PaymentLinkResponse> createOrder(@RequestHeader("Authorization") String jwt,
                                                           @RequestParam PaymentMethod paymentMethod,
                                                           @RequestBody Address shippingAddress) throws StripeException {

        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);

        Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);

        PaymentOrder paymentOrder = paymentService.createOrder(user, orders);

        PaymentLinkResponse response = new PaymentLinkResponse();

        if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
//            PaymentLink payment = paymentService.createRazorpayPaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());
//            String paymentUrl = payment.get("short_url");
//            String paymentUrlId = payment.get("id");

//            response.setPayment_link_url(paymentUrl);
//
//            paymentOrder.setPaymentLinkId(paymentUrlId);
//
//            paymentOrderRepository.save(paymentOrder);
        } else {
            String paymentUrl = paymentService.createStripePaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());
            response.setPayment_link_url(paymentUrl);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistory(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.usersOrderHistory(user.getId());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String jwt,
                                              @PathVariable("orderId") Long orderId) {
        User user = userService.findUserByJwtToken(jwt);
        Order orders = orderService.findOrderById(orderId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(@RequestHeader("Authorization") String jwt,
                                                      @PathVariable("orderItemId") Long orderItemId) {
        User user = userService.findUserByJwtToken(jwt);
        OrderItem orderItem = orderService.getOrderItemById(orderItemId);
        return ResponseEntity.ok(orderItem);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@RequestHeader("Authorization") String jwt,
                                             @PathVariable("orderId") Long orderId) throws SellerException {
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.cancelOrder(orderId, user);

        Seller seller = sellerService.getSellerById(order.getSellerId());
        SellerReport report = sellerReportService.getSellerReport(seller);

        report.setCanceledOrders(report.getCanceledOrders() + 1);
        report.setTotalRefunds(report.getTotalRefunds() + order.getTotalSellingPrice());
        sellerReportService.updateSellerReport(report);

        return ResponseEntity.ok(order);
    }

}
