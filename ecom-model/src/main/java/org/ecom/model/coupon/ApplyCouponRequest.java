package org.ecom.model.coupon;

import org.ecom.model.cart.CartDetails;

import lombok.Getter;
import lombok.Setter;

/**
 * Contains information on cart
 */
@Getter
@Setter
public class ApplyCouponRequest {

   private CartDetails cart;
}
