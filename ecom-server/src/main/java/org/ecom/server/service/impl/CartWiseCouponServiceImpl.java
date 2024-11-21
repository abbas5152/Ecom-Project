package org.ecom.server.service.impl;

import org.ecom.model.cart.CartDetails;
import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.coupon.CouponDTO;
import org.ecom.model.coupon.Details;
import org.ecom.model.enums.CouponType;
import org.ecom.server.entity.CartWiseCoupon;
import org.ecom.server.repository.CartWiseCouponRepository;
import org.ecom.server.service.CartWiseCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartWiseCouponServiceImpl implements CartWiseCouponService {

   @Autowired
   private CartWiseCouponRepository cartWiseCouponRepository;

   @Override
   public CartWiseCoupon createCartWiseCoupon(CouponDTO couponDTO) {
      CartWiseCoupon cartWiseCoupon = new CartWiseCoupon();
      cartWiseCoupon.setCouponType(CouponType.CART_WISE);
      cartWiseCoupon.setDiscount(couponDTO.getDetails().getDiscount());
      cartWiseCoupon.setThreshold(couponDTO.getDetails().getThreshold());
      cartWiseCoupon.setEndDate(couponDTO.getEndDate());
      return cartWiseCoupon;
   }

   @Override
   public double calculateTotalCartValue(CartDetails cart) {
      return cart.getItems().stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
   }

   @Override
   public double applyCartWiseCoupon(CouponDTO coupon, double totalCartValue,
         UpdatedCartDetails updatedCartDetails) {
      double discountPercentage = coupon.getDetails().getDiscount();
      double threshold = coupon.getDetails().getThreshold();

      if (totalCartValue >= threshold) {
         double finalDiscount = totalCartValue * discountPercentage / 100;

         if (updatedCartDetails != null) {
            updatedCartDetails.setTotalDiscount((int) finalDiscount);
            updatedCartDetails.setFinalPrice((int) (totalCartValue - finalDiscount));
         }
         return finalDiscount;
      }
      return 0;
   }

   @Override
   public Details getCartWiseCouponDetails(Long couponId, Details details) {
      CartWiseCoupon cartWiseCoupon = cartWiseCouponRepository
            .findByIdAndIsDeleted(couponId, false).orElse(null);
      details.setDiscount(cartWiseCoupon.getDiscount());
      details.setThreshold(cartWiseCoupon.getThreshold());
      return details;
   }
}
