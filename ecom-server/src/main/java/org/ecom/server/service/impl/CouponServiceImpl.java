package org.ecom.server.service.impl;

import static org.ecom.model.common.ErrorCodes.COUPON_NOT_CREATED;
import static org.ecom.model.common.ErrorCodes.COUPON_NOT_EXISTS;

import java.util.List;
import java.util.Optional;

import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.coupon.ApplyCouponRequest;
import org.ecom.model.coupon.CouponRequest;
import org.ecom.model.enums.CouponType;
import org.ecom.server.entity.CartWiseCoupon;
import org.ecom.server.entity.Coupon;
import org.ecom.server.exception.InvalidRequestException;
import org.ecom.server.exception.ResourceNotFoundException;
import org.ecom.server.repository.CartWiseCouponRepository;
import org.ecom.server.repository.CouponRepository;
import org.ecom.server.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CouponServiceImpl implements CouponService {

   @Autowired
   private CouponRepository couponRepository;

   @Autowired
   private CartWiseCouponRepository cartWiseCouponRepository;

   private final ObjectMapper objectMapper = new ObjectMapper();

   public org.ecom.server.entity.Coupon createCoupon(CouponRequest couponRequest) throws JsonProcessingException {
      try {
         if(couponRequest.getType() == CouponType.CART_WISE) {
            CartWiseCoupon cartWiseCoupon = new CartWiseCoupon();
            cartWiseCoupon.setCouponType(CouponType.CART_WISE);
            cartWiseCoupon.setDiscount(couponRequest.getDetails().getDiscount());
            cartWiseCoupon.setThreshold(couponRequest.getDetails().getThreshold());
            return couponRepository.save(cartWiseCoupon);
         }

      } catch (Exception ex) {
         throw new InvalidRequestException(COUPON_NOT_CREATED, "Cannot create coupon");
      }
      return null;
   }

   @Override
   public org.ecom.server.entity.Coupon updateCoupon(CouponRequest coupon) throws JsonProcessingException {

      return null;

   }

   @Override
   public UpdatedCartDetails applyCoupon(long couponId, ApplyCouponRequest applyCouponRequest) {
      // Get coupon by id
      Coupon coupon = getCoupon(couponId);

      // Check if the coupon is cart-wise
      if (coupon.getCouponType() == CouponType.CART_WISE) {
         // Fetch cart-wise coupon details
         CartWiseCoupon cartWiseCoupon = cartWiseCouponRepository.findByIdAndIsDeleted(couponId, false)
               .orElseThrow(() -> new RuntimeException("Coupon not found or deleted"));

         double discountPercentage = cartWiseCoupon.getDiscount();
         double threshold = cartWiseCoupon.getThreshold();

         // Calculate total cart value
         double totalCartValue = applyCouponRequest.getCart().getItems().stream()
               .mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

         // Check if totalCartValue meets the coupon's threshold
         double finalDiscount = 0.0;
         double finalPrice = totalCartValue;
         if (totalCartValue >= threshold) {
            finalDiscount = totalCartValue * discountPercentage / 100; // Calculate discount
            finalPrice = totalCartValue - finalDiscount; // Final price after discount
         }

         // Populate UpdatedCartDetails object
         UpdatedCartDetails updatedCartDetails = new UpdatedCartDetails();
         updatedCartDetails.setItems(applyCouponRequest.getCart().getItems()); // Set the items
         updatedCartDetails.setTotalPrice((int) totalCartValue); // Total price (before discount)
         updatedCartDetails.setTotalDiscount((int) finalDiscount); // Total discount applied
         updatedCartDetails.setFinalPrice((int) finalPrice); // Final price (after discount)
         // return model map
         return updatedCartDetails;
      }
      return null;
   }

   @Override
   public List<org.ecom.server.entity.Coupon> getCoupons() {
//      return couponRepository.findByIsDeletedFalse();
      return null;
   }

   @Override
   public org.ecom.server.entity.Coupon getCoupon(Long id) {
      Optional<org.ecom.server.entity.Coupon> coupon = couponRepository.findByIdAndIsDeleted(id, false);
      if(coupon.isEmpty()) {
       throw new ResourceNotFoundException(COUPON_NOT_EXISTS, "Coupon not exists", id.toString());
      }
     return coupon.get();
   }

   @Override
   public void deleteCoupon(Long id) {
      Optional<org.ecom.server.entity.Coupon> coupon = couponRepository.findByIdAndIsDeleted(id, false);
      if(coupon.isEmpty()) {
         throw new ResourceNotFoundException(COUPON_NOT_EXISTS, "Coupon not exists", id.toString());
      }
      org.ecom.server.entity.Coupon deleted = coupon.get();
      deleted.setIsDeleted(true);
      couponRepository.save(deleted);
   }
}
