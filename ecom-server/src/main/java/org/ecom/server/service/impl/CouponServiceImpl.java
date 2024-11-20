package org.ecom.server.service.impl;

import static org.ecom.model.common.ErrorCodes.COUPON_NOT_CREATED;
import static org.ecom.model.common.ErrorCodes.COUPON_NOT_DELETED;
import static org.ecom.model.common.ErrorCodes.COUPON_NOT_EXISTS;
import static org.ecom.model.common.ErrorCodes.INVALID_COUPON_TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ecom.model.cart.CartDetails;
import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.common.PageInfo;
import org.ecom.model.coupon.ApplicableCouponResponse;
import org.ecom.model.coupon.ApplyCouponRequest;
import org.ecom.model.coupon.CouponDTO;
import org.ecom.model.coupon.Coupons;
import org.ecom.model.coupon.Details;
import org.ecom.server.entity.Coupon;
import org.ecom.server.exception.InvalidRequestException;
import org.ecom.server.exception.ResourceNotFoundException;
import org.ecom.server.exception.UnknownException;
import org.ecom.server.repository.CouponRepository;
import org.ecom.server.service.BuyXGetYWiseCouponService;
import org.ecom.server.service.CartWiseCouponService;
import org.ecom.server.service.CouponService;
import org.ecom.server.service.ProductWiseCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

   @Autowired
   private CouponRepository couponRepository;

   @Autowired
   private CartWiseCouponService cartWiseCouponService;

   @Autowired
   private ProductWiseCouponService productWiseCouponService;

   @Autowired
   private BuyXGetYWiseCouponService buyXGetYWiseCouponService;

   @Override
   public Coupon createCoupon(CouponDTO couponDTO) {
      try {
         Coupon coupon;
         switch (couponDTO.getType()) {
         case CART_WISE:
            coupon = cartWiseCouponService.createCartWiseCoupon(couponDTO);
            break;
          case PRODUCT_WISE:
            coupon = productWiseCouponService.createProductWiseCoupon(couponDTO);
            break;
         case BXGY_WISE:
            coupon = buyXGetYWiseCouponService.createBuyXGetYWiseCoupon(couponDTO);
            break;
         default:
            throw new InvalidRequestException(INVALID_COUPON_TYPE, "Invalid coupon type");
         }
         return couponRepository.save(coupon);
      } catch (Exception ex) {
         log.error("Error while creating coupon: {}", ex.getMessage());
         throw new InvalidRequestException(COUPON_NOT_CREATED, "Cannot create coupon");
      }
   }

   @Override
   public Coupon updateCoupon(CouponDTO coupon){
      return null;
   }

   @Override
   public UpdatedCartDetails applyCoupon(long couponId, ApplyCouponRequest applyCouponRequest) {
      // Fetch coupon details by ID
      CouponDTO coupon = getCoupon(couponId);
      CartDetails cart = applyCouponRequest.getCart();
      double totalCartValue = cartWiseCouponService.calculateTotalCartValue(cart);

      UpdatedCartDetails updatedCartDetails = new UpdatedCartDetails();
      updatedCartDetails.setItems(cart.getItems());
      updatedCartDetails.setTotalPrice((int) totalCartValue);
      double totalDiscount = 0;

      // Apply coupon based on its type
      switch (coupon.getType()) {
      case CART_WISE:
         totalDiscount = cartWiseCouponService.applyCartWiseCoupon(coupon, totalCartValue, updatedCartDetails);
         break;
      case BXGY_WISE:
         totalDiscount = buyXGetYWiseCouponService.applyBxGyWiseCoupon(coupon, cart, updatedCartDetails);
         break;
      case PRODUCT_WISE:
         totalDiscount = productWiseCouponService.applyProductWiseCoupon(coupon, cart, updatedCartDetails);
         break;
      default:
         throw new InvalidRequestException(INVALID_COUPON_TYPE, "Invalid coupon type");
      }
      updatedCartDetails.setTotalDiscount((int) totalDiscount);
      updatedCartDetails.setFinalPrice((int) (totalCartValue - totalDiscount));
      return updatedCartDetails;
   }

   @Override
   public List<ApplicableCouponResponse> applyCoupons(CartDetails cart) {
      // Fetch active coupons
      List<CouponDTO> activeCoupons = getCoupons().getCoupons();
      double cartTotal = cartWiseCouponService.calculateTotalCartValue(cart);
      List<ApplicableCouponResponse> applicableCoupons = new ArrayList<>();

      for (CouponDTO coupon : activeCoupons) {
         double discount = 0;

         switch (coupon.getType()) {
         case CART_WISE:
            discount = cartWiseCouponService.applyCartWiseCoupon(coupon, cartTotal,null);
            break;
         case BXGY_WISE:
            discount = buyXGetYWiseCouponService.applyBxGyWiseCoupon(coupon, cart, null);
            break;

         case PRODUCT_WISE:
            discount = productWiseCouponService.applyProductWiseCoupon(coupon, cart, null);
            break;
         default:
            throw new InvalidRequestException(INVALID_COUPON_TYPE, "Invalid coupon type");
         }
         if (discount > 0) {
            applicableCoupons.add(new ApplicableCouponResponse(coupon.getId(), coupon.getType().toString(), discount));
         }
      }
      return applicableCoupons;
   }

   @Override
   public Coupons getCoupons() {
      List<Coupon> coupons = couponRepository.findByIsDeletedFalse();

      List<CouponDTO> couponDTOs = coupons.stream()
            .map(this::mapCouponEntityToRequest)
            .collect(Collectors.toList());

      // Handle pagination details - currently hardcoding the details
      int pageSize = 20;
      int totalRecords = couponDTOs.size();
      int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

      PageInfo pageInfo = new PageInfo();
      pageInfo.setPageNumber(0); // Default to the first page
      pageInfo.setPageSize(pageSize);
      pageInfo.setTotalPages(totalPages);

      Coupons response = new Coupons();
      response.setCoupons(couponDTOs);
      response.setPageInfo(pageInfo);
      return response;
   }


   @Override
   public CouponDTO getCoupon(Long id) {
      Optional<org.ecom.server.entity.Coupon> couponOptional = couponRepository.findByIdAndIsDeleted(id, false);
      if (couponOptional.isEmpty()) {
         throw new ResourceNotFoundException(COUPON_NOT_EXISTS, "Coupon not exists", id.toString());
      }
      return mapCouponEntityToRequest(couponOptional.get());
   }

   @Override
   public void deleteCoupon(Long id) {
      Optional<Coupon> coupon = couponRepository.findByIdAndIsDeleted(id, false);
      if(coupon.isEmpty()) {
         throw new ResourceNotFoundException(COUPON_NOT_EXISTS, "Coupon not exists", id.toString());
      }
      try {
         Coupon deleted = coupon.get();
         deleted.setIsDeleted(true);
         couponRepository.save(deleted);
      } catch (Exception ex) {
         log.error("Not able to delete coupon :{}", ex.getMessage());
         throw new UnknownException(COUPON_NOT_DELETED, "Coupon not deleted", id.toString());
      }
   }


   private CouponDTO mapCouponEntityToRequest(Coupon coupon) {
      CouponDTO couponDTO = new CouponDTO();
      couponDTO.setId(coupon.getId());
      couponDTO.setCreatedBy(coupon.getCreatedBy());
      couponDTO.setCreatedOn(coupon.getCreatedOn());
      couponDTO.setLastModifiedBy(coupon.getLastModifiedBy());
      couponDTO.setType(coupon.getCouponType());

      Details details = fetchCouponDetailsByType(coupon);
      couponDTO.setDetails(details);

      return couponDTO;
   }

   private Details fetchCouponDetailsByType(Coupon coupon) {
      Details details = new Details();

      switch (coupon.getCouponType()) {
      case CART_WISE:
          details = cartWiseCouponService.getCartWiseCouponDetails(coupon.getId(), details);
         break;

      case PRODUCT_WISE:
         details = productWiseCouponService.getProductWiseCouponDetails(coupon.getId(), details);
         break;

      case BXGY_WISE:
         details = buyXGetYWiseCouponService.getBuyXGetYCouponDetails(coupon.getId(), details);
         break;

      default:
         throw new InvalidRequestException(INVALID_COUPON_TYPE, "Invalid coupon type");
      }
      return details;
   }

}