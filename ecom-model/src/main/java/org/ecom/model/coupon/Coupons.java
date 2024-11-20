package org.ecom.model.coupon;

import java.util.ArrayList;
import java.util.List;

import org.ecom.model.common.PageInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Coupons {

   private List<CouponDTO> coupons = new ArrayList<>();

   private PageInfo pageInfo;

}
