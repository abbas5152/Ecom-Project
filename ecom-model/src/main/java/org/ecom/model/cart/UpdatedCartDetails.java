package org.ecom.model.cart;

import java.util.List;

import org.ecom.model.product.ProductWiseDetails;

import lombok.Data;

@Data
public class UpdatedCartDetails {
   private List<ProductWiseDetails> items;
   private int totalPrice;
   private int totalDiscount;
   private int finalPrice;
}
