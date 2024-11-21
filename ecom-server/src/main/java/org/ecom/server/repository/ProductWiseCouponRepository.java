package org.ecom.server.repository;

import java.util.Optional;

import org.ecom.server.entity.ProductWiseCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductWiseCouponRepository extends JpaRepository<ProductWiseCoupon, Long> {

   Optional<ProductWiseCoupon> findByIdAndIsDeleted(Long id, boolean isDeleted);

}

