package com.miri.goodsservice.domain.shipping;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {

    Optional<Shipping> findByOrderDetailId(Long orderDetailId);
}
