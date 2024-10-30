package com.bitirmeprojesi.ecommerce.service;

import com.bitirmeprojesi.ecommerce.domain.AccountStatus;
import com.bitirmeprojesi.ecommerce.exception.SellerException;
import com.bitirmeprojesi.ecommerce.model.Seller;

import java.util.List;

public interface ISellerService {
    Seller getSellerProfile(String jwt);
    Seller createSeller(Seller seller);
    Seller getSellerById(Long id) throws SellerException;
    Seller getSellerByEmail(String email);
    List<Seller> getAllSellers(AccountStatus accountStatus);
    Seller updateSeller(Long id, Seller seller);
    void deleteSeller(Long id) throws SellerException;
    Seller verifyEmail(String email, String otp);
    Seller updateSellerAccountStatus(Long sellerId, AccountStatus accountStatus) throws SellerException;
}
