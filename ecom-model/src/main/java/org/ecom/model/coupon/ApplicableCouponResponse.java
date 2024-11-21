package org.ecom.model.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Details of coupon which we can apply on particular cart
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicableCouponResponse {
   private Long couponId;
   private String type;
   private double discount;
}
