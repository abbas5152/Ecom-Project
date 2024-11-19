package org.ecom.server.repository;

import java.util.List;
import java.util.Optional;

import org.ecom.server.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

   Optional<Coupon> findByIdAndIsDeleted(Long id, boolean isDeleted);

//   List<Coupon> findByIsDeletedFalse();
//
//   List<Coupon> findByTypeAndIsDeletedFalse(String type);
//
//   //List<Coupon> findByIsDeletedFalseAndExpirationDateAfter(LocalDateTime date);
}

