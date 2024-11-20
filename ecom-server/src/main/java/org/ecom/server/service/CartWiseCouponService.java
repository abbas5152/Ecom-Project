package org.ecom.server.service;

import org.ecom.model.cart.CartDetails;
import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.coupon.CouponDTO;
import org.ecom.model.coupon.Details;
import org.ecom.server.entity.CartWiseCoupon;

public interface CartWiseCouponService {

   /**
    * Creates a POJO to create a cart_wise type of coupon
    * @param couponDTO Request which contains details related to coupon
    * @return CartWiseCoupon Entity Object related details
    */
   CartWiseCoupon createCartWiseCoupon(CouponDTO couponDTO);

   double calculateTotalCartValue(CartDetails cartDetails);

   /**
    * apply CartWiseCoupon based on card details and update the card details
    * @param coupon Request which contains details related to coupon
    * @param totalCartValue total cart value
    * @param updatedCartDetails Request which contains details related to cart(Products and quantity)
    * @return total discount after applying this coupon also update the cart details
    */
   double applyCartWiseCoupon(CouponDTO coupon, double totalCartValue,
         UpdatedCartDetails updatedCartDetails);

   /**
    * get details of CartWiseCoupon
    * @param couponId id of coupon
    * @param details with some information of coupon
    * @return add information in the details POJO and return the updated details
    */
   Details getCartWiseCouponDetails(Long couponId, Details details);
}
