package com.bitirmeprojesi.ecommerce.service.impl;

import com.bitirmeprojesi.ecommerce.model.Cart;
import com.bitirmeprojesi.ecommerce.model.CartItem;
import com.bitirmeprojesi.ecommerce.model.Product;
import com.bitirmeprojesi.ecommerce.model.User;
import com.bitirmeprojesi.ecommerce.repository.ICartItemRepository;
import com.bitirmeprojesi.ecommerce.repository.ICartRepository;
import com.bitirmeprojesi.ecommerce.service.ICartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CartServiceImpl implements ICartService {

    private final ICartRepository cartRepository;
    private final ICartItemRepository cartItemRepository;

    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) {
            Cart cart = findUserCart(user);
            CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart, product, size);
            if (isPresent == null) {
                CartItem cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setUserId(user.getId());
                cartItem.setSize(size);

                int totalPrice = quantity * product.getSellingPrice();
                cartItem.setSellingPrice(totalPrice);
                cartItem.setMrpPrice(quantity * product.getMrpPrice());

                cart.getCartItems().add(cartItem);
                cartItem.setCart(cart);

                return cartItemRepository.save(cartItem);
            }
        return isPresent;
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());

        /*ALT SATIR BEN EKLEDIM*/
        if (cart == null) {
            throw new RuntimeException("Cart Not Found");
        }

        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getMrpPrice();
            totalDiscountedPrice += cartItem.getSellingPrice();
            totalItem += cartItem.getQuantity();
        }

        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalSellingPrice(totalDiscountedPrice);
        cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountedPrice));

        return cart;
    }

    /*
    *
    * private methods
    *
    * */

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if (mrpPrice <= 0) {
            return 0;
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;

        return (int) discountPercentage;
    }

}
