package org.ecom.model.product;

import javax.validation.constraints.NotEmpty;

import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductWiseDetails {

   @NotNull
   private Long productId;

   @NotNull
   private int quantity;

   private int price;

   private int totalDiscount;

}
