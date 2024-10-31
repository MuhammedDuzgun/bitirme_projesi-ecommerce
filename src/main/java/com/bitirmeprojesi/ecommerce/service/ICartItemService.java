package com.bitirmeprojesi.ecommerce.service;

import com.bitirmeprojesi.ecommerce.model.CartItem;

public interface ICartItemService {

    CartItem findCartItemById(Long id);
    CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem);
    void deleteCartItem(Long userId, Long cartItemId);
}
