package org.ecom.model.cart;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.ecom.model.product.ProductWiseDetails;

import lombok.Data;

@Data
public class CartDetails {
   @NotNull
   private List<ProductWiseDetails> items;
}
