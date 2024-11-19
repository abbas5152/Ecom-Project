package org.ecom.server.service;

import java.util.List;

import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.coupon.ApplyCouponRequest;
import org.ecom.server.entity.Coupon;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface CouponService {

   Coupon createCoupon(org.ecom.model.coupon.CouponRequest coupon) throws JsonProcessingException;

   Coupon updateCoupon(org.ecom.model.coupon.CouponRequest coupon) throws JsonProcessingException;

   UpdatedCartDetails applyCoupon(long couponId, ApplyCouponRequest applyCouponRequest);

   List<Coupon> getCoupons();

   Coupon getCoupon(Long id);

   void deleteCoupon(Long id);

}
