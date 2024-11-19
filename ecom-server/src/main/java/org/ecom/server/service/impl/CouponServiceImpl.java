package org.ecom.server.service.impl;

import static org.ecom.model.common.ErrorCodes.COUPON_NOT_CREATED;
import static org.ecom.model.common.ErrorCodes.COUPON_NOT_EXISTS;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.coupon.ApplyCouponRequest;
import org.ecom.model.coupon.CouponRequest;
import org.ecom.model.coupon.Details;
import org.ecom.model.enums.CouponType;
import org.ecom.model.product.Products;
import org.ecom.server.entity.BuyXGetYWiseCoupon;
import org.ecom.server.entity.CartWiseCoupon;
import org.ecom.server.entity.Coupon;
import org.ecom.server.entity.ProductWiseCoupon;
import org.ecom.server.entity.TransactionProduct;
import org.ecom.server.exception.InvalidRequestException;
import org.ecom.server.exception.ResourceNotFoundException;
import org.ecom.server.repository.BuyXGetYWiseCouponRepository;
import org.ecom.server.repository.CartWiseCouponRepository;
import org.ecom.server.repository.CouponRepository;
import org.ecom.server.repository.ProductWiseCouponRepository;
import org.ecom.server.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CouponServiceImpl implements CouponService {

   @Autowired
   private CouponRepository couponRepository;

   @Autowired
   private CartWiseCouponRepository cartWiseCouponRepository;

   @Autowired
   private ProductWiseCouponRepository productWiseCouponRepository;

   @Autowired
   private BuyXGetYWiseCouponRepository buyXGetYWiseCouponRepository;


   private final ObjectMapper objectMapper = new ObjectMapper();

   public org.ecom.server.entity.Coupon createCoupon(CouponRequest couponRequest) throws JsonProcessingException {
      try {
         if(couponRequest.getType() == CouponType.CART_WISE) {
            CartWiseCoupon cartWiseCoupon = new CartWiseCoupon();
            cartWiseCoupon.setCouponType(CouponType.CART_WISE);
            cartWiseCoupon.setDiscount(couponRequest.getDetails().getDiscount());
            cartWiseCoupon.setThreshold(couponRequest.getDetails().getThreshold());
            return couponRepository.save(cartWiseCoupon);
         }
         else if(couponRequest.getType() == CouponType.PRODUCT_WISE) {
            ProductWiseCoupon productWiseCoupon = new ProductWiseCoupon();
            productWiseCoupon.setCouponType(CouponType.PRODUCT_WISE);
            productWiseCoupon.setDiscount(couponRequest.getDetails().getDiscount());
            return couponRepository.save(productWiseCoupon);
         }
         else if(couponRequest.getType() == CouponType.BXGY_WISE) {
            System.out.println(couponRequest.toString());
            BuyXGetYWiseCoupon buyXGetYWiseCoupon = new BuyXGetYWiseCoupon();
            buyXGetYWiseCoupon.setCouponType(CouponType.BXGY_WISE);
            // Map Buy Products
            List<TransactionProduct> buyProducts = couponRequest.getDetails().getBuyProducts().stream()
                  .map(transactionProduct -> {
                     TransactionProduct product = new TransactionProduct();
                     product.setProductId(transactionProduct.getProductId());
                     product.setQuantity(transactionProduct.getQuantity());
                     return product;
                  })
                  .collect(Collectors.toList());
            buyXGetYWiseCoupon.setBuyProducts(Set.copyOf(buyProducts));

            // Map Get Products
            List<TransactionProduct> getProducts = couponRequest.getDetails().getGetProducts().stream()
                  .map(transactionProduct -> {
                     TransactionProduct product = new TransactionProduct();
                     product.setProductId(transactionProduct.getProductId());
                     product.setQuantity(transactionProduct.getQuantity());
                     return product;
                  })
                  .collect(Collectors.toList());
            buyXGetYWiseCoupon.setGetProducts(getProducts);
            buyXGetYWiseCoupon.setRepetitionLimit(couponRequest.getDetails().getRepetitionLimit());
            System.out.println("dslfkjasdlfj" + buyXGetYWiseCoupon);
            return couponRepository.save(buyXGetYWiseCoupon);
         }

      } catch (Exception ex) {
         System.out.println("ex: " + ex.getMessage());
         ex.printStackTrace();
         throw new InvalidRequestException(COUPON_NOT_CREATED, "Cannot create coupon");
      }
      return null;
   }

   @Override
   public org.ecom.server.entity.Coupon updateCoupon(CouponRequest coupon) throws JsonProcessingException {

      return null;

   }

   @Override
   public UpdatedCartDetails applyCoupon(long couponId, ApplyCouponRequest applyCouponRequest) {
      // Get coupon by id
      CouponRequest coupon = getCoupon(couponId);

      // Check if the coupon is cart-wise
      if (coupon.getType() == CouponType.CART_WISE) {
         // Fetch cart-wise coupon details
         CartWiseCoupon cartWiseCoupon = cartWiseCouponRepository.findByIdAndIsDeleted(couponId, false)
               .orElseThrow(() -> new RuntimeException("Coupon not found or deleted"));

         double discountPercentage = cartWiseCoupon.getDiscount();
         double threshold = cartWiseCoupon.getThreshold();

         // Calculate total cart value
         double totalCartValue = applyCouponRequest.getCart().getItems().stream()
               .mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

         // Check if totalCartValue meets the coupon's threshold
         double finalDiscount = 0.0;
         double finalPrice = totalCartValue;
         if (totalCartValue >= threshold) {
            finalDiscount = totalCartValue * discountPercentage / 100; // Calculate discount
            finalPrice = totalCartValue - finalDiscount; // Final price after discount
         }

         // Populate UpdatedCartDetails object
         UpdatedCartDetails updatedCartDetails = new UpdatedCartDetails();
         updatedCartDetails.setItems(applyCouponRequest.getCart().getItems()); // Set the items
         updatedCartDetails.setTotalPrice((int) totalCartValue); // Total price (before discount)
         updatedCartDetails.setTotalDiscount((int) finalDiscount); // Total discount applied
         updatedCartDetails.setFinalPrice((int) finalPrice); // Final price (after discount)
         // return model map
         return updatedCartDetails;
      }
      return null;
   }

   @Override
   public List<CouponRequest> getCoupons() {
      List<org.ecom.server.entity.Coupon> coupons = couponRepository.findByIsDeletedFalse();

      return coupons.stream().map(coupon -> {
         CouponRequest couponRequest = new CouponRequest();
         couponRequest.setId(coupon.getId());
         couponRequest.setCreatedBy(coupon.getCreatedBy());
         couponRequest.setCreatedOn(coupon.getCreatedOn());
         couponRequest.setLastModifiedBy(coupon.getLastModifiedBy());
         couponRequest.setType(coupon.getCouponType());

         if (coupon.getCouponType() == CouponType.CART_WISE) {
            CartWiseCoupon cartWiseCoupon = cartWiseCouponRepository.findByIdAndIsDeleted(coupon.getId(), false).orElse(null);
            if (cartWiseCoupon != null) {
               Details details = new Details();
               details.setDiscount(cartWiseCoupon.getDiscount());
               details.setThreshold(cartWiseCoupon.getThreshold());
               couponRequest.setDetails(details);
            }
         }

         if (coupon.getCouponType() == CouponType.PRODUCT_WISE) {
            ProductWiseCoupon productWiseCoupon = productWiseCouponRepository.findByIdAndIsDeleted(coupon.getId(), false).orElse(null);
            if (productWiseCoupon != null) {
               Details details = new Details();
               details.setDiscount(productWiseCoupon.getDiscount());
               details.setThreshold(productWiseCoupon.getProductId());
               couponRequest.setDetails(details);
            }
         }

         if (coupon.getCouponType() == CouponType.BXGY_WISE) {
            BuyXGetYWiseCoupon buyXGetYWiseCoupon1 = buyXGetYWiseCouponRepository.findByIdAndIsDeleted(coupon.getId(), false).orElse(null);
            if (buyXGetYWiseCoupon1 != null) {
               Details details = new Details();
               List<Products> buyProducts = buyXGetYWiseCoupon1.getBuyProducts().stream()
                     .map(transactionProduct -> {
                        Products product = new Products();
                        product.setProductId(transactionProduct.getProductId());
                        product.setQuantity(transactionProduct.getQuantity());
                        return product;
                     })
                     .collect(Collectors.toList());
               details.setBuyProducts(buyProducts);

               // Map Get Products
               List<Products> getProducts = buyXGetYWiseCoupon1.getGetProducts().stream()
                     .map(transactionProduct -> {
                        Products product = new Products();
                        product.setProductId(transactionProduct.getProductId());
                        product.setQuantity(transactionProduct.getQuantity());
                        return product;
                     })
                     .collect(Collectors.toList());
               details.setGetProducts(getProducts);
               buyXGetYWiseCoupon1.setRepetitionLimit(buyXGetYWiseCoupon1.getRepetitionLimit());
               couponRequest.setDetails(details);
            }
         }

         return couponRequest;
      }).collect(Collectors.toList());
   }

   @Override
   public CouponRequest getCoupon(Long id) {
      Optional<org.ecom.server.entity.Coupon> coupon = couponRepository.findByIdAndIsDeleted(id, false);
      if(coupon.isEmpty()) {
       throw new ResourceNotFoundException(COUPON_NOT_EXISTS, "Coupon not exists", id.toString());
      }
      Coupon coupon1 = coupon.get();
      CouponRequest couponRequest = new CouponRequest();
      couponRequest.setId(coupon1.getId());
      couponRequest.setCreatedBy(coupon1.getCreatedBy());
      couponRequest.setCreatedOn(coupon1.getCreatedOn());
      couponRequest.setLastModifiedBy(coupon1.getLastModifiedBy());
      couponRequest.setType(coupon1.getCouponType());
     if(coupon.get().getCouponType() == CouponType.CART_WISE) {
        CartWiseCoupon cartWiseCoupon = cartWiseCouponRepository.findByIdAndIsDeleted(coupon1.getId(), false).get();
        Details details = new Details();
        details.setDiscount(cartWiseCoupon.getDiscount());
        details.setThreshold(cartWiseCoupon.getThreshold());
        couponRequest.setDetails(details);
     }
      if(coupon.get().getCouponType() == CouponType.PRODUCT_WISE) {
         ProductWiseCoupon productWiseCoupon = productWiseCouponRepository.findByIdAndIsDeleted(coupon1.getId(), false).get();
         Details details = new Details();
         details.setDiscount(productWiseCoupon.getDiscount());
         details.setThreshold(productWiseCoupon.getProductId());
         couponRequest.setDetails(details);
      }

      if (coupon.get().getCouponType() == CouponType.BXGY_WISE) {
         BuyXGetYWiseCoupon buyXGetYWiseCoupon1 = buyXGetYWiseCouponRepository.findByIdAndIsDeleted(coupon.get().getId(),
               false).orElse(null);
         if (buyXGetYWiseCoupon1 != null) {
            Details details = new Details();
            List<Products> buyProducts = buyXGetYWiseCoupon1.getBuyProducts().stream()
                  .map(transactionProduct -> {
                     Products product = new Products();
                     product.setProductId(transactionProduct.getProductId());
                     product.setQuantity(transactionProduct.getQuantity());
                     return product;
                  })
                  .collect(Collectors.toList());
            details.setBuyProducts(buyProducts);

            // Map Get Products
            List<Products> getProducts = buyXGetYWiseCoupon1.getGetProducts().stream()
                  .map(transactionProduct -> {
                     Products product = new Products();
                     product.setProductId(transactionProduct.getProductId());
                     product.setQuantity(transactionProduct.getQuantity());
                     return product;
                  })
                  .collect(Collectors.toList());
            details.setGetProducts(getProducts);
            buyXGetYWiseCoupon1.setRepetitionLimit(buyXGetYWiseCoupon1.getRepetitionLimit());
            couponRequest.setDetails(details);
         }
      }
      return couponRequest;
   }

   @Override
   public void deleteCoupon(Long id) {
      Optional<org.ecom.server.entity.Coupon> coupon = couponRepository.findByIdAndIsDeleted(id, false);
      if(coupon.isEmpty()) {
         throw new ResourceNotFoundException(COUPON_NOT_EXISTS, "Coupon not exists", id.toString());
      }
      org.ecom.server.entity.Coupon deleted = coupon.get();
      deleted.setIsDeleted(true);
      couponRepository.save(deleted);
   }
}
