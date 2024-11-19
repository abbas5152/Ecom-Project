package org.ecom.model.coupon;

import org.ecom.model.cart.CartDetails;

import lombok.Data;

@Data
public class ApplyCouponRequest {
   private CartDetails cart;
}
