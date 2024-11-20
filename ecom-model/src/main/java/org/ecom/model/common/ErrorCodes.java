package org.ecom.model.common;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ErrorCodes {

   public static ErrorCode COUPON_NOT_CREATED = new ErrorCode(1, "coupon.not.created");

   public static ErrorCode COUPON_NOT_EXISTS = new ErrorCode(2,"coupon.not.exists");

   public static ErrorCode INVALID_COUPON_TYPE = new ErrorCode(3, "invalid.coupon.type");

   public static ErrorCode COUPON_NOT_DELETED = new ErrorCode(4,"coupon.not.deleted");
}
