package org.ecom.server.schdeuler;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.ecom.model.coupon.CouponDTO;
import org.ecom.server.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Validate whether coupon is expired or not in every 5 mins
 */
@Slf4j
@Component
public class CouponExpiry {

   @Autowired
   private CouponService couponService;

   @Scheduled(fixedRateString = "180000000")
   public void checkCouponExpired() {
      System.out.println("came here?");
      List<CouponDTO> coupons = couponService.getCoupons().getCoupons();
      System.out.println(coupons.size());
      coupons.forEach(couponDTO -> {
         if (couponDTO.getEndDate() != null && couponDTO.getEndDate().before(Timestamp.valueOf(LocalDateTime.now(
               ZoneOffset.UTC)))) {
            couponService.deleteCoupon(couponDTO.getId());
         }
      });
   }
}
