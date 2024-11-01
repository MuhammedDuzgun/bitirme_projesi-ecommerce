package com.bitirmeprojesi.ecommerce.controller;

import com.bitirmeprojesi.ecommerce.exception.SellerException;
import com.bitirmeprojesi.ecommerce.model.*;
import com.bitirmeprojesi.ecommerce.response.ApiResponse;
import com.bitirmeprojesi.ecommerce.response.PaymentLinkResponse;
import com.bitirmeprojesi.ecommerce.service.*;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final IPaymentService paymentService;
    private final IUserService userService;
    private final ISellerService sellerService;
    private final ISellerReportService sellerReportService;
    private final ITransactionService transactionService;

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccess(@RequestHeader("Authorization") String jwt,
                                                      @PathVariable("paymentId") String paymentId,
                                                      @RequestParam String paymentLinkId) throws SellerException {
        User user = userService.findUserByJwtToken(jwt);

        PaymentLinkResponse paymentLinkResponse;

        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);

        boolean paymentSuccess = paymentService.proceedPaymentOrder(paymentOrder, paymentId, paymentLinkId);

        if (paymentSuccess) {
            for (Order order : paymentOrder.getOrders()) {
                transactionService.createTransaction(order);
                Seller seller = sellerService.getSellerById(order.getSellerId());
                SellerReport report = sellerReportService.getSellerReport(seller);
                report.setTotalOrders(report.getTotalOrders() + 1);
                report.setTotalEarnings(report.getTotalEarnings() + order.getTotalSellingPrice());
                report.setTotalSales(report.getTotalSales() + order.getOrderItems().size());
                sellerReportService.updateSellerReport(report);
            }
        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Payment successful");

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

}
