package org.ecom.model.product;

import javax.validation.constraints.NotNull;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductWiseDetails {

   private Long productId;

   private double discount;

   private int quantity;

   private int price;

   private int totalDiscount;

}
