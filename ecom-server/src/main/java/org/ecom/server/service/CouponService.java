package org.ecom.server.service;

import java.util.Date;
import java.util.List;

import org.ecom.model.cart.CartDetails;
import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.coupon.ApplicableCouponResponse;
import org.ecom.model.coupon.ApplyCouponRequest;
import org.ecom.model.coupon.CouponDTO;
import org.ecom.model.coupon.Coupons;
import org.ecom.server.entity.Coupon;

public interface CouponService {

   /**
    * Creates coupon
    * @param coupon Request which contains details related to coupon
    * @return Coupon Entity Object related details
    */
   Coupon createCoupon(CouponDTO coupon);

   /**
    * updates coupon ending time
    * @param id coupon id
    * @param endDate endDate which needs to be updated
    * @return Coupon Entity Object related details
    */
   Coupon updateCoupon(long id, Date endDate);

   /**
    * Apply particular coupon
    * @param applyCouponRequest Request which contains details related to products and quantity of products
    * @return UpdatedCartDetails after all the discounts
    */
   UpdatedCartDetails applyCoupon(long couponId, ApplyCouponRequest applyCouponRequest);

   /**
    * get All coupons which are available
    * @return Paginated response of list of coupons
    */
   Coupons getCoupons();

   /**
    * get particular coupon with coupon id
    * @param id coupon of id
    * @return Coupon details
    */
   CouponDTO getCoupon(Long id);

   /**
    * delete particular coupon with coupon id
    * @param id coupon of id
    */
   void deleteCoupon(Long id);

   /**
    * List of all Applicable coupons based on cart details
    * @param cartDetails Request which contains details related to products and quantity of products
    * @return ApplicableCoupons based on cart
    */
   List<ApplicableCouponResponse> applyCoupons(CartDetails cartDetails);

}
