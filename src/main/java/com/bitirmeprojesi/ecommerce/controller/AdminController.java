package com.bitirmeprojesi.ecommerce.controller;

import com.bitirmeprojesi.ecommerce.domain.AccountStatus;
import com.bitirmeprojesi.ecommerce.exception.SellerException;
import com.bitirmeprojesi.ecommerce.model.Seller;
import com.bitirmeprojesi.ecommerce.service.impl.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AdminController {

    private final SellerService sellerService;

    @PatchMapping("/seller/{id}/status/{status}")
    public ResponseEntity<Seller> updateSellerStatus(@PathVariable("id") Long id,
                                                     @PathVariable("status") AccountStatus status) throws SellerException {
        Seller updatedSeller = sellerService.updateSellerAccountStatus(id, status);
        return ResponseEntity.ok(updatedSeller);
    }

}
