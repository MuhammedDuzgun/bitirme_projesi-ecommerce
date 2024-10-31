package com.bitirmeprojesi.ecommerce.service.impl;

import com.bitirmeprojesi.ecommerce.model.Cart;
import com.bitirmeprojesi.ecommerce.model.CartItem;
import com.bitirmeprojesi.ecommerce.model.User;
import com.bitirmeprojesi.ecommerce.repository.ICartItemRepository;
import com.bitirmeprojesi.ecommerce.service.ICartItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CartItemService implements ICartItemService {

    private final ICartItemRepository cartItemRepository;

    @Override
    public CartItem findCartItemById(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Cart Item Not Found with id: " + id));
    }

    @Override
    public CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem) {
        CartItem item = findCartItemById(cartItemId);
        User cartItemUser = item.getCart().getUser();

        if (cartItemUser.getId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setMrpPrice(item.getQuantity()*item.getProduct().getMrpPrice());
            item.setSellingPrice(item.getQuantity()*item.getProduct().getSellingPrice());
            return cartItemRepository.save(item);
        }

        throw new RuntimeException("You cannot update this cart item");
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) {
        CartItem item = findCartItemById(cartItemId);
        User cartItemUser = item.getCart().getUser();

        if (cartItemUser.getId().equals(userId)) {
            cartItemRepository.delete(item);
        } else {
            throw new RuntimeException("You cannot delete this cart item");
        }
    }
}
