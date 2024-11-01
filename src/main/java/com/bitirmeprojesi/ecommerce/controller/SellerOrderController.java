package com.bitirmeprojesi.ecommerce.controller;

import com.bitirmeprojesi.ecommerce.domain.OrderStatus;
import com.bitirmeprojesi.ecommerce.model.Order;
import com.bitirmeprojesi.ecommerce.model.Seller;
import com.bitirmeprojesi.ecommerce.service.IOrderService;
import com.bitirmeprojesi.ecommerce.service.ISellerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/seller/orders")
public class SellerOrderController {

    private final IOrderService orderService;
    private final ISellerService sellerService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(@RequestHeader("Authorization") String jwt) {
        Seller seller = sellerService.getSellerProfile(jwt);

        List<Order> orders = orderService.sellersOrder(seller.getId());

        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrder(@RequestHeader("Authorization") String jwt,
                                             @PathVariable("orderId") Long orderId,
                                             @PathVariable("orderStatus") OrderStatus orderStatus) {

        Order order = orderService.updateOrderStatus(orderId, orderStatus);
        return ResponseEntity.ok(order);
    }

}
