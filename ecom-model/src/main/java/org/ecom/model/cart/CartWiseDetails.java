package org.ecom.model.cart;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartWiseDetails {

   @NotBlank
   private double threshold;

   private double discount;
}
