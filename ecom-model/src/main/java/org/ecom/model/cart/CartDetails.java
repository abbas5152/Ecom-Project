package org.ecom.model.cart;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.ecom.model.product.ProductWiseDetails;

import lombok.Getter;
import lombok.Setter;

/**
 * Contains details of cart
 */

@Getter
@Setter
public class CartDetails {

   @NotNull
   private List<ProductWiseDetails> items;
}
