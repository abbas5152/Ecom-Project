package org.ecom.server.entity;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "buy_x_get_y_wise_coupon")
@ToString
public class BuyXGetYWiseCoupon extends Coupon {
   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   @JoinTable(
         name = "buy_x_get_y_buy_products", // create a new table and add a entry there
         joinColumns = @JoinColumn(name = "id"), // coupon-id
         inverseJoinColumns = @JoinColumn(name = "product_id") //product-id

   )
   private Set<TransactionProduct> buyProducts;

   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   @JoinTable(
         name = "buy_x_get_y_get_products", // create a new table and add a entry there
         joinColumns = @JoinColumn(name = "id"), // coupon-id
         inverseJoinColumns = @JoinColumn(name = "product_id") //product-id
   )
   private Set<TransactionProduct> getProducts;

   private int repetitionLimit;
}
