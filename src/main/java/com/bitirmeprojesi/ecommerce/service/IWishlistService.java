package com.bitirmeprojesi.ecommerce.service;

import com.bitirmeprojesi.ecommerce.model.Product;
import com.bitirmeprojesi.ecommerce.model.User;
import com.bitirmeprojesi.ecommerce.model.Wishlist;

public interface IWishlistService {

    Wishlist createWishlist(User user);
    Wishlist getWishlistByUserId(User user);
    Wishlist addProductToWishlist(User user, Product product);

}
