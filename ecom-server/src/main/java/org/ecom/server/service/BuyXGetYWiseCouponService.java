package org.ecom.server.service;

import org.ecom.model.cart.CartDetails;
import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.coupon.CouponDTO;
import org.ecom.model.coupon.Details;
import org.ecom.server.entity.BuyXGetYWiseCoupon;

public interface BuyXGetYWiseCouponService {

   /**
    * Creates a POJO to create a BuyXGetY type of coupon
    * @param couponDTO Request which contains details related to coupon
    * @return BuyXGetYWiseCoupon Entity Object related details
    */
   BuyXGetYWiseCoupon createBuyXGetYWiseCoupon(CouponDTO couponDTO);

   /**
    * apply BxGyWiseCoupon based on card details and update the card details
    * @param coupon Request which contains details related to coupon
    * @param cart Request which contains details related to cart(Products and quantity)
    * @param updatedCartDetails Request which contains details related to cart(Products and quantity)
    * @return total discount after applying this coupon also update the cart details
    */
   double applyBxGyWiseCoupon(CouponDTO coupon, CartDetails cart, UpdatedCartDetails updatedCartDetails);

   /**
    * get details of BxGyWiseCoupon
    * @param couponId id of coupon
    * @param details with some information of coupon
    * @return add information in the details POJO and return the updated details
    */
   Details getBuyXGetYCouponDetails(Long couponId, Details details);
}
