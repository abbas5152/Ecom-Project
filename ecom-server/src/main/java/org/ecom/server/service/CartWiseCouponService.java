package org.ecom.server.service;

import org.ecom.model.cart.CartDetails;
import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.coupon.CouponDTO;
import org.ecom.model.coupon.Details;
import org.ecom.server.entity.CartWiseCoupon;

public interface CartWiseCouponService {

   CartWiseCoupon createCartWiseCoupon(CouponDTO couponDTO);

   double calculateTotalCartValue(CartDetails cartDetails);

   double applyCartWiseCoupon(CouponDTO coupon, double totalCartValue,
         UpdatedCartDetails updatedCartDetails);

   Details getCartWiseCouponDetails(Long couponId, Details details);
}
