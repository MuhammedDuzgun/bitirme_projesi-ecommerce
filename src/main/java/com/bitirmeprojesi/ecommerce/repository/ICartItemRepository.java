package com.bitirmeprojesi.ecommerce.repository;

import com.bitirmeprojesi.ecommerce.model.Cart;
import com.bitirmeprojesi.ecommerce.model.CartItem;
import com.bitirmeprojesi.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);

}
