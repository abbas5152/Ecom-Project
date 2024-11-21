package org.ecom.model.coupon;

import java.util.List;

import org.ecom.model.product.Products;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Contains information of all types of coupons
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Details {

   private Long productId;

   private double discount;

   private List<Products> buyProducts;

   private List<Products> getProducts;

   private int repetitionLimit;

   private double threshold;
}
