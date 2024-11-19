package org.ecom.server.entity;

import javax.persistence.Entity;
import javax.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProductWiseCoupon extends Coupon {
   @Column(nullable = false)
   private long productId;

   @Column(nullable = false)
   private double discount;
}