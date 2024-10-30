package com.bitirmeprojesi.ecommerce.repository;

import com.bitirmeprojesi.ecommerce.domain.AccountStatus;
import com.bitirmeprojesi.ecommerce.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus accountStatus);
}
