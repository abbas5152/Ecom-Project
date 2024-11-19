package org.ecom.server.entity;

import javax.persistence.Entity;
import javax.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CartWiseCoupon extends Coupon {
   @Column(nullable = false)
   private double threshold;

   @Column(nullable = false)
   private double discount;
}