package org.ecom.server.service;

import org.ecom.model.cart.CartDetails;
import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.coupon.CouponDTO;
import org.ecom.model.coupon.Details;
import org.ecom.server.entity.ProductWiseCoupon;

public interface ProductWiseCouponService {

   ProductWiseCoupon createProductWiseCoupon(CouponDTO couponDTO);

   double applyProductWiseCoupon(CouponDTO coupon, CartDetails cart, UpdatedCartDetails updatedCartDetails);

   Details getProductWiseCouponDetails(Long couponId, Details details);
}
