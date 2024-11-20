package org.ecom.server.service;

import org.ecom.model.cart.CartDetails;
import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.coupon.CouponDTO;
import org.ecom.model.coupon.Details;
import org.ecom.server.entity.BuyXGetYWiseCoupon;

public interface BuyXGetYWiseCouponService {

   BuyXGetYWiseCoupon createBuyXGetYWiseCoupon(CouponDTO couponDTO);

   double applyBxGyWiseCoupon(CouponDTO coupon, CartDetails cart, UpdatedCartDetails updatedCartDetails);

   Details getBuyXGetYCouponDetails(Long couponId, Details details);
}
