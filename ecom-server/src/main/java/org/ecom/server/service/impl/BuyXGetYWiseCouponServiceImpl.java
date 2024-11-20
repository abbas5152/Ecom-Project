package org.ecom.server.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.ecom.model.cart.CartDetails;
import org.ecom.model.cart.UpdatedCartDetails;
import org.ecom.model.coupon.CouponDTO;
import org.ecom.model.coupon.Details;
import org.ecom.model.enums.CouponType;
import org.ecom.model.product.ProductWiseDetails;
import org.ecom.model.product.Products;
import org.ecom.server.entity.BuyXGetYWiseCoupon;
import org.ecom.server.entity.TransactionProduct;
import org.ecom.server.repository.BuyXGetYWiseCouponRepository;
import org.ecom.server.service.BuyXGetYWiseCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyXGetYWiseCouponServiceImpl implements BuyXGetYWiseCouponService {

   @Autowired
   private BuyXGetYWiseCouponRepository buyXGetYWiseCouponRepository;

   @Override
   public BuyXGetYWiseCoupon createBuyXGetYWiseCoupon(CouponDTO couponDTO) {
      BuyXGetYWiseCoupon buyXGetYWiseCoupon = new BuyXGetYWiseCoupon();
      buyXGetYWiseCoupon.setCouponType(CouponType.BXGY_WISE);

      // Map Buy Products
      Set<TransactionProduct> buyProducts = mapTransactionProducts(couponDTO.getDetails().getBuyProducts());
      buyXGetYWiseCoupon.setBuyProducts(buyProducts);

      // Map Get Products
      Set<TransactionProduct> getProducts = mapTransactionProducts(couponDTO.getDetails().getGetProducts());
      buyXGetYWiseCoupon.setGetProducts(getProducts);

      buyXGetYWiseCoupon.setRepetitionLimit(couponDTO.getDetails().getRepetitionLimit());
      buyXGetYWiseCoupon.setEndDate(couponDTO.getEndDate());
      return buyXGetYWiseCoupon;
   }

   @Override
   public double applyBxGyWiseCoupon(CouponDTO coupon, CartDetails cart, UpdatedCartDetails updatedCartDetails) {
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

   @Override
   public Details getBuyXGetYCouponDetails(Long couponId, Details details) {
      BuyXGetYWiseCoupon bxgyCoupon = buyXGetYWiseCouponRepository.findByIdAndIsDeleted(couponId, false).orElse(null);
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
      return details;
   }

   private double calculateBuyXGetYDiscount(CartDetails cart, CouponDTO coupon, int maxRepetitions) {
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

   private int getMaxRepetitionsForBuyXGetY(CartDetails cart, CouponDTO coupon) {
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
}
