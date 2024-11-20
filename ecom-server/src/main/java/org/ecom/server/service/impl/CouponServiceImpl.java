package org.ecom.server.service.impl;

import static org.ecom.model.common.ErrorCodes.COUPON_NOT_CREATED;
import static org.ecom.model.common.ErrorCodes.COUPON_NOT_EXISTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.ecom.model.cart.CartDetails;
import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.coupon.ApplicableCouponResponse;
import org.ecom.model.coupon.ApplyCouponRequest;
import org.ecom.model.coupon.CouponRequest;
import org.ecom.model.coupon.Details;
import org.ecom.model.enums.CouponType;
import org.ecom.model.product.ProductWiseDetails;
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
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

   @Override
   public Coupon createCoupon(CouponRequest couponRequest) {
      try {
         // Validate couponRequest before proceeding
         if (couponRequest == null || couponRequest.getType() == null || couponRequest.getDetails() == null) {
            throw new InvalidRequestException(COUPON_NOT_CREATED, "Invalid coupon request: Missing required fields");
         }
         Coupon coupon;
         switch (couponRequest.getType()) {
         case CART_WISE:
            coupon = createCartWiseCoupon(couponRequest);
            break;

          case PRODUCT_WISE:
            coupon = createProductWiseCoupon(couponRequest);
            break;

         case BXGY_WISE:
            coupon = createBuyXGetYWiseCoupon(couponRequest);
            break;

         default:
            throw new InvalidRequestException(COUPON_NOT_CREATED, "Invalid coupon type");
         }
         return couponRepository.save(coupon);
      } catch (Exception ex) {
         log.error("Error while creating coupon: " + ex.getMessage(), ex);
         throw new InvalidRequestException(COUPON_NOT_CREATED, "Cannot create coupon");
      }
   }

   @Override
   public Coupon updateCoupon(CouponRequest coupon){
      return null;
   }

   @Override
   public UpdatedCartDetails applyCoupon(long couponId, ApplyCouponRequest applyCouponRequest) {
      // Fetch coupon details by ID
      CouponRequest coupon = getCoupon(couponId);
      CartDetails cart = applyCouponRequest.getCart();
      double totalCartValue = calculateTotalCartValue(cart);

      UpdatedCartDetails updatedCartDetails = new UpdatedCartDetails();
      updatedCartDetails.setItems(cart.getItems());
      updatedCartDetails.setTotalPrice((int) totalCartValue);
      double totalDiscount = 0;

      // Apply coupon based on its type
      switch (coupon.getType()) {
      case CART_WISE:
         totalDiscount = applyCartWiseCoupon(coupon, totalCartValue, updatedCartDetails);
         break;

      case BXGY_WISE:
         totalDiscount = applyBxGyWiseCoupon(coupon, cart, updatedCartDetails);
         break;

      case PRODUCT_WISE:
         totalDiscount = applyProductWiseCoupon(coupon, cart, updatedCartDetails);
         break;

      default:
         throw new InvalidRequestException(COUPON_NOT_EXISTS, "Unsupported coupon type");
      }

      updatedCartDetails.setTotalDiscount((int) totalDiscount);
      updatedCartDetails.setFinalPrice((int) (totalCartValue - totalDiscount));
      return updatedCartDetails;
   }

   @Override
   public List<ApplicableCouponResponse> applyCoupons(CartDetails cart) {
      // Fetch active coupons
      List<CouponRequest> activeCoupons = getCoupons();
      double cartTotal = calculateTotalCartValue(cart);
      List<ApplicableCouponResponse> applicableCoupons = new ArrayList<>();

      for (CouponRequest coupon : activeCoupons) {
         double discount = 0;

         // Applying different coupon types with a common method
         switch (coupon.getType()) {
         case CART_WISE:
            discount = applyCartWiseCoupon(coupon, cartTotal,null);
            break;

         case BXGY_WISE:
            discount = applyBxGyWiseCoupon(coupon, cart, null);
            break;

         case PRODUCT_WISE:
            discount = applyProductWiseCoupon(coupon, cart, null);
            break;

         default:
            // Handle unknown coupon types (optional logging or exception)
            break;
         }

         if (discount > 0) {
            applicableCoupons.add(new ApplicableCouponResponse(coupon.getId(), coupon.getType().toString(), discount));
         }
      }

      return applicableCoupons;
   }

   @Override
   public List<CouponRequest> getCoupons() {
      List<org.ecom.server.entity.Coupon> coupons = couponRepository.findByIsDeletedFalse();

      return coupons.stream()
            .map(this::mapCouponEntityToRequest)
            .collect(Collectors.toList());
   }

   @Override
   public CouponRequest getCoupon(Long id) {
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
      Coupon deleted = coupon.get();
      deleted.setIsDeleted(true);
      couponRepository.save(deleted);
   }

   // Helper method for creating CartWiseCoupon
   private CartWiseCoupon createCartWiseCoupon(CouponRequest couponRequest) {
      CartWiseCoupon cartWiseCoupon = new CartWiseCoupon();
      cartWiseCoupon.setCouponType(CouponType.CART_WISE);
      cartWiseCoupon.setDiscount(couponRequest.getDetails().getDiscount());
      cartWiseCoupon.setThreshold(couponRequest.getDetails().getThreshold());
      return cartWiseCoupon;
   }

   // Helper method for creating ProductWiseCoupon
   private ProductWiseCoupon createProductWiseCoupon(CouponRequest couponRequest) {
      ProductWiseCoupon productWiseCoupon = new ProductWiseCoupon();
      productWiseCoupon.setCouponType(CouponType.PRODUCT_WISE);
      productWiseCoupon.setDiscount(couponRequest.getDetails().getDiscount());
      productWiseCoupon.setProductId(couponRequest.getDetails().getProductId());
      return productWiseCoupon;
   }

   // Helper method for creating BuyXGetYWiseCoupon
   private BuyXGetYWiseCoupon createBuyXGetYWiseCoupon(CouponRequest couponRequest) {
      BuyXGetYWiseCoupon buyXGetYWiseCoupon = new BuyXGetYWiseCoupon();
      buyXGetYWiseCoupon.setCouponType(CouponType.BXGY_WISE);

      // Map Buy Products
      Set<TransactionProduct> buyProducts = mapTransactionProducts(couponRequest.getDetails().getBuyProducts());
      buyXGetYWiseCoupon.setBuyProducts(buyProducts);

      // Map Get Products
      Set<TransactionProduct> getProducts = mapTransactionProducts(couponRequest.getDetails().getGetProducts());
      buyXGetYWiseCoupon.setGetProducts(getProducts);

      buyXGetYWiseCoupon.setRepetitionLimit(couponRequest.getDetails().getRepetitionLimit());
      return buyXGetYWiseCoupon;
   }

   // Helper method to map transaction products
   private Set<TransactionProduct> mapTransactionProducts(List<Products> productList) {
      return productList.stream()
            .map(product -> {
               TransactionProduct transactionProduct = new TransactionProduct();
               transactionProduct.setProductId(product.getProductId());
               transactionProduct.setQuantity(product.getQuantity());
               return transactionProduct;
            })
            .collect(Collectors.toSet());
   }

   private double applyCartWiseCoupon(CouponRequest coupon, double totalCartValue, UpdatedCartDetails updatedCartDetails) {
      double discountPercentage = coupon.getDetails().getDiscount();
      double threshold = coupon.getDetails().getThreshold();

      // Check if the total cart value meets the threshold
      if (totalCartValue >= threshold) {
         double finalDiscount = totalCartValue * discountPercentage / 100;

         // Update the UpdatedCartDetails if provided
         if (updatedCartDetails != null) {
            updatedCartDetails.setTotalDiscount((int) finalDiscount);
            updatedCartDetails.setFinalPrice((int) (totalCartValue - finalDiscount));
         }

         return finalDiscount;
      }
      return 0; // No discount if the cart value is below the threshold
   }

   private double applyBxGyWiseCoupon(CouponRequest coupon, CartDetails cart, UpdatedCartDetails updatedCartDetails) {
      int maxRepetitions = getMaxRepetitionsForBuyXGetY(cart, coupon);

      // Respect repetition limit
      int repetitionLimit = coupon.getDetails().getRepetitionLimit() == 0 ? Integer.MAX_VALUE : coupon.getDetails().getRepetitionLimit();
      maxRepetitions = Math.min(maxRepetitions, repetitionLimit);

      double discount = calculateBuyXGetYDiscount(cart, coupon, maxRepetitions);

      // If UpdatedCartDetails is provided, update it
      if (updatedCartDetails != null) {
         updatedCartDetails.setItems(cart.getItems());
      }

      return discount;
   }

   private int getMaxRepetitionsForBuyXGetY(CartDetails cart, CouponRequest coupon) {
      int maxRepetitions = Integer.MAX_VALUE;

      // Validate Buy Products
      for (Products buyProduct : coupon.getDetails().getBuyProducts()) {
         ProductWiseDetails cartItem = cart.getItems().stream()
               .filter(item -> item.getProductId() == buyProduct.getProductId())
               .findFirst()
               .orElse(null);

         if (cartItem == null || cartItem.getQuantity() < buyProduct.getQuantity()) {
            return 0; // Not eligible
         }

         maxRepetitions = Math.min(maxRepetitions, cartItem.getQuantity() / buyProduct.getQuantity());
      }

      return maxRepetitions;
   }

   private double calculateBuyXGetYDiscount(CartDetails cart, CouponRequest coupon, int maxRepetitions) {
      double discount = 0;

      // Apply Get Products
      for (Products freeProduct : coupon.getDetails().getGetProducts()) {
         ProductWiseDetails cartItem = cart.getItems().stream()
               .filter(item -> item.getProductId() == freeProduct.getProductId())
               .findFirst()
               .orElse(null);

         if (cartItem != null) {
            int freeItems = Math.min(maxRepetitions * freeProduct.getQuantity(), cartItem.getQuantity());
            double productDiscount = freeItems * cartItem.getPrice();

            // Update the item's totalDiscount
            cartItem.setTotalDiscount(cartItem.getTotalDiscount() + (int) productDiscount);
            discount += productDiscount;

            // Optional: Add the free items to the cart quantity
            cartItem.setQuantity(cartItem.getQuantity() + freeItems);
         }
      }

      return discount;
   }


   private double applyProductWiseCoupon(CouponRequest coupon, CartDetails cart, UpdatedCartDetails updatedCartDetails) {
      double discount = calculateProductWiseDiscount(cart, coupon);

      if (updatedCartDetails != null) {
         updatedCartDetails.setItems(cart.getItems()); // Update cart items if `UpdatedCartDetails` is provided
      }

      return discount;
   }

   private double calculateProductWiseDiscount(CartDetails cart, CouponRequest coupon) {
      double discountPercentage = coupon.getDetails().getDiscount();
      double totalDiscount = 0;

      // Iterate through cart items to calculate the discount
      for (ProductWiseDetails cartItem : cart.getItems()) {
         if (cartItem.getProductId() == coupon.getDetails().getProductId()) {
            double discount = (cartItem.getPrice() * cartItem.getQuantity() * discountPercentage / 100);

            // Update the item's specific discount
            cartItem.setTotalDiscount(cartItem.getTotalDiscount() + (int) discount);

            totalDiscount += discount;
         }
      }

      return totalDiscount;
   }


   private double calculateTotalCartValue(CartDetails cart) {
      return cart.getItems().stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
   }

   private CouponRequest mapCouponEntityToRequest(org.ecom.server.entity.Coupon coupon) {
      CouponRequest couponRequest = new CouponRequest();
      couponRequest.setId(coupon.getId());
      couponRequest.setCreatedBy(coupon.getCreatedBy());
      couponRequest.setCreatedOn(coupon.getCreatedOn());
      couponRequest.setLastModifiedBy(coupon.getLastModifiedBy());
      couponRequest.setType(coupon.getCouponType());

      Details details = fetchCouponDetailsByType(coupon);
      if (details != null) {
         couponRequest.setDetails(details);
      }

      return couponRequest;
   }

   private Details fetchCouponDetailsByType(org.ecom.server.entity.Coupon coupon) {
      Details details = new Details();

      switch (coupon.getCouponType()) {
      case CART_WISE:
         CartWiseCoupon cartWiseCoupon = cartWiseCouponRepository.findByIdAndIsDeleted(coupon.getId(), false).orElse(null);
         if (cartWiseCoupon != null) {
            details.setDiscount(cartWiseCoupon.getDiscount());
            details.setThreshold(cartWiseCoupon.getThreshold());
         }
         break;

      case PRODUCT_WISE:
         ProductWiseCoupon productWiseCoupon = productWiseCouponRepository.findByIdAndIsDeleted(coupon.getId(), false).orElse(null);
         if (productWiseCoupon != null) {
            details.setDiscount(productWiseCoupon.getDiscount());
            details.setProductId(productWiseCoupon.getProductId());
         }
         break;

      case BXGY_WISE:
         BuyXGetYWiseCoupon bxgyCoupon = buyXGetYWiseCouponRepository.findByIdAndIsDeleted(coupon.getId(), false).orElse(null);
         if (bxgyCoupon != null) {
            List<Products> buyProducts = bxgyCoupon.getBuyProducts().stream()
                  .map(product -> {
                     Products buyProduct = new Products();
                     buyProduct.setProductId(product.getProductId());
                     buyProduct.setQuantity(product.getQuantity());
                     return buyProduct;
                  })
                  .collect(Collectors.toList());
            details.setBuyProducts(buyProducts);

            List<Products> getProducts = bxgyCoupon.getGetProducts().stream()
                  .map(product -> {
                     Products getProduct = new Products();
                     getProduct.setProductId(product.getProductId());
                     getProduct.setQuantity(product.getQuantity());
                     return getProduct;
                  })
                  .collect(Collectors.toList());
            details.setGetProducts(getProducts);
            details.setRepetitionLimit(bxgyCoupon.getRepetitionLimit());
         }
         break;

      default:
         // Optional: Handle unknown coupon types (logging or exceptions)
         break;
      }
      return details;
   }

}