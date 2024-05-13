package com.miri.orderservice.domain.shipping;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {

    Optional<Shipping> findByOrderDetailId(Long orderDetailId);

    @Modifying
    @Query("UPDATE Shipping s SET s.shippingStatus = :status WHERE s.orderDetailId IN (SELECT od.id FROM OrderDetail od WHERE od.order.id = :orderId)")
    int updateShippingStatusByOrderDetailsOrderId(@Param("orderId") Long orderId, @Param("status") ShippingStatus status);

    @Modifying
    @Query("UPDATE Shipping s SET s.shippingStatus = 'IN_TRANSIT' " +
            "WHERE s.shippingStatus = 'PENDING' AND " +
            "FUNCTION('DATE', s.lastModifiedDate) < CURRENT_DATE")
    int updateShippingStatusToInTransitIfOlderThanADay();

    @Modifying
    @Query("UPDATE Shipping s SET s.shippingStatus = 'IN_TRANSIT' " +
            "WHERE s.shippingStatus = 'PENDING' AND " +
            "FUNCTION('DATE', s.lastModifiedDate) < CURRENT_DATE")
    int updateShippingStatusToDeliveredIfOlderThanADay();
}
