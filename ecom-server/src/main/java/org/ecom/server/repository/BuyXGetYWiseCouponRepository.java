package org.ecom.server.repository;

import java.util.Optional;

import org.ecom.server.entity.BuyXGetYWiseCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyXGetYWiseCouponRepository extends JpaRepository<BuyXGetYWiseCoupon, Long> {

   Optional<BuyXGetYWiseCoupon> findByIdAndIsDeleted(Long id, boolean isDeleted);

}

