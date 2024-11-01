package com.bitirmeprojesi.ecommerce.repository;

import com.bitirmeprojesi.ecommerce.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWishlistRepository extends JpaRepository<Wishlist, Long> {

    Wishlist findByUserId(Long userId);

}
