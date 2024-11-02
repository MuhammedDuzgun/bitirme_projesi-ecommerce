package com.bitirmeprojesi.ecommerce.service.impl;

import com.bitirmeprojesi.ecommerce.model.Cart;
import com.bitirmeprojesi.ecommerce.model.Coupon;
import com.bitirmeprojesi.ecommerce.model.User;
import com.bitirmeprojesi.ecommerce.repository.ICartRepository;
import com.bitirmeprojesi.ecommerce.repository.ICouponRepository;
import com.bitirmeprojesi.ecommerce.repository.IUserRepository;
import com.bitirmeprojesi.ecommerce.service.ICouponService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CouponService implements ICouponService {

    private final ICouponRepository couponRepository;
    private final ICartRepository cartRepository;
    private final IUserRepository userRepository;

    @Override
    public Cart applyCoupon(String code, double orderValue, User user) {
        Coupon coupon = couponRepository.findByCode(code);
        Cart cart = cartRepository.findByUserId(user.getId());

        if (coupon == null) {
            throw new RuntimeException("Coupon not found");
        }
        if (user.getUsedCoupons().contains(coupon)) {
            throw new RuntimeException("Coupon is used");
        }
        if (orderValue < coupon.getMinimumOrderValue()) {
            throw new RuntimeException("Order value is less than minimum order value");
        }
        if (coupon.isActive()
                && LocalDate.now().isAfter(coupon.getValidityStartDate())
                && LocalDate.now().isBefore(coupon.getValidityEndDate())
        ) {
            user.getUsedCoupons().add(coupon);
            userRepository.save(user);

            double discountedPrice = (cart.getTotalSellingPrice()*coupon.getDiscountPercentage())/100;
            cart.setTotalSellingPrice(cart.getTotalSellingPrice()-discountedPrice);

            cart.setCouponCode(code);
            cartRepository.save(cart);
            return cart;
        }
        throw new RuntimeException("Coupon is not valid");
    }

    @Override
    public Cart removeCoupon(String code, User user) {
        Coupon coupon = couponRepository.findByCode(code);
        if (coupon == null) {
            throw new RuntimeException("Coupon not found");
        }
        Cart cart = cartRepository.findByUserId(user.getId());

        double discountedPrice = (cart.getTotalSellingPrice()*coupon.getDiscountPercentage())/100;
        cart.setTotalSellingPrice(cart.getTotalSellingPrice()+discountedPrice);

        cart.setCouponCode(null);

        return cartRepository.save(cart);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public Coupon getCouponById(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCoupon(Long couponId) {
        getCouponById(couponId);
        couponRepository.deleteById(couponId);
    }

}
