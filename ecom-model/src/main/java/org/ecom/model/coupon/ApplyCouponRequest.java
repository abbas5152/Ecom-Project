package org.ecom.model.coupon;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.ecom.model.cart.CartDetails;

import lombok.Data;

@Data
public class ApplyCouponRequest {

   private CartDetails cart;
}
