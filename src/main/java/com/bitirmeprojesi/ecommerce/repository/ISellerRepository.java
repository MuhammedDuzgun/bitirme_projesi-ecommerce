package com.bitirmeprojesi.ecommerce.repository;

import com.bitirmeprojesi.ecommerce.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);
}
