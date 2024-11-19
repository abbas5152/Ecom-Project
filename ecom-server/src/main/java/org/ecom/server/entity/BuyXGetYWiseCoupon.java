package org.ecom.server.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "buy_x_get_y_wise_coupon")
public class BuyXGetYWiseCoupon extends Coupon {
   @OneToMany(cascade = CascadeType.ALL)
   @JoinTable(
         name = "buy_x_get_y_buy_products", // Join table for "buy" products
         joinColumns = @JoinColumn(name = "id"), // Foreign key to BuyXGetYWiseCoupon
         inverseJoinColumns = @JoinColumn(name = "product_id") // Foreign key to TransactionProduct
   )
   private List<TransactionProduct> buyProducts;

   @OneToMany(cascade = CascadeType.ALL)
   @JoinTable(
         name = "buy_x_get_y_get_products", // Join table for "buy" products
         joinColumns = @JoinColumn(name = "id"), // Foreign key to BuyXGetYWiseCoupon
         inverseJoinColumns = @JoinColumn(name = "product_id") // Foreign key to TransactionProduct
   )
   private List<TransactionProduct> getProducts;

   private int repetitionLimit;
}