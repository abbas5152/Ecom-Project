package org.ecom.model.bxGy;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.ecom.model.product.Products;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BxGyDetails {

   @NotEmpty
   private List<Products> buyProducts;

   @NotEmpty
   private List<Products> getProducts;

   private int repetitionLimit;


}
