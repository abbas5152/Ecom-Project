package org.ecom.server.service;

import java.util.List;

import org.ecom.model.cart.CartDetails;
import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.coupon.ApplicableCouponResponse;
import org.ecom.model.coupon.ApplyCouponRequest;
import org.ecom.model.coupon.CouponDTO;
import org.ecom.model.coupon.Coupons;
import org.ecom.server.entity.Coupon;

public interface CouponService {

   Coupon createCoupon(CouponDTO coupon);

   Coupon updateCoupon(CouponDTO coupon);

   UpdatedCartDetails applyCoupon(long couponId, ApplyCouponRequest applyCouponRequest);

   Coupons getCoupons();

   CouponDTO getCoupon(Long id);

   void deleteCoupon(Long id);

   List<ApplicableCouponResponse> applyCoupons(CartDetails cartDetails);

}
