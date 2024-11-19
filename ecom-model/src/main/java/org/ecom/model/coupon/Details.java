package org.ecom.model.coupon;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.ecom.model.product.Products;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Details {

   private Long productId;

   private double discount;


   private List<Products> buyProducts;


   private List<Products> getProducts;

   private int repetitionLimit;


   private double threshold;
}