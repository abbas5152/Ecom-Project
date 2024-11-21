package org.ecom.model.coupon;

import java.util.ArrayList;
import java.util.List;

import org.ecom.model.common.PageInfo;

import lombok.Getter;
import lombok.Setter;

/**
 * Paginated response of list of coupons
 */
@Getter
@Setter
public class Coupons {

   private List<CouponDTO> coupons = new ArrayList<>();

   private PageInfo pageInfo;

}
