package org.ecom.server.service;

import org.ecom.model.cart.CartDetails;
import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.coupon.CouponDTO;
import org.ecom.model.coupon.Details;
import org.ecom.server.entity.ProductWiseCoupon;

public interface ProductWiseCouponService {

   /**
    * Creates a POJO to create a product_wise type of coupon
    * @param couponDTO Request which contains details related to coupon
    * @return ProductWiseCoupon Entity Object related details
    */
   ProductWiseCoupon createProductWiseCoupon(CouponDTO couponDTO);

   /**
    * apply ProductWiseCoupon based on card details and update the card details
    * @param coupon Request which contains details related to coupon
    * @param cart Request which contains details related to cart(Products and quantity)
    * @param updatedCartDetails Request which contains details related to cart(Products and quantity)
    * @return total discount after applying this coupon also update the cart details
    */
   double applyProductWiseCoupon(CouponDTO coupon, CartDetails cart, UpdatedCartDetails updatedCartDetails);

   /**
    * get details of ProductWiseCoupon
    * @param couponId id of coupon
    * @param details with some information of coupon
    * @return add information in the details POJO and return the updated details
    */
   Details getProductWiseCouponDetails(Long couponId, Details details);
}
