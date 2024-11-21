package org.ecom.server.service.impl;

import org.ecom.model.cart.CartDetails;
import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.coupon.CouponDTO;
import org.ecom.model.coupon.Details;
import org.ecom.model.enums.CouponType;
import org.ecom.model.product.ProductWiseDetails;
import org.ecom.server.entity.ProductWiseCoupon;
import org.ecom.server.repository.ProductWiseCouponRepository;
import org.ecom.server.service.ProductWiseCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductWiseCouponServiceImpl implements ProductWiseCouponService {

   @Autowired
   private ProductWiseCouponRepository productWiseCouponRepository;

   @Override
   public ProductWiseCoupon createProductWiseCoupon(CouponDTO couponDTO) {
      ProductWiseCoupon productWiseCoupon = new ProductWiseCoupon();
      productWiseCoupon.setCouponType(CouponType.PRODUCT_WISE);
      productWiseCoupon.setDiscount(couponDTO.getDetails().getDiscount());
      productWiseCoupon.setProductId(couponDTO.getDetails().getProductId());
      productWiseCoupon.setEndDate(couponDTO.getEndDate());
      return productWiseCoupon;
   }

   @Override
   public double applyProductWiseCoupon(CouponDTO coupon, CartDetails cart, UpdatedCartDetails updatedCartDetails) {
      double discount = calculateProductWiseDiscount(cart, coupon);

      if (updatedCartDetails != null) {
         updatedCartDetails.setItems(cart.getItems()); // Update cart items if `UpdatedCartDetails` is provided
      }

      return discount;
   }

   @Override
   public Details getProductWiseCouponDetails(Long couponId, Details details) {
      ProductWiseCoupon productWiseCoupon = productWiseCouponRepository
            .findByIdAndIsDeleted(couponId, false).orElse(null);
      details.setDiscount(productWiseCoupon.getDiscount());
      details.setProductId(productWiseCoupon.getProductId());
      return details;
   }

   private double calculateProductWiseDiscount(CartDetails cart, CouponDTO coupon) {
      double discountPercentage = coupon.getDetails().getDiscount();
      double totalDiscount = 0;

      for (ProductWiseDetails cartItem : cart.getItems()) {
         if (cartItem.getProductId() == coupon.getDetails().getProductId()) {
            double discount = (cartItem.getPrice() * cartItem.getQuantity() * discountPercentage / 100);

            cartItem.setTotalDiscount(cartItem.getTotalDiscount() + (int) discount);

            totalDiscount += discount;
         }
      }

      return totalDiscount;
   }
}
