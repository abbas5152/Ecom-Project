package org.ecom.server.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import javax.validation.Valid;

import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.coupon.ApplicableCouponResponse;
import org.ecom.model.coupon.ApplyCouponRequest;
import org.ecom.model.coupon.CouponDTO;
import org.ecom.model.coupon.Coupons;
import org.ecom.server.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/coupon")
public class CouponController {
   @Autowired
   private CouponService couponService;

   @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.CREATED)
   public org.ecom.server.entity.Coupon createCoupon(@Valid @RequestBody CouponDTO coupon) {
      return couponService.createCoupon(coupon);
   }

   @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.CREATED)
   public org.ecom.server.entity.Coupon updateCoupon(@Valid @RequestBody CouponDTO coupon)  {
      return couponService.updateCoupon(coupon);
   }

   @PostMapping(value = "/apply-coupon", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.OK)
   public List<ApplicableCouponResponse> applyCoupons(@Valid @RequestBody ApplyCouponRequest cart) {
      return couponService.applyCoupons(cart.getCart());
   }

   @PostMapping(value = "/apply-coupon/{id}",consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.OK)
   public UpdatedCartDetails applyCoupon(@PathVariable long id,
         @RequestBody @Valid ApplyCouponRequest applyCouponRequest){
      return couponService.applyCoupon(id, applyCouponRequest);
   }

   @GetMapping(produces = APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.OK)
   public Coupons getCoupons() {
      return couponService.getCoupons();
   }

   @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.OK)
   public CouponDTO getCoupon(@PathVariable Long id) {
      return couponService.getCoupon(id);
   }

   @DeleteMapping(value = "/{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void deleteCoupon(@PathVariable Long id) {
      couponService.deleteCoupon(id);
   }
}

