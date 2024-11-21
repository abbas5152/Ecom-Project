package org.ecom.model.product;

import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * contains details of product after applying coupon
 */

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductWiseDetails {

   @NotNull
   private Long productId;

   @NotNull
   private int quantity;

   private int price;

   private int totalDiscount;

}
