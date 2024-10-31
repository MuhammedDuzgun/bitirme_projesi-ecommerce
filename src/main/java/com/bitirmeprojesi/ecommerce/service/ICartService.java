package com.bitirmeprojesi.ecommerce.service;

import com.bitirmeprojesi.ecommerce.model.Cart;
import com.bitirmeprojesi.ecommerce.model.CartItem;
import com.bitirmeprojesi.ecommerce.model.Product;
import com.bitirmeprojesi.ecommerce.model.User;

public interface ICartService {

    public CartItem addCartItem(
            User user,
            Product product,
            String size,
            int quantity
    );

    public Cart findUserCart(User user);
}
