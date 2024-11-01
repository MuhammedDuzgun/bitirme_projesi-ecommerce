package com.bitirmeprojesi.ecommerce.service.impl;

import com.bitirmeprojesi.ecommerce.model.Product;
import com.bitirmeprojesi.ecommerce.model.User;
import com.bitirmeprojesi.ecommerce.model.Wishlist;
import com.bitirmeprojesi.ecommerce.repository.IWishlistRepository;
import com.bitirmeprojesi.ecommerce.service.IWishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WishlistService implements IWishlistService {

    private final IWishlistRepository wishlistRepository;

    @Override
    public Wishlist createWishlist(User user) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        return wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist getWishlistByUserId(User user) {
        Wishlist wishlist = wishlistRepository.findByUserId(user.getId());
        if (wishlist == null) {
            wishlist = createWishlist(user);
        }
        return wishlist;
    }

    @Override
    public Wishlist addProductToWishlist(User user, Product product) {
        Wishlist wishlist = getWishlistByUserId(user);

        if (wishlist.getProducts().contains(product)) {
            wishlist.getProducts().remove(product);
        } else {
            wishlist.getProducts().add(product);
        }

        return wishlistRepository.save(wishlist);
    }
}
