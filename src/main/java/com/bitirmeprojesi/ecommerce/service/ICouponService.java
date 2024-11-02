package com.bitirmeprojesi.ecommerce.service;

import com.bitirmeprojesi.ecommerce.model.Cart;
import com.bitirmeprojesi.ecommerce.model.Coupon;
import com.bitirmeprojesi.ecommerce.model.User;

import java.util.List;

public interface ICouponService {

    Cart applyCoupon(String code, double orderValue, User user);
    Cart removeCoupon(String code, User user);
    Coupon createCoupon(Coupon coupon);
    Coupon getCouponById(Long id);
    List<Coupon> getAllCoupons();
    void deleteCoupon(Long couponId);

}
