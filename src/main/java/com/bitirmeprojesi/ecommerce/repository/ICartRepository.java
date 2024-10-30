package com.bitirmeprojesi.ecommerce.repository;

import com.bitirmeprojesi.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICartRepository extends JpaRepository<Cart, Long> {

}
