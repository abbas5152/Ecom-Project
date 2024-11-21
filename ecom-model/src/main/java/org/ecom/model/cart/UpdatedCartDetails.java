package org.ecom.model.cart;

import java.util.List;

import org.ecom.model.product.ProductWiseDetails;

import lombok.Data;

/**
 * Contains details of cart after applying the particular coupon
 */
@Data
public class UpdatedCartDetails {
   private List<ProductWiseDetails> items;
   private int totalPrice;
   private int totalDiscount;
   private int finalPrice;
}
