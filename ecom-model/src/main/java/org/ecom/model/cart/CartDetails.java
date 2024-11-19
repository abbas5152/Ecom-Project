package org.ecom.model.cart;

import java.util.List;

import org.ecom.model.product.ProductWiseDetails;

import lombok.Data;

@Data
public class CartDetails {
   private List<ProductWiseDetails> items;
}
