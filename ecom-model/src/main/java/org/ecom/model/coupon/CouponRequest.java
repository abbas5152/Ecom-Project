package org.ecom.model.coupon;

import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.ecom.model.enums.CouponType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CouponRequest {

   @NotNull(message = "Type is mandatory")
   private CouponType type;

   @NotNull(message = "details are mandatory")
   private Details details;

   private String createdBy;

   private Date createdOn;

   private String lastModifiedBy;

   private Date lastModifiedOn;
}
